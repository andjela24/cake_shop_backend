package com.andjela.diplomski.dto.order;

import com.andjela.diplomski.dto.address.AddressDto;
import com.andjela.diplomski.dto.orderItem.OrderItemDto;
import com.andjela.diplomski.dto.user.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String orderNumber;
    private UserDto user;
    private List<OrderItemDto> orderItems;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime deliveryDate;
    private AddressDto shippingAddress;
    private Integer totalPrice;
    private Integer totalDiscountedPrice;
    private Integer discount;
    private String orderStatus;
    private int totalItem;
    private String paymentId;
    private boolean isPaid;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionId;
}

