package com.andjela.diplomski.service;

import com.andjela.diplomski.dto.orderItem.OrderItemDto;
import com.andjela.diplomski.dto.orderItem.OrderItemMapper;
import com.andjela.diplomski.entity.OrderItem;
import com.andjela.diplomski.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderItemService implements IOrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = OrderItemMapper.MAPPER.mapToOrderItem(orderItemDto);
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.MAPPER.mapToOrderItemDto(savedOrderItem);
    }
}
