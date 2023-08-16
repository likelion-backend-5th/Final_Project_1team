package mutsa.api.config.security;

import lombok.*;
import mutsa.common.domain.models.user.Role;
import mutsa.common.domain.models.user.User;
import mutsa.common.domain.models.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CustomUserDetails implements UserDetails, Serializable {
    private String apiId;
    private String username;

    private String password;

    @Builder.Default
    private Boolean accountNonExpired = true;

    @Builder.Default
    private Boolean accountNonLocked = true;

    @Builder.Default
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    private Boolean enabled = true;

    //transient 해야 serialize되지 않음.
    //Fields in a "Serializable" class should either be transient or serializable
    //java:S1948
    @Builder.Default
    private transient Map<String, Object> attributes = new HashMap<>();

    @Builder.Default
    private Set<SimpleGrantedAuthority> authorities = new HashSet<>();

    @Override
    public String getUsername() {
        return this.username;
    }

//    @Override
//    public Map<String, Object> getAttributes() {
//        return this.attributes;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    public static CustomUserDetails of(User user, Map<String, Object> attributes) {
        return CustomUserDetails.builder()
                .apiId(user.getApiId())
                .username(user.getUsername())
                .password(user.getPassword())
                .attributes(attributes)
                .authorities(user.getUserRoles().stream()
                        .map((UserRole::getRole))
                        .map(Role::getAuthorities)
                        .flatMap(Set::stream)
                        .map(authority ->
                                new SimpleGrantedAuthority(authority.getName()))
                        .collect(Collectors.toSet())
                )
                .build();
    }
}
