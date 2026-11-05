package ch.zli.m223.model;

import jakarta.persistence.*;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import ch.zli.m223.dto.EntryDto;

import java.time.LocalDateTime;

@Entity
public class Entry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(readOnly = true)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime checkIn;

  @Column(nullable = false)
  private LocalDateTime checkOut;

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

  public EntryDto toDto() {
    var dto = new EntryDto();
    dto.setId(this.id);
    dto.setCheckIn(checkIn);
    dto.setCheckOut(checkOut);
    return dto;
  }
}