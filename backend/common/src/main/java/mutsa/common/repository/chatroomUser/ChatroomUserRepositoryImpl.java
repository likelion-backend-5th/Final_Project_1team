package mutsa.common.repository.chatroomUser;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import mutsa.common.customRepository.Querydsl4RepositorySupport;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.chatroomUser.ChatroomUser;
import mutsa.common.domain.models.chatroomUser.QChatroomUser;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.chatroom.ChatroomUserResult;

import java.util.List;
import java.util.Optional;

public class ChatroomUserRepositoryImpl extends Querydsl4RepositorySupport implements ChatroomUserRepositoryCustom {
    public ChatroomUserRepositoryImpl() {
        super(ChatroomUser.class);
    }


    @Override
    public Optional<Chatroom> findByChatroomWithUsers(User user1, User user2) {
        QChatroomUser chatroomUser = QChatroomUser.chatroomUser;

        JPQLQuery<Chatroom> query = select(chatroomUser.chatroom)
                .from(chatroomUser)
                .where(chatroomUser.user.in(user1, user2))
                .groupBy(chatroomUser.chatroom)
                .having(chatroomUser.user.countDistinct().eq(2L));


        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public List<ChatroomUserResult> findByUser(User user) {
        QChatroomUser chatroomUser1 = new QChatroomUser("chatroomUser1");
        QChatroomUser chatroomUser2 = new QChatroomUser("chatroomUser2");

        JPAQuery<ChatroomUserResult> query = select(Projections.constructor(ChatroomUserResult.class,
                chatroomUser2.user.username,
                chatroomUser2.chatroom.apiId))
                .from(chatroomUser1)
                .join(chatroomUser2).on(chatroomUser1.chatroom.eq(chatroomUser2.chatroom))
                .where(chatroomUser1.user.eq(user).and(chatroomUser2.user.ne(user)));

        return query.fetch();
    }
}