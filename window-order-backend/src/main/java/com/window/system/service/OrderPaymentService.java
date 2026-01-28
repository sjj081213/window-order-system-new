package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.mapper.OrderPaymentMapper;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.entity.OrderPayment;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.PaymentCreateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderPaymentService {

    @Autowired
    private OrderPaymentMapper orderPaymentMapper;
    
    @Autowired
    private WindowOrderMapper windowOrderMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result<String> create(PaymentCreateReq req) {
        WindowOrder order = windowOrderMapper.getById(req.getOrderId());
        if (order == null) {
            return Result.error("Order not found");
        }
        
        OrderPayment payment = new OrderPayment();
        BeanUtils.copyProperties(req, payment);
        payment.setCreateBy(req.getCurrentUserId());
        if (payment.getPayTime() == null) {
            payment.setPayTime(LocalDateTime.now());
        }
        
        orderPaymentMapper.insert(payment);
        
        // Update order payment status
        updateOrderPaymentStatus(order);
        
        return Result.success("Payment recorded successfully");
    }
    
    private void updateOrderPaymentStatus(WindowOrder order) {
        List<OrderPayment> payments = orderPaymentMapper.getByOrderId(order.getId());
        BigDecimal totalPaid = payments.stream()
                .map(OrderPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setPaidAmount(totalPaid);
        
        if (order.getPrice() != null && order.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
                order.setPaymentStatus("UNPAID");
            } else if (totalPaid.compareTo(order.getPrice()) >= 0) {
                order.setPaymentStatus("PAID");
            } else {
                order.setPaymentStatus("PARTIAL");
            }
        } else {
            // Price not set yet
             if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
                 order.setPaymentStatus("PARTIAL");
             } else {
                 order.setPaymentStatus("UNPAID");
             }
        }
        
        windowOrderMapper.update(order);
    }

    public Result<List<OrderPayment>> listByOrderId(Long orderId) {
        return Result.success(orderPaymentMapper.getByOrderId(orderId));
    }
}
