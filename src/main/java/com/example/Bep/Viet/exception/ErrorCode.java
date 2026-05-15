package com.example.Bep.Viet.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(1001, "Không tìm thấy người dùng"),
    USERNAME_EXISTED(1002, "Username đã tồn tại"),
    EMAIL_EXISTED(1003, "Email đã tồn tại"),
    UNAUTHORIZED(1004, "Không có quyền truy cập"),
    CATEGORY_NOT_FOUND(1005, "Category không tồn tại"),
    SLUG_EXISTED(1006, "Slug đã tồn tại"),

    INGREDIENT_NOT_FOUND(2001,"Không tìm thấy nguyên liệu"),
    INGREDIENT_NAME_EXISTED(2002,"Tên nguyên liệu đã tồn tại"),
    INGREDIENT_SLUG_EXISTED(2003,"Slug nguyên liệu đã tồn tại"),

    RECIPE_NOT_FOUND(3001,"Công thức không tồn tại"),
    RECIPE_NAME_EXISTED(3002,"Tên công thức đã tồn tại"),
    RECIPE_SLUG_EXISTED(3003,"Slug đã tồn tại công thức"),
    RECIPE_SLUG_NOT_FOUND(3004,"Không tìm thấy Slug"),
    RECIPE_FORBIDDEN(3005,"Bạn không có quyền thực hiện hành dộng này"),
    RECIPE_INGREDIENT_NOT_FOUND(3005, "Không tìm thấy nguyên liệu trong công thức"),
    INGREDIENT_ALREADY_IN_RECIPE(3006, "Nguyên liệu đã tồn tại trong công thức này"),
    RECIPE_STEP_NOT_FOUND(3007, "Bước thực hiện không tồn tại"),
    STEP_NUMBER_EXISTED(3008, "Số thứ tự bước đã tồn tại trong công thức này"),
    COOKBOOK_NOT_FOUND(4001,"Không tìm thấy Cookbook"),
    COOKBOOK_NAME_EXISTED(4002,"Tên Cookbook đã tồn tại"),
    COOKBOOK_FORBIDDEN(4003,"Bạn không có quyền thao tác trên Cookbook này"),
    RECIPE_ALREADY_IN_COOKBOOK(4004,"Công thức đã tồn tại trong Cookbook"),
    RECIPE_NOT_IN_COOKBOOK(4005,"Công thức không tồn tại trong Cookbook"),
    POST_NOT_FOUND(4006,"Bài viết không tồn tại"),
    POST_FORBIDDEN(4007,"Bạn không có quyền thao tác bài viết này"),
    COMMENT_NOT_FOUND(4008,"Không tìm thấy comment");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
