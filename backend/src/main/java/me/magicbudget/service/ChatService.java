package me.magicbudget.service;

import me.magicbudget.dto.incoming_request.ChatTagsResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Comparator;

@Service
public class ChatService {

  private final WebClient webClient;

  public ChatService(WebClient.Builder builder) {
    this.webClient = builder.baseUrl("http://localhost:7869").build();
  }

  public Mono<String> getLatestModelName() {
    return webClient.get()
        .uri("/api/tags")
        .retrieve()
        .bodyToMono(ChatTagsResponse.class)
        .map(response -> response.getModels().stream()
            .max(Comparator.comparing(model ->
                ZonedDateTime.parse(model.getModified_at())
            ))
            .map(ChatTagsResponse.OllamaModel::getName)
            .orElse("default-model-name") // fallback if no models found
        );
  }
}

