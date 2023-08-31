package mutsa.api.dto.chat;

import lombok.Getter;
import lombok.Setter;
import mutsa.common.domain.models.user.Member;

@Getter
@Setter
public class ChatControllerMessage {
    private MessageType type;
    private Member member;
    private String data;

}
