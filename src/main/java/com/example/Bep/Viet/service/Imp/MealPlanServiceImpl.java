package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.enums.DayOfWeek;
import com.example.Bep.Viet.enums.MealTime;
import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.model.MealPlan;
import com.example.Bep.Viet.model.MealPlanItem;
import com.example.Bep.Viet.model.Recipe;
import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.repository.MealPlanItemRepository;
import com.example.Bep.Viet.repository.MealPlanRepository;
import com.example.Bep.Viet.repository.RecipeRepository;
import com.example.Bep.Viet.repository.UserRepository;
import com.example.Bep.Viet.request.MealPlanItemRequest;
import com.example.Bep.Viet.request.MealPlanRequest;
import com.example.Bep.Viet.response.MealPlanItemResponse;
import com.example.Bep.Viet.response.MealPlanResponse;
import com.example.Bep.Viet.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealPlanServiceImpl implements MealPlanService {
    private final MealPlanRepository repository;
    private final MealPlanItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    @Override
    public MealPlanResponse create(MealPlanRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        MealPlan mealPlan = MealPlan.builder()
                .user(user)
                .name(request.getName())
                .weekStartDate(request.getWeekStartDate())
                .build();

        return mapToResponse(repository.save(mealPlan));
    }

    @Override
    public MealPlanResponse getById(Long id, Long currentUserId) {
        MealPlan mealPlan = findById(id);
        checkOwner(mealPlan, currentUserId);
        return mapToResponse(mealPlan);
    }

    @Override
    public List<MealPlanResponse> getByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MealPlanResponse addItem(Long mealPlanId, MealPlanItemRequest request, Long currentUserId) {
        MealPlan mealPlan = findById(mealPlanId);
        checkOwner(mealPlan, currentUserId);

        if (itemRepository.existsByMealPlanIdAndDayOfWeekAndMealTime(
                mealPlanId, request.getDayOfWeek(), request.getMealTime())) {
            throw new AppException(ErrorCode.MEAL_PLAN_ITEM_EXISTED);
        }

        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND));

        MealPlanItem item = MealPlanItem.builder()
                .mealPlan(mealPlan)
                .recipe(recipe)
                .dayOfWeek(request.getDayOfWeek())
                .mealTime(request.getMealTime())
                .build();

        itemRepository.save(item);
        return mapToResponse(findById(mealPlanId));
    }

    @Override
    public MealPlanResponse updateItem(Long mealPlanId, MealPlanItemRequest request, Long currentUserId) {
        MealPlan mealPlan = findById(mealPlanId);
        checkOwner(mealPlan, currentUserId);

        itemRepository.deleteByMealPlanIdAndDayOfWeekAndMealTime(
                mealPlanId, request.getDayOfWeek(), request.getMealTime());

        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND));

        MealPlanItem item = MealPlanItem.builder()
                .mealPlan(mealPlan)
                .recipe(recipe)
                .dayOfWeek(request.getDayOfWeek())
                .mealTime(request.getMealTime())
                .build();

        itemRepository.save(item);
        return mapToResponse(findById(mealPlanId));
    }

    @Override
    public void removeItem(Long mealPlanId, DayOfWeek dayOfWeek, MealTime mealTime, Long currentUserId) {
        MealPlan mealPlan = findById(mealPlanId);
        checkOwner(mealPlan, currentUserId);
        itemRepository.deleteByMealPlanIdAndDayOfWeekAndMealTime(mealPlanId, dayOfWeek, mealTime);
    }

    @Override
    public void delete(Long id, Long currentUserId) {
        MealPlan mealPlan = findById(id);
        checkOwner(mealPlan, currentUserId);
        repository.delete(mealPlan);
    }
    private MealPlan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MEAL_PLAN_NOT_FOUND));
    }

    private void checkOwner(MealPlan mealPlan, Long currentUserId) {
        if (!mealPlan.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
    }

    private MealPlanResponse mapToResponse(MealPlan mp) {
        return MealPlanResponse.builder()
                .id(mp.getId())
                .userId(mp.getUser().getId())
                .name(mp.getName())
                .weekStartDate(mp.getWeekStartDate())
                .items(mp.getItems() == null ? List.of() :
                        mp.getItems().stream().map(this::mapItemToResponse).toList())
                .createdAt(mp.getCreatedAt())
                .build();
    }

    private MealPlanItemResponse mapItemToResponse(MealPlanItem item) {
        return MealPlanItemResponse.builder()
                .id(item.getId())
                .recipeId(item.getRecipe().getId())
                .recipeName(item.getRecipe().getName())
                .recipeImageUrl(item.getRecipe().getImageUrl())
                .dayOfWeek(item.getDayOfWeek())
                .mealTime(item.getMealTime())
                .build();
    }
}
