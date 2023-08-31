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
@RequestMapping("/api/chat")
@Slf4j
public class ChatroomController {
    private final ChatroomService chatService;

    //필요 메서드
    //내가 방을 만들어서 들어가는 경우(제안을 하는 경우)
    @PostMapping("rooms")
    public ResponseEntity<ChatroomResponseDto> createRoom(@RequestBody ChatroomRequestDto requestChatroomDto) {
        return ResponseEntity.ok(chatService.createChatRoom(requestChatroomDto, getCurrentUsername()));
    }

    @GetMapping
    public ResponseEntity<List<ChatroomResponseDto>> getRoom() {
        return ResponseEntity.ok(chatService.findMyChatroom(SecurityUtil.getCurrentUsername()));
    }

    @GetMapping("/{chatroomId}")
    public ResponseEntity<ChatroomResponseDto> getOngRoom(@PathVariable("chatroomId") String chatroomApiId) {
        return ResponseEntity.ok(chatService.findChatroom(chatroomApiId,SecurityUtil.getCurrentUsername()));
    }

}
