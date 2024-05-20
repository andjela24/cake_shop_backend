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
import com.andjela.diplomski.repository.CartItemRepository;
import com.andjela.diplomski.repository.CartRepository;
import com.andjela.diplomski.repository.UserRepository;
import com.andjela.diplomski.request.AddItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final CakeService cakeService;
    private final CakeRepository cakeRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public CartDto createCart(UserDto userDto) {
        User user = UserMapper.MAPPER.mapToUser(userDto);
        Cart cart = Cart.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        cartRepository.save(cart);
        return CartMapper.MAPPER.mapToCartDto(cart);
    }

    //Trenutno je moguce da se doda vise istih torti
    @Override
    public String addCartItem(Long userId, CartItemDto req) {
        Cart cart = cartRepository.findUserById(userId);
//        User user = userRepository.findById(userId).orElse(null);

        Cake cake = cakeRepository.findById(req.getCakeId()).orElseThrow(() -> new RuntimeException("Cake with id " + req.getCakeId() + " not found"));
        CartItem cartItem = createCartItem(userId, req, cake, cart);
        cartItemRepository.save(cartItem);



//        if (cart == null) {
//            Cart newCart = Cart.builder()
//                    .user(user)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            cartRepository.save(newCart);
//
//            CartItem cartItem = createCartItem(userId, req, cake, newCart);
//            cartItemRepository.save(cartItem);
//            newCart.setCartItems(List.of(cartItem));
//            newCart.setDiscount(10); //default discount for online purchase
//            newCart.setTotalItem(newCart.getCartItems().size());
//            newCart.setTotalPrice();
//
//
//        } else {
//            CartItem cartItem = createCartItem(userId, req, cake, cart);
//            cartItemRepository.save(cartItem);
//            cart.getCartItems().add(cartItem);
//        }
        return "Item added to cart";
    }

    private static CartItem createCartItem(Long userId, CartItemDto req, Cake cake, Cart newCart) {
        CartItem cartItem = CartItem.builder()
                .selectedWeight(req.getSelectedWeight())
                .selectedTiers(req.getSelectedTiers())
                .piecesNumber(req.getPiecesNumber())
                .totalPrice((int) (cake.getPricePerKilo() * req.getSelectedWeight()))
                .cake(cake)
                .flavors(req.getFlavors())
                .note(req.getNote())
                .fakeTier(req.getFakeTier())
                .cart(newCart)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
        return cartItem;
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
