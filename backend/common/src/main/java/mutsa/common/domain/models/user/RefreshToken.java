package mutsa.common.domain.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class RefreshToken {
    @Id
    @Column(name = "refreshtoken_id")
    private String key;

    private String value;

    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }
}