package me.magicbudget.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "businesses")
public class Business {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  // @JsonProperty("id")
  private UUID id;

  @NotNull
  @Size(max = 50)
  @Column(unique = true)
  // @JsonProperty("name")
  private String name;



  public Business() {
  }

  public Business(UUID id, @NotNull @Size(max = 50) String name) {
    this.id = id;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @NotNull @Size(max = 50) String getName() {
    return name;
  }

  public void setName(@NotNull @Size(max = 50) String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Business{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
