package ch.zli.m223.model;

import jakarta.persistence.*;

import ch.zli.m223.dto.EntryDetailDto;
import ch.zli.m223.dto.EntryDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Entry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime checkIn;

  @Column(nullable = false)
  private LocalDateTime checkOut;

  @Column 
  private String description;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToMany
  @JoinTable(
      name = "entry_tag",
      joinColumns = @JoinColumn(name = "entry_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

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

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  public void setDescription(String desc) {
    this.description = desc;
  }

  public String getDescription() {
    return this.description;
  }

  public EntryDto toDto() {
    var dto = new EntryDto();
    dto.setId(id);
    dto.setCheckIn(checkIn);
    dto.setCheckOut(checkOut);
    dto.setDescription(description);
    dto.setCategoryId(category != null ? category.getId() : null);
    dto.setTagIds(tags.stream().map(Tag::getId).sorted().toList());
    return dto;
  }

  public EntryDetailDto toDetailDto() {
    var dto = new EntryDetailDto();
    dto.setId(id);
    dto.setCheckIn(checkIn);
    dto.setCheckOut(checkOut);
    dto.setCategory(category != null ? category.toDto() : null);
    dto.setTags(tags.stream()
        .sorted((a, b) -> a.getId().compareTo(b.getId()))
        .map(Tag::toDto)
        .toList());
    return dto;
  }
}
