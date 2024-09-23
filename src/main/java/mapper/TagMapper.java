package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import model.dto.TagDTO;
import model.entity.TagEntity;
import model.command.TagCommand;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper extends aMapper<TagEntity, TagCommand, TagDTO> {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    @Override
    TagDTO mapToDTO(TagEntity tagEntity);

    @Override
    TagEntity mapToEntity(TagCommand tagCommand);

}
