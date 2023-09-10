package mutsa.api.repository.chatroom;

import jakarta.transaction.Transactional;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.repository.chatroom.ChatroomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@Transactional
class ChatroomRepositoryTest {
    @Autowired
    private  ChatroomRepository chatroomRepository;
    @MockBean
    private ChatroomRepository mockChatroomRepository;

    @Test
    void findByApiId() {
        //given
        String testApiId = "testApiID";
        Chatroom chatroom = Chatroom.of(testApiId);

        // 모킹 설정
        when(mockChatroomRepository.findByApiId(testApiId)).thenReturn(Optional.of(chatroom));

        //when
        Optional<Chatroom> byApiId = chatroomRepository.findByApiId(testApiId);

        //then
        assertThat(byApiId.isPresent()).isTrue();
        assertThat(byApiId.get().getArticleApiId()).isEqualTo(testApiId);
    }
}