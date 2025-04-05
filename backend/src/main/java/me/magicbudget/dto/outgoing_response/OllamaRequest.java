package me.magicbudget.dto.outgoing_response;

public class OllamaRequest {
  private String model;
  private String prompt;

  public OllamaRequest(String model, String prompt) {
    this.model = model;
    this.prompt = prompt;
  }

  public String getModel() { return model; }
  public String getPrompt() { return prompt; }
}
