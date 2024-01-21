package com.upo.springtest.enums;

public enum FuelType {
    GASOLINE("Benzyna"),
    DIESEL("Diesel"),
    ELECTRIC("Elektryczny"),
    LPG("Na gaz");

    private final String type;

    FuelType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
