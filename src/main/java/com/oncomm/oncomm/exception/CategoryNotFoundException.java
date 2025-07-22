package com.oncomm.oncomm.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String categoryId) {
        super("Category not found: " + categoryId);
    }
}