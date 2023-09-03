package mutsa.api.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubSubMessage<T> {
    private String type;
    private T data;

    public PubSubMessage(String type, T data) {
        this.type = type;
        this.data = data;
    }
}
