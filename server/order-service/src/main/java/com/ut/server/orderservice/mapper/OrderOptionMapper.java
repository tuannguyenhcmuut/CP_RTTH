package com.ut.server.orderservice.mapper;

import com.netflix.discovery.converters.Auto;
import com.ut.server.orderservice.dto.OrderOptionDto;
import com.ut.server.orderservice.model.Order;
import com.ut.server.orderservice.model.OrderOption;
import com.ut.server.orderservice.model.OrderOptionType;
import com.ut.server.orderservice.repo.OptionTypeRepository;
import com.ut.server.orderservice.repo.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderOptionMapper {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OptionTypeRepository optionTypeRepository;
    public OrderOption mapDtoToEntity(OrderOptionDto orderOptionDto) {
        // find order
        Optional<Order> order = orderRepository.findById(orderOptionDto.getIds().getOrderId());
        Optional<OrderOptionType> orderOptionType = optionTypeRepository.findById(orderOptionDto.getIds().getTypeId());
        if (!order.isPresent()) {
            throw new RuntimeException("Order in Option not found");
        }
        if (!orderOptionType.isPresent()) {
            throw new RuntimeException("Order Option Type not found");
        }

        // order mapping
        return OrderOption.builder()
                .optionIds(orderOptionDto.getIds())
                .order(order.get())
                .orderOptionType(orderOptionType.get())
                .isChecked(orderOptionDto.getIsChecked())
                .build();
    }

    public List<OrderOption> mapDtosToEntities(List<OrderOptionDto> orderOptionDtos) {
        return orderOptionDtos.stream().map(
                orderOptionDto -> mapDtoToEntity(orderOptionDto)
        ).collect(Collectors.toList());
    }

    public OrderOptionDto mapToDto(OrderOption orderOption) {
        return OrderOptionDto.builder()
                .ids(orderOption.getOptionIds())
                .description(orderOption.getOrderOptionType().getDescription())
                .isChecked(orderOption.getIsChecked())
                .build();
    }

    public List<OrderOptionDto> mapToDtos(List<OrderOption> orderOptions) {
        return orderOptions.stream().map(
                orderOption -> mapToDto(orderOption)
        ).collect(Collectors.toList());
    }
}
