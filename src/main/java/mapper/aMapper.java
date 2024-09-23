package mapper;

public interface aMapper<aEntity, Command, DTO> {

    DTO mapToDTO(aEntity entity);
    aEntity mapToEntity(Command command);

}
