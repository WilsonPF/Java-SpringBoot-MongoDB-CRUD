package com.example.casas_bahia_vendedor_crud.models;

public class ServiceResult {
    public ServiceResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean success;

    public String message;
}
