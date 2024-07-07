package com.example.demo.service;

import com.example.demo.dto.responsDTO.TokenBalanceResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ShyftService {

    private final WebClient webClient;

    public ShyftService(WebClient.Builder webClientBuilder, @Value("${shyft.api.base-url}") String baseUrl, @Value("${shyft.api.key}") String apiKey) {
        System.out.println("Base URL: " + baseUrl); // Log base URL
        System.out.println("API Key: " + apiKey); // Log API Key

        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .defaultHeader("x-api-key", apiKey)
                .build();
    }

    public Mono<String> connectWallet(String walletAddress) {
        return webClient.get()
                .uri("/sol/v1/wallet/all_tokens?network=devnet&wallet={address}", walletAddress)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    System.err.println("Error fetching wallet portfolio: " + error.getMessage());
                });
    }

    public Mono<String> createPaymentTransaction(String fromWallet, String toWallet, double amount) {
        return webClient.post()
                .uri("/wallet/send_token")
                .bodyValue(Map.of(
                        "network", "devnet",
                        "from_address", fromWallet,
                        "to_address", toWallet,
                        "amount", amount,
                        "token_address", "So11111111111111111111111111111111111111112"
                ))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> checkTransactionStatus(String transactionSignature) {
        return webClient.get()
                .uri("/transaction/{transaction_signature}", transactionSignature)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> sendSol(String fromAddress, String toAddress, double amount) {
        return webClient.post()
                .uri("/sol/v1/wallet/send_sol")
                .bodyValue(new SendSolRequest(fromAddress, toAddress, amount))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    if (error instanceof WebClientResponseException) {
                        WebClientResponseException webClientError = (WebClientResponseException) error;
                        System.err.println("Error sending SOL: " + webClientError.getStatusCode() + " - " + webClientError.getResponseBodyAsString());
                    } else {
                        System.err.println("Error sending SOL: " + error.getMessage());
                    }
                });
    }

    public Mono<Double> getTokenBalance(String walletAddress, String tokenAddress) {
        return webClient.get()
                .uri("/sol/v1/wallet/token_balance?network=devnet&wallet={wallet}&token={token}", walletAddress, tokenAddress)
                .retrieve()
                .bodyToMono(TokenBalanceResponse.class)
                .map(TokenBalanceResponse::getResult)
                .map(TokenBalanceResponse.Result::getBalance)
                .doOnError(error -> {
                    System.err.println("Error fetching token balance: " + error.getMessage());
                });
    }

    public Mono<Double> getSolBalance(String walletAddress) {
        return webClient.get()
                .uri("/sol/v1/wallet/balance?network=devnet&wallet={wallet}", walletAddress)
                .retrieve()
                .bodyToMono(TokenBalanceResponse.class)
                .doOnNext(response -> {
                    System.out.println("Raw balance response: " + response);
                })
                .map(TokenBalanceResponse::getResult)
                .map(TokenBalanceResponse.Result::getBalance)
                .doOnError(error -> {
                    System.err.println("Error fetching SOL balance: " + error.getMessage());
                });
    }

    public Mono<String> getAccountInfo(String walletAddress) {
        return webClient.get()
                .uri("/sol/v1/wallet/account?network=devnet&wallet={wallet}", walletAddress)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    System.err.println("Error fetching account info: " + error.getMessage());
                });
    }

    @Getter
    @Setter
    private static class SendSolRequest {
        private final String network = "devnet";
        private final String from_address;
        private final String to_address;
        private final double amount;

        public SendSolRequest(String from_address, String to_address, double amount) {
            this.from_address = from_address;
            this.to_address = to_address;
            this.amount = amount;
        }
    }
}
