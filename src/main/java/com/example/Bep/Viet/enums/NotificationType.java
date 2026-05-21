package com.example.Bep.Viet.enums;
public enum NotificationType {
    new_follower,       // B bắt đầu follow A
    new_comment,        // ai đó comment vào recipe/post của A
    new_like,           // ai đó like recipe/post/comment của A
    new_answer,         // ai đó trả lời comment của A
    recipe_approved,    // recipe của A được admin duyệt
    recipe_rejected     // recipe của A bị admin từ chối
}
