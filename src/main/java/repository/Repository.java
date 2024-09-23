package repository;

import model.entity.aEntity;
import org.hibernate.*;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.*;

import java.util.List;

public abstract class Repository<T> {
    private final Class<T> clazz;
    private final SessionFactory sessionFactory;

    public Repository(final Class<T> clazzToSet, SessionFactory sessionFactory)   {
        this.clazz = clazzToSet;
        this.sessionFactory = sessionFactory;
    }

    public T getById(final long id) {
        return getCurrentSession().get(clazz, id);
    }

    public List<T> findAll() {
        return getCurrentSession().createQuery("from " + clazz.getName(), clazz).list();
    }

//    public List<T> getItems(int from, int count) {
//        Query<T> query = getCurrentSession().createQuery("from " + clazz.getName(), clazz);
//        query.setFirstResult(from);
//        query.setMaxResults(count);
//        return query.getResultList();
//    }

    public T getByAttribute(String fieldName, String attribute) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> critQuery = builder.createQuery(clazz);
        Root<T> root = critQuery.from(clazz);
        critQuery.select(root).where(builder.equal(root.get(fieldName), attribute));
        Query<T> query = getCurrentSession().createQuery(critQuery);
        return query.getSingleResult();
    }

    public List<T> getListByStringTypeAttribute(String fieldName, String attribute) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> critQuery = builder.createQuery(clazz);
        Root<T> root = critQuery.from(clazz);
        critQuery.select(root).where(builder.equal(root.get(fieldName), attribute));
        Query<T> query = getCurrentSession().createQuery(critQuery);
        return query.getResultList();
    }

    public List<T> getListByEntityTypeAttribute(String fieldName, aEntity attribute) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> critQuery = builder.createQuery(clazz);
        Root<T> root = critQuery.from(clazz);
        critQuery.select(root).where(builder.equal(root.get(fieldName), attribute));
        Query<T> query = getCurrentSession().createQuery(critQuery);
        return query.getResultList();
    }

    public void create(final T entity) {
        getCurrentSession().persist(entity);
    }

    public void update(final T entity) {
        getCurrentSession().merge(entity);
    }

    public void delete(final T entity) {
        getCurrentSession().remove(entity);
    }

//    public void deleteById(final long entityId) {
//        final T entity = getById(entityId);
//        delete(entity);
//    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<? extends aEntity> getListBySetElement(aEntity entity) {return null;}

}
