package ch.zli.m223.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import ch.zli.m223.model.Entry;

public class EntryDto {
  @Schema(readOnly = true)
  private Long id;

  @NotNull
  private LocalDateTime checkIn;

  @NotNull
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

  public Entry toEntity() {
    var entry = new Entry();
    entry.setCheckIn(checkIn);
    entry.setCheckOut(checkOut);
    return entry;
  }
}
