package com.codexsoft.servicesupport.main.domain.type;

public enum CategoryType {
    CUSTOMER(""),
    INVESTMENT_ACCOUNT(""),
    CONSULTATION(""),
    SHAPE(""),
    TICKET(""),
    PRODUCT(""),
    COMMISSION_STATEMENT(""),
    CONTRACT("");

    private final String type;

    CategoryType(String type) {
        this.type = type;
    }
}
