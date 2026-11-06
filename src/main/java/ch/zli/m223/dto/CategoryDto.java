package ch.zli.m223.dto;

import jakarta.validation.constraints.NotBlank;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import ch.zli.m223.model.Category;

public class CategoryDto {
  @Schema(readOnly = true)
  private Long id;

  @NotBlank
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

  public Category toEntity() {
    var entity = new Category();
    entity.setTitle(title);
    return entity;
  }
}
