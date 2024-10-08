package com.andjela.diplomski.controller;

import com.andjela.diplomski.dto.cartItem.CartItemDto;
import com.andjela.diplomski.entity.User;
import com.andjela.diplomski.service.CartItemService;
import com.andjela.diplomski.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final UserService userService;

    @PutMapping("{id}/increase-weight")
    public ResponseEntity<CartItemDto> increaseCartItemWeight(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        CartItemDto updatedCartItemDto = cartItemService.increaseCartItemWeight(user.getId(), id);
        return new ResponseEntity<>(updatedCartItemDto, HttpStatus.OK);
    }

    @PutMapping("{id}/decrease-weight")
    public ResponseEntity<CartItemDto> decreaseCartItemWeight(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        CartItemDto updatedCartItemDto = cartItemService.decreaseCartItemWeight(user.getId(), id);
        return new ResponseEntity<>(updatedCartItemDto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
