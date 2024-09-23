package service;

import mapper.aMapper;
import org.hibernate.*;
import model.dto.UserDTO;
import model.entity.UserEntity;
import model.command.UserCommand;
import repository.Repository;

import java.util.List;

public class UserService extends AbstractService<UserEntity, UserCommand, UserDTO> {

    public UserService(aMapper<UserEntity, UserCommand, UserDTO> mapper, Repository<UserEntity> repository, SessionFactory sessionFactory) {
        super(mapper, repository, sessionFactory);
    }

    public List<String> getLoginList() {
        List<String> result;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            List<UserEntity> userEntities = repository.findAll();
            result = userEntities.stream().map(UserEntity::getLogin).toList();
            transaction.commit();
        }
        return result;
    }

    public UserEntity getUserByLogin(String commandLogin) {
        UserEntity result;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            result = repository.getByAttribute("login", commandLogin);
            transaction.commit();
        }
        return result;
    }
}
