package mutsa.api.controller.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.dto.chat.ChatRoomDetailDto;
import mutsa.api.dto.chat.ChatroomRequestDto;
import mutsa.api.service.chatroom.ChatroomService;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.chatroom.ChatroomRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Slf4j
class ChatroomControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;

    @BeforeAll
    public static void beforeAll() {
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void afterAll() {
        securityUtilMockedStatic.close();
    }

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
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    @DisplayName("채팅방 생성")
    void createRoom() throws Exception {
        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());
        ChatroomRequestDto dto = new ChatroomRequestDto(article.getApiId());
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        //when, then
        mockMvc.perform(post("/api/chat/room")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/chatroom/채팅방 생성",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("articleTitle").value(article.getTitle()),
                        jsonPath("articleUsername").value(seller.getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        assertThat(chatroomRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("내가속한 채팅방 반환")
    void getRoom() throws Exception {
        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());
        ChatroomRequestDto dto1 = new ChatroomRequestDto(article.getApiId());
        ChatRoomDetailDto chatRoom1 = chatroomService.createChatRoom(dto1, consumer.getUsername());
        ChatroomRequestDto dto2 = new ChatroomRequestDto(article3.getApiId());
        ChatRoomDetailDto chatRoom2 = chatroomService.createChatRoom(dto2, consumer.getUsername());

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/chat/room")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/chatroom/나의 모든 채팅방 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
        //then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].chatroomApiId").value(chatRoom1.getChatroomApiId()))
                .andExpect(jsonPath("$[0].roomName").value(chatRoom1.getRoomName()))
                .andExpect(jsonPath("$[1].chatroomApiId").value(chatRoom2.getChatroomApiId()))
                .andExpect(jsonPath("$[1].roomName").value(chatRoom2.getRoomName()))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("한개의 방 정보 조회")
    void getOneRoom() throws Exception {
        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());
        ChatroomRequestDto dto1 = new ChatroomRequestDto(article.getApiId());
        ChatRoomDetailDto chatroom = chatroomService.createChatRoom(dto1, consumer.getUsername());

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/chat/room/{chatroomId}", chatroom.getChatroomApiId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/chatroom/한개의 채팅방 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
        //then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("chatroomApiId").value(chatroom.getChatroomApiId()))
                .andExpect(jsonPath("roomName").value(chatroom.getRoomName()))
                .andExpect(jsonPath("articleTitle").value(chatroom.getArticleTitle()))
                .andExpect(jsonPath("articleDescription").value(chatroom.getArticleDescription()))
                .andExpect(jsonPath("articleUsername").value(chatroom.getArticleUsername()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}