package service;

import repository.*;
import mapper.aMapper;
import utils.Utilities;
import org.hibernate.*;
import model.enums.*;
import model.entity.*;
import model.dto.TaskDTO;
import model.command.TaskCommand;

import java.util.*;
import java.sql.SQLException;
import java.security.InvalidParameterException;

import static constants.Constants.*;

public class TaskService extends AbstractService<TaskEntity, TaskCommand, TaskDTO> {

    public TaskService(aMapper<TaskEntity, TaskCommand, TaskDTO> mapper, Repository<TaskEntity> repository, SessionFactory sessionFactory) {
        super(mapper, repository, sessionFactory);
    }

    public void save(TaskCommand taskCommand, UserEntity user, Set<TagEntity> tags) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            TaskEntity task = mapper.mapToEntity(taskCommand);
            task.setUser(user);
            task.setTags(tags);
            for (TagEntity tag : tags) {
                tag.getTasks().add(task);
            }
            repository.update(task);
            transaction.commit();
        } catch (Exception e) {
            throw new InvalidParameterException(ENTITY_SAVE_ERROR);
        }
    }

    public TaskEntity getTaskByAttributes(String commandTitle, Long userId) {
        TaskEntity result = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            for (TaskEntity task : repository.getListByStringTypeAttribute("title", commandTitle)) {
                if (Objects.equals(task.getUser().getId(), userId)) result = task;
                break;
            }
            transaction.commit();
        }
        return result;
    }

    public List<TaskDTO> getTasks(UserEntity user, UserEntity filteredUser) {
        List<TaskDTO> result;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            if (user.getRole() == Role.ADMIN) {
                if (filteredUser != null) {
                    result = repository.getListByEntityTypeAttribute("user", filteredUser).stream().map(mapper::mapToDTO).toList();
                } else {
                    result = repository.findAll().stream().map(mapper::mapToDTO).toList();
                }
            } else {
                result = repository.getListByEntityTypeAttribute("user", user).stream().map(mapper::mapToDTO).toList();
            }
            transaction.commit();
        }
        return result;
    }

    public void editTask(String requestUrl, String[] parameters, Set<TagEntity> tags, UserEntity user) throws SQLException {
        int taskId = Utilities.getId(requestUrl, TASKS);
        if (taskId == -1) {
            throw new InvalidParameterException(ID_EXTRACT_ERROR);
        }
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            TaskEntity task = repository.getById(taskId);
            if (task !=null) {
                if (parameters[0] != null) task.setTitle(parameters[0]);
                if (parameters[1] != null) task.setUser(user);
                if (parameters[2] != null) task.setState(State.valueOf(parameters[2]));
                if (!tags.isEmpty()) task.setTags(tags);
                repository.update(task);
            } else {
                throw new SQLException(ENTITY_SAVE_ERROR);
            }
            transaction.commit();
        }
    }

    public void deleteTagFromTasks(TagEntity tag) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            List<TaskEntity> filteredTasks = ((TaskRepository) repository).getListBySetElement(tag);
            for (TaskEntity filteredTask : filteredTasks) {
                filteredTask.getTags().remove(tag);
                repository.update(filteredTask);
            }
            transaction.commit();
        }
    }
}
