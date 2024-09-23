package service;

import lombok.*;
import mapper.aMapper;
import org.hibernate.*;
import repository.Repository;

import java.util.List;
import java.security.InvalidParameterException;

import static constants.Constants.*;

@Getter
@AllArgsConstructor
public abstract class AbstractService<aEntity, Command, DTO> {
    protected final aMapper<aEntity, Command, DTO> mapper;
    protected final Repository<aEntity> repository;
    protected final SessionFactory sessionFactory;

    public void save(Command command) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            aEntity entity = mapper.mapToEntity(command);
            repository.create(entity);
            transaction.commit();
        } catch (Exception e) {
            throw new InvalidParameterException(ENTITY_SAVE_ERROR);
        }
    }

    public List<DTO> getAllElements() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            List<aEntity> entities = repository.findAll();
            transaction.commit();
            return entities.stream().map(mapper::mapToDTO).toList();
        }
    }

    public aEntity getById(Long id) {
        aEntity result;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            result = repository.getById(id);
            transaction.commit();
            return result;
        }
    }

    public void deleteEntity(aEntity entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            repository.delete(entity);
            transaction.commit();
        }
    }
}
