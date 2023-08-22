package mutsa.common.domain.models;

import java.util.Objects;

public enum Status {
    ACTIVE, DELETED;

    public static Status of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Status 값은 null 이나 blank가 될 수 없습니다.");
        }

        for (Status status : Status.values()) {
            if (value.toUpperCase().equals(status.toString())) {
                return status;
            }
        }

        throw new IllegalArgumentException("Status에 없는 값 입니다. : " + value);
    }
}
