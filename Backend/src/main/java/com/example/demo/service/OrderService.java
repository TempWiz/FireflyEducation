package com.example.demo.service;

import com.example.demo.converter.OrderConverter;
import com.example.demo.dto.requestDTO.OrderRequest;
import com.example.demo.entity.OrderEntity;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private ShyftService shyftService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private OrderConverter orderConverter;

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderEntity getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public OrderEntity saveOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    public Mono<OrderEntity> processCoursePayment(OrderRequest  order) {
        return shyftService.createPaymentTransaction(
                userRepository.findById(order.getUser_id()).get().getSolana_wallet_address(), // Địa chỉ ví của người dùng
                        "ADG6ZtpMGgoxxz2EhwMgHWbZMSzgTm34kNL2uRjh3gP7", // Địa chỉ ví nhận tiền của bạn
                        courseRepository.findById(order.getCourse_id()).get().getPrice() // Giá của khóa học
                )
                .flatMap(transactionSignature -> {
                    order.setTransaction_id(transactionSignature);
                    order.setPayment_status("PENDING");
                    OrderEntity savedOrder = orderRepository.save(orderConverter.convertToEntity(order));
                    return checkPaymentStatus(savedOrder);
                });
    }

    private Mono<OrderEntity> checkPaymentStatus(OrderEntity order) {
        return shyftService.checkTransactionStatus(order.getTransaction_id())
                .map(status -> {
                    if ("confirmed".equals(status)) {
                        order.setPayment_status("COMPLETED");
                    } else if ("failed".equals(status)) {
                        order.setPayment_status("FAILED");
                    }
                    return orderRepository.save(order);
                });
    }

}