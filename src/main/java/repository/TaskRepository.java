package repository;

import model.entity.*;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;

import java.util.List;

public class TaskRepository extends Repository<TaskEntity> {

    public TaskRepository(SessionFactory sessionFactory) {
        super(TaskEntity.class, sessionFactory);
    }

    @Override
    public List<TaskEntity> getListBySetElement(aEntity entity) {
        TagEntity tag = (TagEntity) entity;
        String hql = "from TaskEntity tasks join tasks.tags tags where tags.id = :tagId ";
        Query<TaskEntity> query = getCurrentSession().createQuery(hql, TaskEntity.class);
        query.setParameter("tagId", tag.getId());
        return query.getResultList();
    }
}
