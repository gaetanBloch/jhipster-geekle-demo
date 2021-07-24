package io.gbloch.tododemo.service.mapper;

import io.gbloch.tododemo.domain.*;
import io.gbloch.tododemo.service.dto.TagDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<TagDTO> toDtoNameSet(Set<Tag> tag);
}
