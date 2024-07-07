package com.example.demo.service;

import com.example.demo.converter.OrderConverter;
import com.example.demo.dto.requestDTO.OrderRequest;
import com.example.demo.entity.OrderEntity;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    @Autowired
    private ShyftService shyftService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderConverter orderConverter;
    public Mono<OrderEntity> processPayment(OrderRequest order) {
        return shyftService.connectWallet(userRepository.findById(order.getUser_id()).get().getSolana_wallet_address())
                .flatMap(transactionId -> {
                    order.setTransaction_id(transactionId);
                    order.setPayment_status("PAID");
                    return Mono.just(orderRepository.save(orderConverter.convertToEntity(order)));
                });
    }
}