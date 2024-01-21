package com.upo.springtest.enums;

public enum BookingStatus {
    ACTIVE("Aktywne"),
    FINISHED("Zakończone"),
    CANCELED("Anulowane");


    private final String status;

    BookingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
