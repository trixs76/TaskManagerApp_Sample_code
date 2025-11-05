package com.trixs.taskmanager.controller;

import com.trixs.taskmanager.data.entities.TaskEntity;
import com.trixs.taskmanager.data.entities.TaskStatus;
import com.trixs.taskmanager.data.repositores.TaskRepository;
import com.trixs.taskmanager.models.TaskDTO;
import com.trixs.taskmanager.models.dto.mappers.TaskMapper;
import com.trixs.taskmanager.models.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskRepository taskRepository;

    //generate web page tasks/index.html
    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String renderIndexPage(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "sortField", defaultValue = "dueDate") String sortField,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "status", defaultValue = "IN_PROGRESS") TaskStatus status,
            Model model) {

        int pageSize = 3;       //number task on the page

        if (status == TaskStatus.COMPLETED) {
            sortDir = "desc";
        }

        Page<TaskEntity> page = taskService.findPaginatedAndSortedByStatus(pageNo, pageSize, sortField, sortDir, status);
        List<TaskEntity> taskList = page.getContent();

        //button sorting by COMPLETED or IN_PROGRESS at /tasks/index.html
        model.addAttribute("tasks", taskList);
        model.addAttribute("status", status);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "pages/tasks/index";
    }


    @GetMapping("/create")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String showCreateForm(@ModelAttribute TaskDTO task) {
        task.setStatus("IN_PROGRESS");                              //automatic set status for create task
        return "pages/tasks/create";
    }

    @PostMapping("/create")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String createTask(
            @Valid @ModelAttribute TaskDTO task,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (task.getStatus() == null || task.getStatus().isBlank()) {
            task.setStatus("IN_PROGRESS"); // default status
        }

        if (result.hasErrors()) {
            System.out.println(">>> Validation errors: " + result.getAllErrors());
            return showCreateForm(task);
        }

        // we call a method with one parameter – TaskService will detect the logged in user itself
        taskService.create(task);

        redirectAttributes.addFlashAttribute("success", "Task created.");
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{taskId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String showEditForm(@PathVariable Long taskId,
                               TaskDTO task) {
        TaskDTO fetchedTask = taskService.getById(taskId);
        taskMapper.updateTaskDTO(fetchedTask, task);
        return "pages/tasks/edit";
    }
    @GetMapping("/delete/{taskId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String delete(@PathVariable Long taskId) {
        taskService.remove(taskId);
        return "redirect:/tasks";
    }


    @PostMapping("/edit/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String updateTask(@PathVariable Long id,
                               @ModelAttribute("taskDTO") @Valid TaskDTO taskDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        // Set ID for DTO
        taskDTO.setTaskId(id);
        // Update
        taskService.update(taskDTO);
        // relink
        return "redirect:/tasks";
    }

    @GetMapping("/detail/{taskId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String detail(@PathVariable Long taskId, Model model) {
        TaskDTO task = taskService.getById(taskId);
        model.addAttribute("task", task);
        return "pages/tasks/detail";
    }

    @GetMapping("/search")
    public String searchTasks(@RequestParam(required = false) String title,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                                Model model) {

        List<TaskEntity> tasks = taskService.searchTasks(title, dueDate);
        model.addAttribute("tasks", tasks);
        return "pages/tasks/search";
    }
}
