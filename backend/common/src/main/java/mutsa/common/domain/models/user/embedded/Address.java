package mutsa.common.domain.models.user.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

import java.io.Serializable;
import java.util.Comparator;

@Embeddable
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Address implements Serializable {
    private String zipcode;
    private String city;
    private String street;

    public static Address of(String zipcode, String city) {
        return Address.of(zipcode, city, null);
    }

    public static Address of(String zipcode, String city, String street) {
        return Address.builder()
                .zipcode(zipcode)
                .city(city)
                .street(street)
                .build();
    }
}
