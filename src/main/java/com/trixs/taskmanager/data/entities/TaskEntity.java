package com.trixs.taskmanager.data.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="task_entity")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryStatus category; //etc. SCHOOL, WORK, DOCTOR

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status; // etc. IN_PROGRESS, COMPLETED

    // --------------- User relationship----------------
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    // I assume you have a TaskEntity -> User relationship. This is for email TaskReminderScheduler.

    // --- Getters & Setters ---


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

    public CategoryStatus getCategory() {
        return category;
    }

    public void setCategory(CategoryStatus category) {
        this.category = category;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
