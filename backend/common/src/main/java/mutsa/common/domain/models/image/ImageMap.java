///**
// * @project backend
// * @author ARA
// * @since 2023-09-01 AM 7:42
// */
//
//package mutsa.common.domain.models.image;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.UUID;
//
//@Entity(name = "image_map")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ImageMap implements Serializable {
//    @Id
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    @Builder.Default
//    private String apiId = UUID.randomUUID().toString();
//
//    @Column(name = "ref_api_id", nullable = false)
//    private String refApiId;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "image_id", nullable = false)
//    private List<Image> images;
//
//    @Enumerated(EnumType.STRING)
//    private ImageReference imageReference;
//
//    @Column(nullable = false)
//    private Integer index;
//}
