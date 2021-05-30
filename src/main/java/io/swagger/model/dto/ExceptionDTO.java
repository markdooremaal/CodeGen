package io.swagger.model.dto;

public class ExceptionDTO {
    private String reason;
    private String message;

    public ExceptionDTO(String message) {
        this.message = message;
    }

    public ExceptionDTO(String reason, String message) {
        this.reason = reason;
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}