package me.magicbudget.controller;

import me.magicbudget.dto.incoming_request.ChatRequest;
import me.magicbudget.dto.outgoing_response.OllamaRequest;
import me.magicbudget.service.ChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

  private final WebClient webClient;

  private final ChatService chatService;

  public ChatController(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:7869").build();
    chatService = new ChatService(webClientBuilder);
  }

  @PostMapping
  public Mono<String> chatWithAssistant(@RequestBody ChatRequest request) {
    return chatService.getLatestModelName()
        .flatMap(latestModelName -> {
          OllamaRequest ollamaRequest = new OllamaRequest(latestModelName, request.getMessage());

          return webClient.post()
              .uri("/api/generate")
              .bodyValue(ollamaRequest)
              .retrieve()
              .bodyToMono(String.class); // <== this is the real return
        });
  }

}
