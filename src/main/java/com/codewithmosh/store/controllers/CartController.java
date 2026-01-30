package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItemRequest;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("carts")
@Tag(name = "Carts")
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @PostMapping
    @Operation(summary = "Create a new cart")
    public ResponseEntity<CartDto> createCart(){
        var cartDto = cartService.createCart();
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add item to the cart")
    public ResponseEntity<CartItemDto> addItem(@PathVariable UUID cartId, @RequestBody AddItemRequest request){
        var cartItemDto = cartService.addToCart(cartId,request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("{cartId}/items")
    @Operation(summary = "Fetch a card")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        CartDto cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok().body(cartDto);
    }

    @PutMapping("{cartId}/items/{productId}")
    @Operation(summary = "Update a product inside a cart")
    public ResponseEntity<CartItemDto> updateItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
            ){
        CartItemDto cartItemDto = cartService.updateProduct(cartId,productId,request.getQuantity());
        return ResponseEntity.ok().body(cartItemDto);
    }

    @DeleteMapping("{cartId}/items/{productId}")
    @Operation(summary = "Delete a product from cart")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
            ){
        cartService.deleteCartItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{cartId}/items")
    @Operation(summary = "Clear a cart")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Product not found in cart"));
    }
}
