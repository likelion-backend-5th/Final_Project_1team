/**
 * @project backend
 * @author ARA
 * @since 2023-08-19 PM 4:45
 */

package mutsa.common.domain.models.article;

import mutsa.common.domain.models.Status;

public enum ArticleStatus {
    LIVE, EXPIRED;

    public static ArticleStatus of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ArticleStatus 값은 null 이나 blank가 될 수 없습니다.");
        }

        for (ArticleStatus status : ArticleStatus.values()) {
            if (value.toUpperCase().equals(status.toString())) {
                return status;
            }
        }

        throw new IllegalArgumentException("ArticleStatus에 없는 값 입니다. : " + value);
    }
}
