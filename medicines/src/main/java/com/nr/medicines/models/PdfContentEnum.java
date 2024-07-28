package com.nr.medicines.models;

public enum PdfContentEnum {
    INFO("info"),
    IMAGE("image");

    private final String value;

    PdfContentEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
