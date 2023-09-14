package mutsa.api.config.security;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.domain.models.user.Role;
import mutsa.common.domain.models.user.User;
import mutsa.common.domain.models.user.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Getter
public class CustomPrincipalDetails implements UserDetails, OAuth2User, Serializable {
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

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

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
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    public static CustomPrincipalDetails of(User user, Map<String, Object> attributes) {
        log.info("CustomPrincipalDetails 생성 ");
        return CustomPrincipalDetails.builder()
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
