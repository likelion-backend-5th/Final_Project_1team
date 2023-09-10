package mutsa.api.service.chatroom;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.dto.chat.ChatRoomDetailDto;
import mutsa.api.dto.chat.ChatroomRequestDto;
import mutsa.api.dto.chat.ChatroomResponseDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
@Slf4j
class ChatroomServiceTest {
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;
    private User seller, seller2, consumer;
    private Article article, article2, article3;

    @BeforeEach
    public void init() {
        seller = User.of("user1", "password", "email1@", "oauthName1", null, null);
        seller2 = User.of("seller2", "password", "seller2@", "seller2", null, null);
        consumer = User.of("user2", "password", "email2@", "oauthName2", null, null);
        seller = userRepository.save(seller);
        seller2 = userRepository.save(seller2);
        consumer = userRepository.save(consumer);

        article = articleRepository.save(
                Article.builder()
                        .title("Pre Article 1")
                        .description("Pre Article 1 desc")
                        .user(seller)
                        .price(12900L)
                        .build()
        );

        article2 = articleRepository.save(
                Article.builder()
                        .title("Pre Article 2")
                        .description("Pre Article 2 desc")
                        .user(seller)
                        .build()
        );

        article3 = articleRepository.save(
                Article.builder()
                        .title("Pre Article 3")
                        .description("Pre Article 3 desc")
                        .user(seller2)
                        .build()
        );
    }

    @AfterEach
    public void tearDown() {
        // Redis 데이터 삭제
        userRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("단순 생성 테스트")
    void createChatRoom() {
        //given
        ChatroomRequestDto dto = new ChatroomRequestDto(article.getApiId());

        //when
        ChatRoomDetailDto chatRoom = chatroomService.createChatRoom(dto, consumer.getUsername());

        //then
        assertThat(chatRoom.getRoomName()).isEqualTo(seller.getUsername());
        assertThat(chatRoom.getArticleTitle()).isEqualTo(article.getTitle());
    }


    @Test
    @DisplayName("이미 채팅방이 있는 경우(다른 게시글)")
    void createChatRoom2() {
        //given
        ChatroomRequestDto dto = new ChatroomRequestDto(article.getApiId());
        chatroomService.createChatRoom(dto, consumer.getUsername());

        //when
        ChatroomRequestDto modify = new ChatroomRequestDto(article2.getApiId());
        ChatRoomDetailDto chatRoom = chatroomService.createChatRoom(modify, consumer.getUsername());

        //then
        assertThat(chatRoom.getRoomName()).isEqualTo(seller.getUsername());
        assertThat(chatRoom.getArticleTitle()).isEqualTo(article2.getTitle());
    }

    @Test
    @DisplayName("이미 채팅방이 있는 경우(같은 게시글)")
    void createChatRoom2_1() {
        //given
        ChatroomRequestDto dto = new ChatroomRequestDto(article.getApiId());
        chatroomService.createChatRoom(dto, consumer.getUsername());

        //when
        ChatroomRequestDto modify = new ChatroomRequestDto(article.getApiId());
        ChatRoomDetailDto chatRoom = chatroomService.createChatRoom(modify, consumer.getUsername());

        //then
        assertThat(chatRoom.getRoomName()).isEqualTo(seller.getUsername());
        assertThat(chatRoom.getArticleTitle()).isEqualTo(article.getTitle());
    }

    @Test
    @DisplayName("자신의 게시글 채팅방을 생성하려고 하는 경우")
    void createChatRoom3() {
        //given
        ChatroomRequestDto dto = new ChatroomRequestDto(article.getApiId());

        //when, then
        Assertions.assertThatThrownBy(() -> chatroomService.createChatRoom(dto, seller.getUsername()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_ROOM_REQUEST.getMessage());
    }

    @Test
    void findMyChatrooms() {
        //given
        ChatroomRequestDto dto1 = new ChatroomRequestDto(article.getApiId());
        ChatRoomDetailDto chatRoom1 = chatroomService.createChatRoom(dto1, consumer.getUsername());
        ChatroomRequestDto dto2 = new ChatroomRequestDto(article3.getApiId());
        ChatRoomDetailDto chatRoom2 = chatroomService.createChatRoom(dto2, consumer.getUsername());

        //when
        List<ChatroomResponseDto> myChatroom = chatroomService.findMyChatroom(consumer.getUsername());

        //then
        assertThat(myChatroom.size()).isEqualTo(2);
        assertThat(myChatroom.get(0).getChatroomApiId()).isEqualTo(chatRoom1.getChatroomApiId());
        assertThat(myChatroom.get(1).getChatroomApiId()).isEqualTo(chatRoom2.getChatroomApiId());
    }

    @Test
    void findChatroom() {
        //given
        ChatroomRequestDto dto = new ChatroomRequestDto(article.getApiId());
        ChatRoomDetailDto chatRoom1 = chatroomService.createChatRoom(dto, consumer.getUsername());

        //when
        ChatRoomDetailDto chatroom = chatroomService.findChatroom(chatRoom1.getChatroomApiId(), consumer.getUsername());

        //then
        assertThat(chatroom.getArticleTitle()).isEqualTo(article.getTitle());
        assertThat(chatroom.getRoomName()).isEqualTo(article.getUser().getUsername());
    }

    @Test
    @DisplayName("권한이 부족한 유저가 조회한 경우")
    void findChatroom2() {
        //given
        ChatroomRequestDto dto = new ChatroomRequestDto(article.getApiId());
        ChatRoomDetailDto chatRoom1 = chatroomService.createChatRoom(dto, consumer.getUsername());

        //when,then
        Assertions.assertThatThrownBy(()
                        -> chatroomService.findChatroom(chatRoom1.getChatroomApiId(), seller2.getUsername()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.CHATROOM_PERMISSION_DENIED.getMessage());
    }

    @Test
    public void getByApiId() {
        //when,then
        Assertions.assertThatThrownBy(() -> chatroomService.getByApiId(UUID.randomUUID().toString()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());

    }

    @Test
    public void getById() {
        //when,then
        Assertions.assertThatThrownBy(() -> chatroomService.getById(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());

    }

}