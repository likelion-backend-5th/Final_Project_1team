package mutsa.api.repository.chatroom;

import jakarta.transaction.Transactional;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.chatroomUser.ChatroomUser;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.chatroom.ChatroomUserResult;
import mutsa.common.repository.chatroom.ChatroomRepository;
import mutsa.common.repository.chatroomUser.ChatroomUserRepository;
import mutsa.common.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
class ChatroomUserRepositoryTest {
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private ChatroomUserRepository chatroomUserRepository;
    @Autowired
    private UserRepository userRepository;
    private User user1, user2;
    private Chatroom chatroom1, chatroom2;

    @BeforeEach
    public void init() {
        user1 = User.of("user1", "password", "email1@", "oauthName1", null, null);
        user2 = User.of("seller2", "password", "seller2@", "seller2", null, null);
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        chatroom1 = Chatroom.of("chatroom1");
        chatroom2 = Chatroom.of("chatroom2");
        chatroomRepository.save(chatroom1);
        chatroomRepository.save(chatroom2);
    }

    @Test
    public void testFindByChatroomWithUsers() {
        //given
        ChatroomUser chatroomUser1 = ChatroomUser.of(user1, chatroom1);
        ChatroomUser chatroomUser2 = ChatroomUser.of(user2, chatroom1);
        chatroomUserRepository.save(chatroomUser1);
        chatroomUserRepository.save(chatroomUser2);

        //when
        Optional<Chatroom> result = chatroomUserRepository.findByChatroomWithUsers(user1, user2);

        //then
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().getArticleApiId()).isEqualTo("chatroom1");
    }

    @Test
    void findByUser() {
        //given
        ChatroomUser chatroomUser1 = ChatroomUser.of(user1, chatroom1);
        ChatroomUser chatroomUser2 = ChatroomUser.of(user1, chatroom2);
        ChatroomUser chatroomUser3 = ChatroomUser.of(user2, chatroom1);
        ChatroomUser chatroomUser4 = ChatroomUser.of(user2, chatroom2);
        chatroomUserRepository.save(chatroomUser1);
        chatroomUserRepository.save(chatroomUser2);
        chatroomUserRepository.save(chatroomUser3);
        chatroomUserRepository.save(chatroomUser4);

        //when
        List<ChatroomUserResult> result = chatroomUserRepository.findByUser(user1);

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}