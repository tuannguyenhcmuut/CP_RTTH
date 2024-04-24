package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.ChartStatisticsDto;
import org.ut.server.omsserver.dto.OrderDto;
import org.ut.server.omsserver.dto.PriceDto;
import org.ut.server.omsserver.dto.TopReceiverDto;
import org.ut.server.omsserver.dto.request.OrderOptionRequest;
import org.ut.server.omsserver.dto.request.OrderRequest;
import org.ut.server.omsserver.dto.request.StatusRequest;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.OrderService;
import org.ut.server.omsserver.utils.RestParamUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderController {
    //    jwtUtils
    private final JwtUtils jwtUtils;
    private final OrderService orderService;

    // retrieve all orders of a user
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<OrderDto>> getAllOrders(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate,desc") String[] sort
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        Pageable pageable = RestParamUtils.getPageable(page, size, sort);
        List<OrderDto> orders = orderService.getAllOrders(userId, pageable);

        return GenericResponseDTO.<List<OrderDto>>builder()
                .data(orders)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_ORDER)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(e.getMessage());
//            return GenericResponseDTO.<List<OrderDto>>builder()
//                            .code(e.getMessage())
//                            .timestamps(new Date())
//                            .message(MessageConstants.UNSUCCESSFUL_GET_ORDER)
//                    .build();
//        }
    }

    // retrieve an order of a user by order id
    // http://localhost:8083/api/v1/order/1/user/1
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<OrderDto> getOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.getOrderById(userId, orderId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_ORDER)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(MessageCode.CREATED_FAILED.toString());
//            return GenericResponseDTO.<OrderDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .build();
//        }
    }

    // get order of owner by id
    @GetMapping("/{orderId}/owner")
    @PreAuthorize("hasAnyAuthority('VIEW_ONLY', 'CREATE_ORDER', 'UPDATE_ORDER','MANAGE_ORDER')")
    public GenericResponseDTO<OrderDto> getOwnerOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.getOwnerOrderById(userId, orderId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_ORDER)
                .timestamps(new Date())
                .build();
    }

    // create an order
    // http://localhost:8083/api/v1/order/user/1
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<OrderDto> createOrder(
            @RequestBody OrderRequest orderRequest,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        orderRequest.setUserId(userId);
        OrderDto newOrderDto = orderService.createOrder(orderRequest);
        return GenericResponseDTO.<OrderDto>builder()
                .data(newOrderDto)
                .code(MessageCode.CREATED_SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_CREATED)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(String.format("[CREATED ORDER FAILED: %s]",e.getMessage()));
//            return GenericResponseDTO.<OrderDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_ORDER_CREATED)
//                    .build();
//        }
    }

    // update an order status
//    @PatchMapping("/{order_id}/user/{userId}/status/{status_value}")
//    public ResponseEntity<String> updateOrderStatus(@PathVariable UUID userId, @PathVariable Long order_id, @PathVariable StatusRequest statusRequest) {
//        return orderService.updateOrderStatus(userId, order_id, statusRequest);
//    }

