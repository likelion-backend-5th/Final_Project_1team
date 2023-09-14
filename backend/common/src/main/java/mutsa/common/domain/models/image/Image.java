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
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity implements Serializable {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private String apiId = UUID.randomUUID().toString();

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

    @Column(nullable = false, name = "img_idx")
    private Integer imgIdx;

    @Enumerated(EnumType.STRING)
    private ImageReference imageReference;

    @Column(nullable = false)
    private String refApiId;
}
