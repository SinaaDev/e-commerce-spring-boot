package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AddItemRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartMapper cartMapper;
    private CartRepository cartRepository;
    private ProductRepository productRepository;

    public CartDto createCart(){
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException();
        }


        var cartItem = cart.addItemOrIncreaseQuantity(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateProduct(UUID cartId, Long productId, int quantity){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        CartItem cartItem = cart.getItemById(productId);

        if(cartItem == null){
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteCartItem(UUID cartId, Long productId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        CartItem cartItem = cart.getItemById(productId);

        if(cartItem == null){
            throw new ProductNotFoundException();
        }

        cart.getItems().remove(cartItem);
        cartItem.setCart(null);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){

        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);
    }
}
