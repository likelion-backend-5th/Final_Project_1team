package mutsa.common.domain.models.chatroom;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.chatroomUser.ChatroomUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
@Getter
@Table(name = "chatroom")
public class Chatroom extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String apiId = UUID.randomUUID().toString();
    private String articleApiId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "chatroom")
    @Builder.Default
    private List<ChatroomUser> users = new ArrayList<>();

    public static Chatroom of(String articleApiId) {
        return Chatroom.builder()
                .articleApiId(articleApiId)
                .build();
    }

    public void setArticleApiId(String articleApiId) {
        this.articleApiId = articleApiId;
    }

    public void setUsers(List<ChatroomUser> users) {
        this.users = users;
    }
}