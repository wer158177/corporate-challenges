package com.oncomm.oncomm.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Rule {

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("keywords")
    private List<String> keywords;

    public boolean matches(String description) {
        return keywords != null && keywords.stream().anyMatch(description::contains);
    }
}
