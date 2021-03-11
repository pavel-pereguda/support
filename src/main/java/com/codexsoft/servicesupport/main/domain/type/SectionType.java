package com.codexsoft.servicesupport.main.domain.type;

public enum SectionType {
    GENERAL("Allgemein"),
    INVESTMENT("Investment"),
    INSURANCE("Versicherung");

    private final String type;

    SectionType(String type) {
        this.type = type;
    }
}
