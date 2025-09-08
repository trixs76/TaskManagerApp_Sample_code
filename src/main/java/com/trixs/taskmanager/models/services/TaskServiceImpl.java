package com.trixs.taskmanager.models.services;

import com.trixs.taskmanager.data.entities.TaskEntity;
import com.trixs.taskmanager.data.entities.TaskStatus;
import com.trixs.taskmanager.data.entities.UserEntity;
import com.trixs.taskmanager.data.repositores.TaskRepository;
import com.trixs.taskmanager.data.repositores.UserRepository;
import com.trixs.taskmanager.models.TaskDTO;
import com.trixs.taskmanager.models.dto.mappers.TaskMapper;
import com.trixs.taskmanager.models.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    public TaskMapper taskMapper;

    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    public UserRepository userRepository;

    @Override
    public void create(TaskDTO task){
        TaskEntity newTask = taskMapper.toEntity(task);

        // getting logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " does not exist"));

        // user assignment
        newTask.setUser(user);

        taskRepository.save(newTask);
    }

    @Override
    public void update(TaskDTO taskDTO) {
        TaskEntity entity = taskRepository.findById(taskDTO.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskMapper.updateTaskEntity(taskDTO, entity);
        taskRepository.save(entity);
    }

    @Override
    public List<TaskDTO> getAll() {
        List<TaskDTO> tasks = new ArrayList<>();

        Iterable<TaskEntity> fetchedTasks = taskRepository.findAll();
        for (TaskEntity taskEntity : fetchedTasks) {
            TaskDTO mappedTask = taskMapper.toDTO(taskEntity);
            tasks.add(mappedTask);
        }
        return tasks;
    }

    @Override
    public TaskDTO getById(Long taskId) {
        TaskEntity fetchedTask = getTaskOrThrow(taskId);
        return taskMapper.toDTO(fetchedTask);
    }


    private TaskEntity getTaskOrThrow(long taskId) {
        return taskRepository
                .findById(taskId)
                .orElseThrow(TaskNotFoundException::new); // <-- Updated line
    }

    @Override
    public void edit(TaskDTO task) {
        TaskEntity fetchedTask = getTaskOrThrow(task.getTaskId());

        taskMapper.updateTaskEntity(task, fetchedTask);
        taskRepository.save(fetchedTask);
    }

    @Override
    public void remove(long taskId) {
        TaskEntity fetchedEntity = getTaskOrThrow(taskId);
        taskRepository.delete(fetchedEntity);
    }

    @Override
    public Page<TaskEntity> findPaginatedAndSortedByStatus(
            int pageNo,
            int pageSize,
            String sortField,
            String sortDir,
            TaskStatus status) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return taskRepository.findByStatus(status, pageable);
    }

    @Override
    public List<TaskEntity> searchTasks(String title, LocalDate dueDate) {
        if (title != null && dueDate != null) {
            return taskRepository.findByTitleContainingIgnoreCaseAndDueDate(title, dueDate);
        } else if (dueDate != null) {
            return taskRepository.findByDueDate(dueDate);
        } else {
            // findAll vracia Iterable -> pre konverziu na List použijeme foreach
            List<TaskEntity> result = new ArrayList<>();
            taskRepository.findAll().forEach(result::add);
            return result;
        }
    }
}
