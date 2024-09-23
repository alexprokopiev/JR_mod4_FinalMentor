package service;

import org.hibernate.*;
import mapper.aMapper;
import utils.Utilities;
import model.dto.TagDTO;
import model.enums.Color;
import model.entity.TagEntity;
import model.command.TagCommand;
import repository.Repository;

import java.util.*;
import java.sql.SQLException;
import java.security.InvalidParameterException;

import static constants.Constants.*;

public class TagService extends AbstractService<TagEntity, TagCommand, TagDTO>{
    public TagService(aMapper<TagEntity, TagCommand, TagDTO> mapper, Repository<TagEntity> repository, SessionFactory sessionFactory) {
        super(mapper, repository, sessionFactory);
    }

    public Set<TagEntity> getTagsSetByTitles(String[] commandTitles) {
        Set<TagEntity> result = new HashSet<>();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            for (String commandTitle : commandTitles) {
                result.add(repository.getByAttribute("title", commandTitle));
            }
            transaction.commit();
        }
        return result;
    }

    public void editTag(String requestUrl, String[] parameters) throws SQLException {
        int tagId = Utilities.getId(requestUrl, TAGS);
        if (tagId == -1) {
            throw new InvalidParameterException(ID_EXTRACT_ERROR);
        }
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            TagEntity tag = repository.getById(tagId);
            if (tag !=null) {
                if (parameters[0] != null) tag.setTitle(parameters[0]);
                if (parameters[1] != null) tag.setColor(Color.valueOf(parameters[1]));
                repository.update(tag);
            } else {
                throw new SQLException(ENTITY_SAVE_ERROR);
            }
            transaction.commit();
        }
    }
}
