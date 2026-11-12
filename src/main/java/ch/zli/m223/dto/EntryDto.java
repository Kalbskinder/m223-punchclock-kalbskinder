package ch.zli.m223.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import ch.zli.m223.model.Entry;

public class EntryDto {
  @Schema(readOnly = true)
  private Long id;

  @NotNull
  private LocalDateTime checkIn;

  @NotNull
  private LocalDateTime checkOut;

  @Size(max = 255)
  private String description;

  private Long categoryId;

  private List<Long> tagIds = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getCheckIn() {
    return checkIn;
  }

  public void setCheckIn(LocalDateTime checkIn) {
    this.checkIn = checkIn;
  }

  public LocalDateTime getCheckOut() {
    return checkOut;
  }

  public void setCheckOut(LocalDateTime checkOut) {
    this.checkOut = checkOut;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public List<Long> getTagIds() {
    return tagIds;
  }

  public void setTagIds(List<Long> tagIds) {
    this.tagIds = tagIds != null ? tagIds : new ArrayList<>();
  }

  /**
   * Converts only the simple fields. Category and tags are resolved
   * by the service, because they have to be loaded from the database.
   */
  public Entry toEntity() {
    var entry = new Entry();
    entry.setCheckIn(checkIn);
    entry.setCheckOut(checkOut);
    entry.setDescription(description);
    return entry;
  }
}
