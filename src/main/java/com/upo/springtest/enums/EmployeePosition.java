package com.upo.springtest.enums;

public enum EmployeePosition {
    SALES_AGENT("Agent Sprzedaży"),
    CUSTOMER_SERVICE("Przedstawiciel Obsługi Klienta"),
    MECHANIC("Mechanik"),
    FINANCE("Pracownik Działu Finansowego"),
    DELETED("Pracownik nie istnieje");

    private final String position;

    EmployeePosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
