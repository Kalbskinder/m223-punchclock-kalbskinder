package ch.zli.m223.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntryDetailDto {
  private Long id;
  private LocalDateTime checkIn;
  private LocalDateTime checkOut;
  private CategoryDto category;
  private List<TagDto> tags = new ArrayList<>();

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

  public CategoryDto getCategory() {
    return category;
  }

  public void setCategory(CategoryDto category) {
    this.category = category;
  }

  public List<TagDto> getTags() {
    return tags;
  }

  public void setTags(List<TagDto> tags) {
    this.tags = tags;
  }
}
