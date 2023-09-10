package mutsa.api.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mutsa.common.domain.models.user.User;
import mutsa.common.domain.models.user.embedded.Address;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserDto {

    @NotEmpty
//    @Pattern(regexp = "^[a-zA-Z0-9],{6,20}$")
    private String username;
    @NotEmpty
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\\\d~!@#$%^&*()+|=]{8,16}$")
    private String password;
    @NotEmpty
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\\\d~!@#$%^&*()+|=]{8,16}$")
    private String checkPassword;

    @NotEmpty
    private String nickname;

    @NotEmpty
//    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@\" + \"[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")
    private String email;

    @NotEmpty
    private String phoneNumber;

    private String street;
    private String zipcode;
    private String city;


    public static User from(SignUpUserDto signUpUserDto) {
        User user = User.of(signUpUserDto.getUsername(), signUpUserDto.getPassword(),
            signUpUserDto.getEmail(), null, null, null);
        Address address = Address.of(signUpUserDto.getZipcode(), signUpUserDto.getCity(),
            signUpUserDto.getStreet());
        user.addAddress(address);
        return user;
    }

    public static User from(SignUpUserDto signUpUserDto,String oauthName,String picture) {
        User user = User.of(signUpUserDto.getUsername(), signUpUserDto.getPassword(),
                signUpUserDto.getEmail(), oauthName, picture, null);
        Address address = Address.of(signUpUserDto.getZipcode(), signUpUserDto.getCity(),
                signUpUserDto.getStreet());
        user.addAddress(address);
        return user;
    }
}

