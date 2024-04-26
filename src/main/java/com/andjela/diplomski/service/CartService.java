package com.andjela.diplomski.service;

import com.andjela.diplomski.dto.cake.CakeDto;
import com.andjela.diplomski.dto.cake.CakeMapper;
import com.andjela.diplomski.dto.cart.CartDto;
import com.andjela.diplomski.dto.cart.CartMapper;
import com.andjela.diplomski.dto.cartItem.CartItemDto;
import com.andjela.diplomski.dto.cartItem.CartItemMapper;
import com.andjela.diplomski.dto.user.UserDto;
import com.andjela.diplomski.dto.user.UserMapper;
import com.andjela.diplomski.entity.Cake;
import com.andjela.diplomski.entity.Cart;
import com.andjela.diplomski.entity.CartItem;
import com.andjela.diplomski.entity.User;
import com.andjela.diplomski.repository.CakeRepository;
import com.andjela.diplomski.repository.CartRepository;
import com.andjela.diplomski.request.AddItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final CakeService cakeService;

    @Override
    public CartDto createCart(UserDto userDto) {
        User user = UserMapper.MAPPER.mapToUser(userDto);
        Cart cart = Cart.builder()
                .user(user)
                .build();
        cartRepository.save(cart);
        return CartMapper.MAPPER.mapToCartDto(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) {
        Cart cart = cartRepository.findUserById(userId);
        CartDto cartDto = CartMapper.MAPPER.mapToCartDto(cart);

        CakeDto cakeDto = cakeService.getCakeById(req.getCakeId());
        Cake cake = CakeMapper.MAPPER.mapToCake(cakeDto);

        CartItemDto cartItemDto = cartItemService.isCartItemExists(cartDto, cakeDto, userId);
        if (cartItemDto == null) {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .cake(cake)
                    .userId(userId)
                    .build();
            CartItemDto createdCartItemDto = CartItemMapper.MAPPER.mapToCartItemDto(cartItem);
            cartItemService.createCartItem(createdCartItemDto);
            cart.getCartItems().add(CartItemMapper.MAPPER.mapToCartItem(createdCartItemDto));
        }
        return "Item added to cart";
    }

    @Override
    public CartDto getUserCart(Long userId) {
        Cart cart = cartRepository.findUserById(userId);
        int totalPrice = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getTotalPrice();
        }
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
        return CartMapper.MAPPER.mapToCartDto(cart);
    }
}
