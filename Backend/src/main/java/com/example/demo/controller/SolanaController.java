package com.example.demo.controller;

import com.example.demo.entity.CourseEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ShyftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/solana")
public class SolanaController {

    @Autowired
    private ShyftService shyftService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/connect/{walletAddress}")
    public Mono<String> connectWallet(@PathVariable String walletAddress) {
        return shyftService.connectWallet(walletAddress)
                .doOnError(error -> {
                    System.err.println("Error connecting wallet: " + error.getMessage());
                });
    }

    @PostMapping("/purchase")
    public Mono<String> purchaseCourse(@RequestParam Long userId, @RequestParam Long courseId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        CourseEntity course = courseRepository.findById(courseId).orElseThrow();
        System.out.println("User: " + user.getSolana_wallet_address());

        return shyftService.sendSol(user.getSolana_wallet_address(), "ADG6ZtpMGgoxxz2EhwMgHWbZMSzgTm34kNL2uRjh3gP7", course.getPrice())
                .flatMap(transactionId -> {
                    OrderEntity order = new OrderEntity();
                    order.setUser(user);
                    order.setCourse(course);
                    order.setPayment_status("COMPLETED");
                    order.setTransaction_id(transactionId);
                    orderRepository.save(order);

                    return Mono.just("Purchase completed with transaction ID: " + transactionId);
                })
                .doOnError(error -> {
                    System.err.println("Error purchasing course: " + error.getMessage());
                });
    }

    @GetMapping("/balance/{walletAddress}")
    public Mono<Double> getBalance(@PathVariable String walletAddress) {
        return shyftService.getSolBalance(walletAddress)
                .doOnError(error -> {
                    System.err.println("Error fetching balance: " + error.getMessage());
                });
    }

    @GetMapping("/account/{walletAddress}")
    public Mono<String> getAccountInfo(@PathVariable String walletAddress) {
        return shyftService.getAccountInfo(walletAddress)
                .doOnError(error -> {
                    System.err.println("Error fetching account info: " + error.getMessage());
                });
    }
}
