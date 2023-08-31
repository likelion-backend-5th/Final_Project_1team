package mutsa.api.service.chat;

import lombok.RequiredArgsConstructor;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.report.ReportRepository;
import mutsa.common.repository.review.ReviewRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatModuleService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ReviewRepository reviewRepository;
}
