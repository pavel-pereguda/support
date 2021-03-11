package com.codexsoft.servicesupport.main.domain.type;

public enum IssueType {
    CREATION(""),
    DATA(""),
    SHARING(""),
    COMPENSATION("Vergütung"),
    PRODUCT_IDEAS("Produktideen"),
    TRADABILITIES("Handelbarkeiten"),
    BASE_DATA("Stammdaten"),
    STATEMENT("Statement"),
    GENERAL("Allgemein");

    private final String type;

    IssueType(String type) {
        this.type = type;
    }
}
