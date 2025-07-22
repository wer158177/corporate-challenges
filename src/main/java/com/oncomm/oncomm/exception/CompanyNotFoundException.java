package com.oncomm.oncomm.exception;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String companyId) {
        super("Company not found: " + companyId);
    }
}