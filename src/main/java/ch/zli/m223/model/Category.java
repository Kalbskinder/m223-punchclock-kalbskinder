package ch.zli.m223.model;

import jakarta.persistence.*;

import ch.zli.m223.dto.CategoryDto;

@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public CategoryDto toDto() {
    var dto = new CategoryDto();
    dto.setId(id);
    dto.setTitle(title);
    return dto;
  }
}
