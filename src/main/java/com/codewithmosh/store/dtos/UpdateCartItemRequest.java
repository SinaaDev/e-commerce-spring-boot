package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity can not be null")
    @Min(value = 1,message = "Quantity must be at least one")
    private int quantity;
}
