package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import model.dto.CommentDTO;
import model.entity.CommentEntity;
import model.command.CommentCommand;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper extends aMapper<CommentEntity, CommentCommand, CommentDTO> {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Override
    CommentDTO mapToDTO(CommentEntity commentEntity);

    @Override
    CommentEntity mapToEntity(CommentCommand commentCommand);

}
