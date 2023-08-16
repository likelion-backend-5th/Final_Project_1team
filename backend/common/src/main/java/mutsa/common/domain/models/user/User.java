package mutsa.common.domain.models.user;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseTimeEntity;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.embedded.Address;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "user")
public class User extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private final String apiId = UUID.randomUUID().toString();

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Embedded
    private Address address;

//    @OneToOne(mappedBy = "user", fetch = LAZY)
//    private Member member;

    @Singular
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private final Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Article> articles;

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


    //    Member member
    public static User of(String username, String encodedPassword, String email,
                          String oauth2Username, String imageUrl) {

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
//                .oauth2Username(oauth2Username)
//                .imageUrl(imageUrl)
                .build();
//        user.setMember(member);
        return user;
    }
}
