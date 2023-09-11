package mutsa.common.domain.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseTimeEntity;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.user.embedded.Address;
import mutsa.common.domain.models.user.embedded.OAuth2Type;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    @Builder.Default
    private final String apiId = UUID.randomUUID().toString();

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private String imageUrl;

    /* OAuth2 */
    @Column(nullable = false, length = 30)
    private String oauth2Username;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private OAuth2Type oAuth2Type;

    @Builder.Default
    @Column(nullable = false, length = 2)
    private Boolean isOAuth2 = true;

    @Builder.Default
    @Column(nullable = false, length = 2)
    private Boolean isAvailable = true;

    /* mapping table  */
    @OneToOne(mappedBy = "user", fetch = LAZY)
    @JsonIgnore
    private Member member;

    @Singular
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private final Set<UserRole> userRoles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    @JsonIgnore
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    public void updatePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public boolean hasRole(RoleStatus roleStatus) {
        return this.getUserRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getValue)
                .collect(Collectors.toSet())
                .contains(roleStatus);
    }

    public void addMember(Member member) {
        if (member != null) {
            this.member = member;
            member.addUser(this);
        }
    }

    public void addAddress(Address address) {
        this.address = address;
    }

    public static User of(String username, String encodedPassword, String email,
                          String oauth2Username, String imageUrl, Member member) {

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .oauth2Username(oauth2Username == null ? "" : oauth2Username)
                .imageUrl(StringUtils.hasText(imageUrl) ? imageUrl : "")
                .build();
        user.addMember(member);
        return user;
    }

    public static User of(String username, String encodedPassword, String email,
                          String oauth2Username, OAuth2Type oauthType, String imageUrl, Member member) {

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .oauth2Username(oauth2Username == null ? "" : oauth2Username)
                .oAuth2Type(oauthType)
                .imageUrl(StringUtils.hasText(imageUrl) ? imageUrl : "")
                .build();
        user.addMember(member);
        return user;
    }

}
