package com.trixs.taskmanager.models.scheduler;

import com.trixs.taskmanager.data.entities.TaskEntity;
import com.trixs.taskmanager.data.entities.UserEntity;
import com.trixs.taskmanager.data.repositores.TaskRepository;
import com.trixs.taskmanager.models.services.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskReminderScheduler {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    public TaskReminderScheduler(TaskRepository taskRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    // It starts EVERY day at 8:00 AM
    // @Scheduled(cron = "0 0 8 * * ?")
    //@Scheduled(cron = "0 * * * * ?") // starts every minute
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // we load tasks with the user within a transaction
        List<TaskEntity> tasks = taskRepository.findByDueDate(tomorrow);

        for (TaskEntity task : tasks) {
            if (task.getUser() != null && task.getUser().getEmail() != null) {
                emailService.sendTaskReminder(
                        task.getUser().getEmail(),
                        "Warning: task deadline " + task.getTitle(),
                        "Task '" + task.getTitle() + "' has a deadline: " + task.getDueDate()
                );
            }
        }
    }
}
