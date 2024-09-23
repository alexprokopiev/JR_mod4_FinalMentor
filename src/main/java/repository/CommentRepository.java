package repository;

import model.entity.CommentEntity;
import org.hibernate.SessionFactory;

public class CommentRepository extends Repository<CommentEntity> {

    public CommentRepository(SessionFactory sessionFactory) {
        super(CommentEntity.class, sessionFactory);
    }
}
