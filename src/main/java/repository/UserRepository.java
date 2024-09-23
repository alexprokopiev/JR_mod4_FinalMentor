package repository;

import model.entity.UserEntity;
import org.hibernate.SessionFactory;

public class UserRepository extends Repository<UserEntity> {
    public UserRepository(SessionFactory sessionFactory) {
        super(UserEntity.class, sessionFactory);
    }
}
