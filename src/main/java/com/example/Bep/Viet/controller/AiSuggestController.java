package com.example.Bep.Viet.controller;

import com.example.Bep.Viet.request.AiSuggestRequest;
import com.example.Bep.Viet.response.AiSuggestResponse;
import com.example.Bep.Viet.service.AiSuggestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiSuggestController {

    private final AiSuggestService aiSuggestService;

    @PostMapping("/suggest-recipes")
    public ResponseEntity<AiSuggestResponse> suggest(
            @RequestBody @Valid AiSuggestRequest request) {
        return ResponseEntity.ok(aiSuggestService.suggest(request));
    }
}
