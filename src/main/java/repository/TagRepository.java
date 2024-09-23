package repository;

import model.entity.TagEntity;
import org.hibernate.SessionFactory;

public class TagRepository extends Repository<TagEntity> {
    public TagRepository(SessionFactory sessionFactory) {
        super(TagEntity.class, sessionFactory);
    }
}
