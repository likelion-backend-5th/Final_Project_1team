/**
 * @project backend
 * @author ARA
 * @since 2023-09-07 AM 9:16
 */

package mutsa.api.service.image;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.image.ImagesRequestDto;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.image.ImageReference;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.image.ImageRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ImageModuleService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Image> saveAll(List<ImagesRequestDto> imagesRequestDtos, String articleApiId) {
        User currentUser = userRepository
                .findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.SECURITY_CONTEXT_ERROR));

        List<Image> images = new ArrayList<>();

        for (int i = 0; i < imagesRequestDtos.size(); i++) {
            images.add(
                    Image.builder()
                            .path(imagesRequestDtos.get(i).getS3URL())
                            .fileName(imagesRequestDtos.get(i).getFilename())
                            .user(currentUser)
                            .imgIdx(i)
                            .imageReference(ImageReference.ARTICLE)
                            .refApiId(articleApiId)
                            .status(Status.ACTIVE)
                            .build()
            );
        }

        images = imageRepository.saveAll(images);
        return images;
    }

    @Transactional
    public void deleteByRefApiId(String refApiId) {
        List<Image> images = imageRepository.getAllByRefApiId(refApiId);

        images.forEach(image -> image.setStatus(Status.DELETED));
    }

    @Transactional
    public void deleteAllByRefId(String refApiId) {
        User currentUser = userRepository
                .findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.SECURITY_CONTEXT_ERROR));

        List<Image> images = imageRepository.getAllByRefApiId(refApiId);

        images.forEach(image -> {
            if (!image.getUser().equals(currentUser)) {
                throw new BusinessException(ErrorCode.IMAGE_USER_NOT_MATCH);
            }

            image.setStatus(Status.DELETED);
        });
    }
}
