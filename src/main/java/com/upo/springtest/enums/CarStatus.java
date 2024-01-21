package com.upo.springtest.enums;

public enum CarStatus {
    IN_USE("W użytku"),
    IN_REPAIR("W naprawie"),
    NEEDS_MAINTENANCE("Wymaga konserwacji"),
    OUT_OF_SERVICE("Wyłączony z użytku");

    private final String status;

    CarStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}