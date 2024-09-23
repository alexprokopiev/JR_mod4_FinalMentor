package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import model.dto.TaskDTO;
import model.entity.TaskEntity;
import model.command.TaskCommand;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper extends aMapper<TaskEntity, TaskCommand, TaskDTO> {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Override
    TaskDTO mapToDTO(TaskEntity taskEntity);

    @Override
    TaskEntity mapToEntity(TaskCommand taskCommand);

}
