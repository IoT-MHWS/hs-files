package artgallery.cms.configuration;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

public class ServerUserDetails implements UserDetails {

  @Getter
  private final Long id;

  private final String username;

  private final Collection<? extends GrantedAuthority> authorities;

  public ServerUserDetails(Long userId, String username, Collection<? extends GrantedAuthority> authorities) {
    this.id = userId;
    this.username = username;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return "undefined";
  }

  @Override
  public String getUsername() {
    return username;
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
