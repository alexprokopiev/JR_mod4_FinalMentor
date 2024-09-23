package service;

import mapper.aMapper;
import org.hibernate.*;
import repository.Repository;
import model.entity.*;
import model.dto.CommentDTO;
import model.command.CommentCommand;

import java.security.InvalidParameterException;

import static constants.Constants.*;

public class CommentService extends AbstractService<CommentEntity, CommentCommand, CommentDTO> {

    public CommentService(aMapper<CommentEntity, CommentCommand, CommentDTO> mapper, Repository<CommentEntity> repository, SessionFactory sessionFactory) {
        super(mapper, repository, sessionFactory);
    }

    public void save(CommentCommand commentCommand, TaskEntity task) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            CommentEntity comment = mapper.mapToEntity(commentCommand);
            comment.setTask(task);
            repository.create(comment);
            transaction.commit();
        } catch (Exception e) {
            throw new InvalidParameterException(ENTITY_SAVE_ERROR);
        }
    }
}
