package mutsa.api.service.chatroom;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.chat.ChatroomRequestDto;
import mutsa.api.dto.chat.ChatroomResponseDto;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.chatroom.ChatroomUser;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.chatroom.ChatroomUserResult;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.chatroom.ChatroomRepository;
import mutsa.common.repository.chatroom.ChatroomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mutsa.common.exception.ErrorCode.ORDER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;
    private final ChatroomUserRepository chatroomUserRepository;
    private final ArticleModuleService articleModuleService;
    private final UserModuleService userModuleService;

    /**
     *
     * @param dto
     * @param username
     * @return 이미 채팅방이 있는 경우는 기존의 방을 리턴, 아닌경우 새로운 방을 리턴한다
     */
    @Transactional
    public ChatroomResponseDto createChatRoom(ChatroomRequestDto dto, String username) {
        User suggester = userModuleService.getByUsername(username);//제안하는 사람
        Article article = articleModuleService.getByApiId(dto.getArticleApiId());
        User seller = article.getUser();//지금 사려고 하는 아이템의 판매자

        Optional<Chatroom> byChatroomWithUsers = chatroomUserRepository.findByChatroomWithUsers(seller, suggester);
        if (!byChatroomWithUsers.isEmpty()) { //이미 해당 유저와의 채팅방이 존재한다.
            return ChatroomResponseDto.fromEntity(byChatroomWithUsers.get(), seller.getUsername());
        }
        if (seller.getId().equals(suggester.getId())) {
            throw new BusinessException(ErrorCode.INVALID_ROOM_REQUEST);
        }

        //채팅방 생성
        Chatroom chatroom = chatroomRepository.save(new Chatroom());

        //유저들과 연결(매핑 테이블 생성)
        chatroomUserRepository.save(ChatroomUser.of(suggester, chatroom));
        chatroomUserRepository.save(ChatroomUser.of(seller, chatroom));

        return ChatroomResponseDto.fromEntity(chatroom, seller.getUsername());
    }

    /**
     *
     * @param username
     * @return 내가 속한 채팅방을 반환(상대방의 이름으로 된 채팅방)
     */
    public List<ChatroomResponseDto> findMyChatroom(String username) {
        User user = userModuleService.getByUsername(username);//현재 내가 누구인가

        //chat room 중에 내가 속한 방이 있다면 모두 보여준다.
        List<ChatroomUserResult> chatrooms = chatroomUserRepository.findByUser(user);

        return chatrooms.stream()
                .map(o -> new ChatroomResponseDto(o.getChatroom(), o.getUser()))
                .collect(Collectors.toList());
    }


    /**
     *
     * @param chatroomApiId
     * @param currentUsername
     * @return 채팅방을 찾고, 상대방의 이름으로 방 이름을 저장하여 반환한다.
     * HELP : 채팅방 이름을 찾는 경우 이렇게 찾는게 맞을지 고민</p>
     */
    public ChatroomResponseDto findChatroom(String chatroomApiId, String currentUsername) {
        Chatroom chatroom = getByApiId(chatroomApiId);

        String otherName = "";
        for (ChatroomUser user : chatroom.getUsers()) {
            if (!user.getUser().getUsername().equals(currentUsername)) {
                otherName = user.getUser().getUsername();
            }else{
                if (!otherName.equals(currentUsername)) {
                    //에러 처리
                }
            }
        }
        return ChatroomResponseDto.fromEntity(chatroom, otherName);
    }

    public Chatroom getByApiId(String apiId) {
        return chatroomRepository.findByApiId(apiId)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }

    public Chatroom getById(Long id) {
        return chatroomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }
}
