package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.ERole;
import com.tttm.Whear.App.enums.Language;
import com.tttm.Whear.App.enums.StatusGeneral;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends AuditEntity implements Serializable, UserDetails {

  @Id
  @Column(name = "userID")
  private String userID;

  @Column(name = "username", columnDefinition = "nvarchar(550)")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "dateOfBirth", unique = false, nullable = true)
  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "gender", unique = false, nullable = true)
  private Boolean gender;    // 1 Male 0 Female

  @Column(name = "role", unique = false, nullable = true)
  @Enumerated(EnumType.STRING)
  private ERole role;

  @Column(name = "imgUrl", columnDefinition = "TEXT")
  private String imgUrl;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private StatusGeneral status;

  @Column(name = "language")
  @Enumerated(EnumType.STRING)
  private Language language;

  @ManyToOne
  @JoinColumn(name = "bodyShapeID", referencedColumnName = "bodyShapeID")
  private BodyShape bodyShape;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }
  @Override
  public String getUsername() {
    return email;
  }

  public String getNameOfUser() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
