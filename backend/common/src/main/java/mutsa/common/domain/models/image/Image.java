/**
 * @project backend
 * @author ARA
 * @since 2023-09-01 AM 7:30
 */

package mutsa.common.domain.models.image;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.user.User;

import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity implements Serializable {
    @Id
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String apiId;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
