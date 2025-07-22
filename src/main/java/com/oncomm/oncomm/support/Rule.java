package com.oncomm.oncomm.support;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Rule {

    private String categoryId;
    private String categoryName;
    private List<String> keywords;

    public boolean matches(String description) {
        return keywords != null && keywords.stream().anyMatch(description::contains);
    }
}
