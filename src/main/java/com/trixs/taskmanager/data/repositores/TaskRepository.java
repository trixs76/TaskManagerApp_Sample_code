package com.trixs.taskmanager.data.repositores;

import com.trixs.taskmanager.data.entities.TaskEntity;
import com.trixs.taskmanager.data.entities.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository <TaskEntity, Long> {
    List<TaskEntity> findByTitleContainingIgnoreCaseAndDueDate(String title, LocalDate dueDate);
    List<TaskEntity> findByTitleContainingIgnoreCase(String title);
    List<TaskEntity> findByDueDate(LocalDate dueDate);
    Page<TaskEntity> findByStatus(TaskStatus status, Pageable pageable);

}
