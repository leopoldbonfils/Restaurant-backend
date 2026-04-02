package com.restaurant.Restaurant_Backend.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** POST /api/customers – check a customer into a table. */
@Data
public class CustomerCheckInRequest {

    @NotBlank(message = "Table number is required")
    private String tableNumber;

    private String name;           // optional
    private String phone;          // optional
    private String preferredLanguage = "en";
}
