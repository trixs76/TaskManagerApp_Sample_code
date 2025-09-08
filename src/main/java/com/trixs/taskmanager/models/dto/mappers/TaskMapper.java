package com.trixs.taskmanager.models.dto.mappers;

import com.trixs.taskmanager.data.entities.TaskEntity;
import com.trixs.taskmanager.models.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskEntity toEntity(TaskDTO source);
    TaskDTO toDTO(TaskEntity source);

    void updateTaskDTO(TaskDTO source, @MappingTarget TaskDTO target);
    void updateTaskEntity(TaskDTO source, @MappingTarget TaskEntity target);

}