//    update entire order by orderId
    @PutMapping("/{orderId}")
    public GenericResponseDTO<OrderDto> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderDto orderDto,
            @RequestHeader("Authorization") String token
    ) {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            OrderDto updatedOrderDto = orderService.updateOrder(userId, orderId, orderDto);
            return GenericResponseDTO.<OrderDto>builder()
                    .data(updatedOrderDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                    .timestamps(new Date())
                    .build();
    }

    @PatchMapping("/{orderId}/status")
    public GenericResponseDTO<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody StatusRequest status,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto updatedOrderDto = orderService.updateOrderStatus(userId, orderId, status.getNewStatus().name());
        return GenericResponseDTO.<OrderDto>builder()
                .data(updatedOrderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
    }


    // delete an order
    @DeleteMapping("/{orderId}")
    public GenericResponseDTO<String> deleteOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        orderService.deleteOrder(userId, orderId);
        return GenericResponseDTO.<String>builder()
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_DELETED)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e) {
//            log.error(MessageCode.CREATED_FAILED.toString());
//            return GenericResponseDTO.<String>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_ORDER_DELETED)
//                    .build();
//        }
    }

    // retrieve order form information for order: orderOptionType, receipt by userId, products by userId, store by userId


    // http://localhost:8083/api/v1/order/2/user/1/receiver/1
    @PatchMapping("/{orderId}/receiver/{receiverId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_ORDER','MANAGE_ORDER')")
    public GenericResponseDTO<OrderDto> updateReceiver(
            @PathVariable Long orderId,
            @PathVariable Long receiverId,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.updateReceiver(userId, orderId, receiverId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
//        }
//        catch (Exception e){
//            log.error(MessageCode.CREATED_FAILED.toString());
//            return GenericResponseDTO.<OrderDto>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstants.UNSUCCESSFUL_ORDER_UPDATED)
//                    .build();
//        }
    }

//    update owner receiver
    @PatchMapping("/{orderId}/owner/receiver/{receiverId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_ORDER','MANAGE_ORDER')")
    public GenericResponseDTO<OrderDto> updateOwnerReceiver(
            @PathVariable Long orderId,
            @PathVariable Long receiverId,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.updateOwnerReceiver(userId, orderId, receiverId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
    }

//    update store by order id
    @PatchMapping("/{orderId}/store/{storeId}")
    public GenericResponseDTO<OrderDto> updateStore(
            @PathVariable Long orderId,
            @PathVariable Long storeId,
            @RequestHeader("Authorization") String token
    ) {
//        try {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.updateStore(userId, orderId, storeId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
    }

    //    update owner order, store
    @PatchMapping("/{orderId}/owner/store/{storeId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_ORDER','MANAGE_ORDER')")
    public GenericResponseDTO<OrderDto> updateOwnerStore(
            @PathVariable Long orderId,
            @PathVariable Long storeId,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto orderDto = orderService.updateOwnerStore(userId, orderId, storeId);
        return GenericResponseDTO.<OrderDto>builder()
                .data(orderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
    }



    // calculate the total price and option's prices based on order options 
    @PostMapping("/price/calculate")
    public List<PriceDto> postMethodName(
        @RequestBody List<OrderOptionRequest> options,
        @RequestHeader("Authorization") String token
    ) {
        List<PriceDto> priceDtos = new ArrayList<>();
        float total_price = 0;
        if (options != null) {
            for (OrderOptionRequest option : options) {
                PriceDto priceDto = PriceDto.builder()
                        .serviceCode(option.getServiceCode())
                        .serviceName(this.getServiceName(option.getServiceCode()))
                        .price(this.getOptionPrice(option.getServiceCode()  ))
                        .build();
                priceDtos.add(priceDto);
                total_price += priceDto.getPrice();
            }
        }
        priceDtos.add(0,
                PriceDto.builder()
                    .serviceCode("ALL")
                    .serviceName("Tổng cộng")
                    .price(total_price)
                    .build()
        );
        return priceDtos;
    }

    private String getServiceName(String serviceCode) {
        if (serviceCode.equals("fragile")) {
            return "Hàng dễ vỡ";
        } else if (serviceCode.equals("bulky")) {
            return "Hàng cồng kềnh";
        } else if (serviceCode.equals("valuable")) {
            return "Hàng quý giá";
        } else if (serviceCode.equals("document")) {
            return "Tài liệu";
        }
        return "";
    }

    private float getOptionPrice(String serviceCode) {
        if (serviceCode.equals("fragile")) {
            return 1000.0f;
        } else if (serviceCode.equals("bulky")) {
            return 2000.0f;
        } else if (serviceCode.equals("valuable")) {
            return 3000.0f;
        } else if (serviceCode.equals("document")) {
            return 4000.0f;
        }
        return 0;

    }

//    create order for shop owner


    // get list order of shop owner
    @GetMapping("/owner")
    @PreAuthorize("hasAnyAuthority('VIEW_ONLY', 'CREATE_ORDER', 'UPDATE_ORDER','MANAGE_ORDER')")
//    @PreAuthorize("hasPermission('VIEW_ONLY', 'CREATE_ORDER', 'UPDATE_ORDER','MANAGE_ORDER')")
    public GenericResponseDTO<List<OrderDto>> getOwnerOrders(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate,desc") String[] sort
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        Pageable pageable = RestParamUtils.getPageable(page, size, sort);
        List<OrderDto> orders = orderService.getOwnerOrders(userId, pageable);
        return GenericResponseDTO.<List<OrderDto>>builder()
                .data(orders)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_OWNER_ORDER)
                .timestamps(new Date())
                .build();
    }

    // create order for owner
    @PostMapping("/owner")
//    @PreAuthorize("#hasPermission('CREATE_ORDER') or #hasPermission('UPDATE_ORDER') or #hasPermission('MANAGE_ORDER')")
    @PreAuthorize("hasAnyAuthority('CREATE_ORDER', 'UPDATE_ORDER','MANAGE_ORDER')")
    public GenericResponseDTO<OrderDto> createOwnerOrder(
            @RequestBody OrderRequest orderRequest,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        orderRequest.setUserId(userId);
        OrderDto newOrderDto = orderService.createOwnerOrder(orderRequest);
        return GenericResponseDTO.<OrderDto>builder()
                .data(newOrderDto)
                .code(MessageCode.CREATED_SUCCESS.toString())
                .message(MessageConstants.SUCCESS_OWNER_ORDER_CREATED)
                .timestamps(new Date())
                .build();
    }

//    update order for owner
    @PutMapping("/{orderId}/owner")
    @PreAuthorize("hasAnyAuthority('UPDATE_ORDER','MANAGE_ORDER')")
     public GenericResponseDTO<OrderDto> updateOwnerOrder(
            @PathVariable Long orderId,
            @RequestBody OrderDto orderDto,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto updatedOrder = orderService.updateOwnerOrder(userId, orderId, orderDto);
        return GenericResponseDTO.<OrderDto>builder()
                .data(updatedOrder)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_OWNER_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
    }

//    update owner order status
    @PatchMapping("/{orderId}/owner/status")
    @PreAuthorize("hasAnyAuthority('UPDATE_ORDER','MANAGE_ORDER')")
    public GenericResponseDTO<OrderDto> updateOwnerOrderStatus(
            @PathVariable Long orderId,
            @RequestBody StatusRequest status,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        OrderDto updatedOrderDto = orderService.updateOwnerOrderStatus(userId, orderId, status.getNewStatus().name());
        return GenericResponseDTO.<OrderDto>builder()
                .data(updatedOrderDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ORDER_UPDATED)
                .timestamps(new Date())
                .build();
    }

    // get top 10 receiver by completed order spending
    @GetMapping("/top-receiver")
    public GenericResponseDTO<List<TopReceiverDto>> getTopReceiver(
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<TopReceiverDto> orders = orderService.getTopReceiver(userId);
        return GenericResponseDTO.<List<TopReceiverDto>>builder()
                .data(orders)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_TOP_RECEIVER)
                .timestamps(new Date())
                .build();
    }

    // get data of total spending, total order by month of a user
    @GetMapping("/statistic")
    public GenericResponseDTO<List<ChartStatisticsDto>> getStatistic(
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<ChartStatisticsDto> orders = orderService.getStatistic(userId);
        return GenericResponseDTO.<List<ChartStatisticsDto>>builder()
                .data(orders)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_STATISTIC)
                .timestamps(new Date())
                .build();
    }




    // phân shipper vào

    // update status order

}
