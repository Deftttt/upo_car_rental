package com.upo.springtest.enums;

public enum TransmitionType {
    MANUAL("Manualna"),
    AUTOMATIC("Automatyczna");

    private final String type;

    TransmitionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
