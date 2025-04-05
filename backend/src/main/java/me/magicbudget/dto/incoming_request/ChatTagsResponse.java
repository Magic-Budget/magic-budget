package me.magicbudget.dto.incoming_request;

import java.util.List;

public class ChatTagsResponse {
  private List<OllamaModel> models;

  public List<OllamaModel> getModels() {
    return models;
  }

  public void setModels(List<OllamaModel> models) {
    this.models = models;
  }

  public static class OllamaModel {
    private String name;
    private String size;
    private String modified_at;

    public String getName() { return name; }
    public String getSize() { return size; }
    public String getModified_at() { return modified_at; }

    public void setName(String name) { this.name = name; }
    public void setSize(String size) { this.size = size; }
    public void setModified_at(String modified_at) { this.modified_at = modified_at; }
  }
}

