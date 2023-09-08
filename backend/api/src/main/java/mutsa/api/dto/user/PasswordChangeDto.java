package mutsa.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
    private String password;
    private String newPassword;
    private String newPasswordCheck;
}
