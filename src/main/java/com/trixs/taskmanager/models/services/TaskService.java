package com.trixs.taskmanager.models.services;

import com.trixs.taskmanager.data.entities.TaskEntity;
import com.trixs.taskmanager.data.entities.TaskStatus;
import com.trixs.taskmanager.models.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TaskService {
    void create(TaskDTO task);
    List<TaskDTO> getAll();
    TaskDTO getById(Long taskId);
    void update(TaskDTO task);

    void edit(TaskDTO task);
    void remove(long taskId);

    List<TaskEntity> searchTasks(String title, LocalDate dueDate);
    Page<TaskEntity> findPaginatedAndSortedByStatus(int pageNo, int pageSize, String sortField, String sortDir, TaskStatus status);
}
