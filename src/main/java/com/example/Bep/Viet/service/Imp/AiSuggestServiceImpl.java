package com.example.Bep.Viet.service.Imp;
import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.repository.IngredientRepository;
import com.example.Bep.Viet.repository.RecipeRepository;
import com.example.Bep.Viet.request.AiSuggestRequest;
import com.example.Bep.Viet.response.AiRawResult;
import com.example.Bep.Viet.response.AiSuggestResponse;
import com.example.Bep.Viet.service.AiSuggestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiSuggestServiceImpl implements AiSuggestService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final RecipeRepository recipeRepository;
    private final ObjectMapper objectMapper;
    private final IngredientRepository ingredientRepository;
    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    @Override
    public AiSuggestResponse suggest(AiSuggestRequest request) {
        // Bước 1: Normalize input
        List<String> keywords = request.getIngredients().stream()
                .map(s -> s.trim().toLowerCase())
                .distinct()
                .toList();

        // Bước 2: Expand keyword → tên nguyên liệu thật trong DB
        // "gà" → "thịt gà", "gà hầm", ...
        List<String> expandedIngredients = keywords.stream()
                .flatMap(keyword ->
                        ingredientRepository.findByNameContainingIgnoreCase(keyword)
                                .stream()
                                .map(i -> i.getName().toLowerCase())
                )
                .distinct()
                .toList();

        if (expandedIngredients.isEmpty()) {
            throw new AppException(ErrorCode.AI_NO_SUGGESTION);
        }

        // Bước 3: Query DB
        List<Object[]> rows = recipeRepository.findCandidatesByIngredients(expandedIngredients);
        if (rows.isEmpty()) {
            throw new AppException(ErrorCode.AI_NO_SUGGESTION);
        }

        // Bước 4: Build context
        List<Map<String, Object>> context = rows.stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id",           ((Number) row[0]).longValue());
            map.put("name",         row[1]);
            map.put("cooking_time", row[3]);
            map.put("avg_rating",   row[4]);
            String rawIngredients = (String) row[5];
            List<String> ingredientList = Arrays.stream(rawIngredients.split("\\|"))
                    .map(s -> {
                        String[] parts = s.split(":");
                        String qty  = parts.length > 1 ? parts[1] : "";
                        String unit = parts.length > 2 ? parts[2] : "";
                        return parts[0] + (qty.isEmpty() ? "" : " (" + qty + " " + unit + ")");
                    }).toList();
            map.put("ingredients", ingredientList);
            return map;
        }).toList();

        // Bước 5: Gọi Groq
        AiRawResult aiResult = callGroq(buildPrompt(expandedIngredients, context, request));

        // Bước 6: Map kết quả
        Map<Long, Object[]> rowMap = rows.stream()
                .collect(Collectors.toMap(r -> ((Number) r[0]).longValue(), r -> r));

        List<AiSuggestResponse.SuggestionItem> suggestions = aiResult.getSuggestions()
                .stream()
                .filter(s -> rowMap.containsKey(s.getRecipeId()))
                .map(s -> {
                    Object[] row = rowMap.get(s.getRecipeId());
                    return AiSuggestResponse.SuggestionItem.builder()
                            .recipeId(s.getRecipeId())
                            .title((String) row[1])
                            .thumbnail((String) row[2])
                            .avgRating(row[4] != null
                                    ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO)
                            .cookingTime(row[3] != null
                                    ? ((Number) row[3]).intValue() : null)
                            .matchScore(s.getMatchScore())
                            .missingIngredients(s.getMissingIngredients())
                            .aiReason(s.getReason())
                            .build();
                }).toList();

        return AiSuggestResponse.builder().suggestions(suggestions).build();
    }

    // =============== PRIVATE ===============

    private String buildPrompt(
            List<String> ingredients,
            List<Map<String, Object>> context,
            AiSuggestRequest request) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Người dùng có: ").append(String.join(", ", ingredients)).append(".\n");
            if ("chay".equalsIgnoreCase(request.getDiet()))
                sb.append("Người dùng ăn chay.\n");
            if (request.getCookingTimeMax() != null)
                sb.append("Thời gian tối đa: ").append(request.getCookingTimeMax()).append(" phút.\n");

            sb.append("\nDanh sách công thức:\n")
                    .append(objectMapper.writeValueAsString(context));

            sb.append("""
                \nChọn TOP 5 phù hợp nhất. Ưu tiên: nhiều nguyên liệu khớp → thời gian ngắn → rating cao.
                Trả về JSON sau, KHÔNG thêm text nào khác:
                {
                  "suggestions": [
                    {
                      "recipe_id": <số>,
                      "match_score": <0-100>,
                      "missing_ingredients": ["tên nguyên liệu còn thiếu"],
                      "reason": "Giải thích ngắn bằng tiếng Việt"
                    }
                  ]
                }
                """);
            return sb.toString();
        } catch (Exception e) {
            throw new AppException(ErrorCode.AI_SERVICE_ERROR);
        }
    }

    private AiRawResult callGroq(String prompt) {
        try {
            Map<String, Object> body = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + groqApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> res = client.send(req,
                    HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() != 200) {
                log.error("Groq API lỗi {}: {}", res.statusCode(), res.body());
                throw new AppException(ErrorCode.AI_SERVICE_ERROR);
            }

            // Parse response Groq (chuẩn OpenAI)
            JsonNode root = objectMapper.readTree(res.body());
            String rawText = root
                    .path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Strip markdown fence nếu có
            String clean = rawText.replaceAll("(?s)```json|```", "").trim();

            return objectMapper.readValue(clean, AiRawResult.class);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Lỗi gọi Groq: ", e);
            throw new AppException(ErrorCode.AI_SERVICE_ERROR);
        }
    }
}