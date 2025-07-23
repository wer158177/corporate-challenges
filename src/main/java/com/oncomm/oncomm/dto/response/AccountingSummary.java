package com.oncomm.oncomm.dto.response;


import lombok.Value;

@Value
public class AccountingSummary {
    String categoryId;
    String categoryName;
    Long totalIncome;
    Long totalExpenditure;
}
