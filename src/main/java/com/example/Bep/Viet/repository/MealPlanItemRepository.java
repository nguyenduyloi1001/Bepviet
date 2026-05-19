package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.enums.DayOfWeek;
import com.example.Bep.Viet.enums.MealTime;
import com.example.Bep.Viet.model.MealPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
    boolean existsByMealPlanIdAndDayOfWeekAndMealTime(Long mealPlanId, DayOfWeek dayOfWeek, MealTime mealTime);
    void deleteByMealPlanIdAndDayOfWeekAndMealTime(Long mealPlanId, DayOfWeek dayOfWeek, MealTime mealTime);
}
