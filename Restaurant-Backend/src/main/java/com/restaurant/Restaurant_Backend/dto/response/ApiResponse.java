package com.restaurant.Restaurant_Backend.dto.response;

/**
 * Generic envelope for every REST response:
 * { "success": true, "message": "...", "data": { ... } }
 */
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // ── Constructors ──────────────────────────────────────────────────────────

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data    = data;
    }

    // ── Static factory methods ────────────────────────────────────────────────

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.data    = data;
        return r;
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.message = message;
        r.data    = data;
        return r;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = false;
        r.message = message;
        return r;
    }

    // ── Builder (kept for GlobalExceptionHandler compatibility) ───────────────

    public static <T> Builder<T> builder() { return new Builder<>(); }

    public static class Builder<T> {
        private boolean success;
        private String message;
        private T data;

        public Builder<T> success(boolean v)  { this.success = v; return this; }
        public Builder<T> message(String v)   { this.message = v; return this; }
        public Builder<T> data(T v)           { this.data = v;    return this; }

        public ApiResponse<T> build() {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = this.success;
            r.message = this.message;
            r.data    = this.data;
            return r;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public boolean isSuccess()              { return success; }
    public void    setSuccess(boolean v)    { this.success = v; }

    public String getMessage()              { return message; }
    public void   setMessage(String v)      { this.message = v; }

    public T getData()                      { return data; }
    public void setData(T v)               { this.data = v; }
}