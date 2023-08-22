/**
 * @project backend
 * @author ARA
 * @since 2023-08-18 AM 11:40
 */

package mutsa.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.api.service.article.ArticleService;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.article.ArticleStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    private ObjectMapper objectMapper = new ObjectMapper();

    private User user1, user2;
    private List<Article> articles;

    @BeforeAll
    public static void beforeAll() {
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    public void init() {
        user1 = User.of(
                "ArticleControllerTestUser1",
                passwordEncoder.encode("test"),
                "articlecontrollertestuser1@gmail.com",
                null,
                null,
                null
        );
        user1 = userRepository.save(user1);

        user2 = User.of(
                "ArticleControllerTestUser2",
                passwordEncoder.encode("test"),
                "articlecontrollertestuser2@gmail.com",
                null,
                null,
                null
        );
        user2 = userRepository.save(user2);

        articles = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Article article = Article.builder()
                    .title("title-" + (i + 1))
                    .description("desc-" + (i + 1))
                    .user(i % 2 == 0 ? user1 : user2)
                    .build();

            articles.add(article);
        }

        articles = articleRepository.saveAll(articles);

        log.info("beforeEach Created()");
    }

    @Test
    @DisplayName("게시글 단일 조회")
    public void getArticle() throws Exception {
        Article entity = Article.builder()
                .user(user1)
                .title("test")
                .description("test")
                .build();

        entity = articleRepository.save(entity);

        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        mockMvc.perform(get("/api/articles/{articleApiId}", entity.getApiId())
                                .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 단일 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("apiId").value(entity.getApiId()),
                        jsonPath("title").value(entity.getTitle()),
                        jsonPath("description").value(entity.getDescription()),
                        jsonPath("username").value(entity.getUser().getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @DisplayName("게시글 목록 조회")
    public void getPage() throws Exception {
        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        MvcResult mvcResult = mockMvc.perform(get("/api/articles")
                                                      .param("page", String.valueOf(0))
                                                      .param("size", String.valueOf(10))
                                                      .param("order", "ASC")
                                                      .param("articleStatus", ArticleStatus.LIVE.toString())
                                                      .param("status", Status.ACTIVE.toString())
                                                      .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 페이지 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("size", equalTo(10)),
                        jsonPath("number", equalTo(0)),
                        jsonPath("numberOfElements", equalTo(10))
                )
                .andReturn();
    }

    @Test
    @DisplayName("게시글 제목 조회")
    public void getPageByTitle() throws Exception {
        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        MvcResult mvcResult = mockMvc.perform(get("/api/articles")
                                                      .param("page", String.valueOf(0))
                                                      .param("size", String.valueOf(10))
                                                      .param("order", "ASC")
                                                      .param("articleStatus", ArticleStatus.LIVE.toString())
                                                      .param("status", Status.ACTIVE.toString())
                                                      .param("title", "title-1")
                                                      .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 제목 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))
                .andExpectAll(
                        status().isOk(),
                        status().is2xxSuccessful(),
                        jsonPath("$['content'][0]['apiId']").value(articles.get(0).getApiId()),
                        jsonPath("$['content'][0]['title']").value(articles.get(0).getTitle()),
                        jsonPath("$['content'][0]['description']").value(articles.get(0).getDescription()),
                        jsonPath("$['content'][0]['username']").value(articles.get(0).getUser().getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
    }

    @Test
    @DisplayName("게시글 내용 조회")
    public void getPageByDescription() throws Exception {
        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        MvcResult mvcResult = mockMvc.perform(get("/api/articles")
                                                      .param("page", String.valueOf(0))
                                                      .param("size", String.valueOf(10))
                                                      .param("order", "ASC")
                                                      .param("articleStatus", ArticleStatus.LIVE.toString())
                                                      .param("status", Status.ACTIVE.toString())
                                                      .param("description", "desc-1")
                                                      .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 내용 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))
                .andExpectAll(
                        status().isOk(),
                        status().is2xxSuccessful(),
                        jsonPath("$['content'][0]['apiId']").value(articles.get(0).getApiId()),
                        jsonPath("$['content'][0]['title']").value(articles.get(0).getTitle()),
                        jsonPath("$['content'][0]['description']").value(articles.get(0).getDescription()),
                        jsonPath("$['content'][0]['username']").value(articles.get(0).getUser().getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
    }

    @Test
    @DisplayName("게시글 유저이름 조회")
    public void getPageByUsername() throws Exception {
        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        MvcResult mvcResult = mockMvc.perform(get("/api/articles")
                                                      .param("page", String.valueOf(0))
                                                      .param("size", String.valueOf(10))
                                                      .param("order", "ASC")
                                                      .param("articleStatus", ArticleStatus.LIVE.toString())
                                                      .param("status", Status.ACTIVE.toString())
                                                      .param("username", "ArticleControllerTestUser1")
                                                      .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 유저이름 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))
                .andExpectAll(
                        status().isOk(),
                        status().is2xxSuccessful(),
                        jsonPath("size", equalTo(10)),
                        jsonPath("number", equalTo(0)),
                        jsonPath("numberOfElements", equalTo(5)),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        String content = jsonNode.get("content").toString();

        ArticleResponseDto[] responseDtos = objectMapper.readValue(
                content,
                ArticleResponseDto[].class
        );

        for (ArticleResponseDto responseDto : responseDtos) {
            Assertions.assertEquals(user1.getUsername(), responseDto.getUsername());
        }
    }

    @Test
    @DisplayName("게시글 등록")
    public void createArticle() throws Exception {
        ArticleCreateRequestDto articleCreateRequestDto = new ArticleCreateRequestDto();
        articleCreateRequestDto.setTitle("test Article");
        articleCreateRequestDto.setDescription("test Desc");
        articleCreateRequestDto.setUsername(user1.getUsername());

        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        MvcResult mvcResult = mockMvc.perform(post("/api/articles")
                                                      .content(objectMapper.writeValueAsString(
                                                              articleCreateRequestDto
                                                      ))
                                                      .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 등록",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("title").value(articleCreateRequestDto.getTitle()),
                        jsonPath("description").value(articleCreateRequestDto.getDescription()),
                        jsonPath("username").value(articleCreateRequestDto.getUsername())
                )
                .andReturn();
    }

    @Test
    @DisplayName("게시글 수정")
    public void updateArticle() throws Exception {
        ArticleUpdateRequestDto articleUpdateRequestDto = new ArticleUpdateRequestDto();
        articleUpdateRequestDto.setTitle("test Article");
        articleUpdateRequestDto.setDescription("test Desc");
        articleUpdateRequestDto.setUsername(user1.getUsername());
        articleUpdateRequestDto.setApiId(articles.get(0).getApiId());

        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        MvcResult mvcResult = mockMvc.perform(put("/api/articles")
                                                      .content(objectMapper.writeValueAsString(
                                                              articleUpdateRequestDto
                                                      ))
                                                      .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 수정",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("title").value(articleUpdateRequestDto.getTitle()),
                        jsonPath("description").value(articleUpdateRequestDto.getDescription()),
                        jsonPath("username").value(articleUpdateRequestDto.getUsername())
                )
                .andReturn();
    }

    @Test
    @DisplayName("게시글 삭제")
    public void deleteArticle() throws Exception {
        when(SecurityUtil.getCurrentUsername()).thenReturn(user1.getUsername());

        mockMvc.perform(delete("/api/articles/{articleApiId}", articles.get(0).getApiId())
                                .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "api/articles/게시글 삭제",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ))

                .andExpectAll(
                        status().is2xxSuccessful()
                );

        entityManager.flush();
        entityManager.clear();

        Article deletedArticle = articleRepository.findByApiId(articles.get(0).getApiId()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 게시글")
        );
        Assertions.assertEquals(Status.DELETED, deletedArticle.getStatus());
        Assertions.assertEquals(ArticleStatus.EXPIRED, deletedArticle.getArticleStatus());
    }
}
