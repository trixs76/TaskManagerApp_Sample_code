package com.trixs.taskmanager.models;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TaskDTO {
    private Long taskId;

    @NotBlank(message = "Fill in the title")
    @Size(max = 100, message = "Name is too long")
    private String title;

    @NotBlank(message = "Fill in the description")
    @Size(max = 200, message = "Description is too long")
    private String description;

    @NotNull(message = "Fill in the date DD-MM-YYYY")
    //@Future(message = "Date must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotBlank(message = "Category is required")
    private String category; // etc. "SCHOOL", "WORK", "DOCTOR" - like as STRING


    private String status;  // etc. "COMPLETED", "IN_PROGRESS" — like as STRING

    //getters and setters


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
