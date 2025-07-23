package com.oncomm.oncomm.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CompanyRule {

    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("categories")
    private List<Rule> categories;
}
