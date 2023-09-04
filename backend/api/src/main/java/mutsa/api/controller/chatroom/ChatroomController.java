package mutsa.api.controller.chatroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.chat.ChatroomRequestDto;
import mutsa.api.dto.chat.ChatroomResponseDto;
import mutsa.api.service.chatroom.ChatroomService;
import mutsa.api.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static mutsa.api.util.SecurityUtil.getCurrentUsername;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
@Slf4j
public class ChatroomController {
    private final ChatroomService chatService;

    /**
     * @param requestChatroomDto
     * @return 방을 들어가는 경우 ( 기존에 방이 있는 경우 그 방을 반환한다)
     */
    @PostMapping
    public ResponseEntity<ChatroomResponseDto> createRoom(@RequestBody ChatroomRequestDto requestChatroomDto) {
        return ResponseEntity.ok(chatService.createChatRoom(requestChatroomDto, getCurrentUsername()));
    }

    /**
     *
     * @return 내가 속한 모든 방을 반환
     */
    @GetMapping
    public ResponseEntity<List<ChatroomResponseDto>> getRoom() {
        return ResponseEntity.ok(chatService.findMyChatroom(SecurityUtil.getCurrentUsername()));
    }

    /**
     *
     * @param chatroomApiId
     * @return 한개의 방 정보를 반환
     */
    @GetMapping("/{chatroomId}")
    public ResponseEntity<ChatroomResponseDto> getOneRoom(@PathVariable("chatroomId") String chatroomApiId) {
        return ResponseEntity.ok(chatService.findChatroom(chatroomApiId,SecurityUtil.getCurrentUsername()));
    }

}
