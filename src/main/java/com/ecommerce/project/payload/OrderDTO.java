package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
    private PaymentDTO paymentDTO;
    private LocalDate orderDate;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;
}
