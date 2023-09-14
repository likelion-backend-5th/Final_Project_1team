/**
 * @project backend
 * @author ARA
 * @since 2023-09-07 AM 9:11
 */

package mutsa.common.repository.image;

import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> getAllByRefApiId(String refApiId);

    List<Image> getAllByUser(User user);

    Optional<Image> getByFileName(String filename);
}
