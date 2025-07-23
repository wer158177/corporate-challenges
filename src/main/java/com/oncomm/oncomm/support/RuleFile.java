package com.oncomm.oncomm.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RuleFile {

    @JsonProperty("companies")
    private List<CompanyRule> companies;
}
