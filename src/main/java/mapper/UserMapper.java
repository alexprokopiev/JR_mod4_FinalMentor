package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import model.dto.UserDTO;
import model.entity.UserEntity;
import model.command.UserCommand;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends aMapper<UserEntity, UserCommand, UserDTO> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    UserDTO mapToDTO(UserEntity userEntity);

    @Override
    UserEntity mapToEntity(UserCommand userCommand);

}
