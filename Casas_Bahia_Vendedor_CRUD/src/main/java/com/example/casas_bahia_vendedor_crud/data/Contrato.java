package com.example.casas_bahia_vendedor_crud.data;

public enum Contrato {
    OUT("Outsoursing (terceirizado)"),
    CLT("CLT"),
    PJ("Pessoa Jurídica");

    private String displayName;

    Contrato(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}
