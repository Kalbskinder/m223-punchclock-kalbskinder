package ch.zli.m223.model;

import jakarta.persistence.*;

import ch.zli.m223.dto.ApplicationUserDto;

@Entity
@Table(name = "application_user") // "user" is a reserved word in PostgreSQL
public class ApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  /** BCrypt hash, never the plain password. */
  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String role;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public ApplicationUserDto toDto() {
    var dto = new ApplicationUserDto();
    dto.setId(id);
    dto.setEmail(email);
    dto.setRole(role);
    return dto;
  }
}
