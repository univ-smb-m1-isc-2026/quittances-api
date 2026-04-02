package com.nobless.quittances.controller.dto;

public class ApiResponse<T> {
    private T data;
    private String state;

    public ApiResponse(T data, String state) {
        this.data = data;
        this.state = state;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, "[SUCCESS]");
    }

    public static <T> ApiResponse<T> success(T data, String details) {
        return new ApiResponse<>(data, buildState("SUCCESS", details));
    }

    public static <T> ApiResponse<T> info(T data, String details) {
        return new ApiResponse<>(data, buildState("INFO", details));
    }

    public static <T> ApiResponse<T> error(String details) {
        return new ApiResponse<>(null, buildState("ERROR", details));
    }

    private static String buildState(String level, String details) {
        String normalizedDetails = details == null ? "" : details.trim();
        return normalizedDetails.isEmpty() ? "[" + level + "]" : "[" + level + "] " + normalizedDetails;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}