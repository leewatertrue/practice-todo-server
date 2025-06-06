package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.example.constants.TaskStatus;
import org.example.model.Task;
import org.example.model.TaskEntity;
import org.example.persist.entity.TaskRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    public Task add(String title, String description, LocalDate dueDate) {
        var e= TaskEntity.builder()
                .title(title)
                .description(description)
                .dueDate(Date.valueOf(dueDate))
                .status(TaskStatus.TODO)
                .build();

        var saved = this.taskRepository.save(e);
        return entitiyToObject(saved);
    }

    public List<Task> getAll() {
        return this.taskRepository.findAll().stream()
                .map(this::entitiyToObject)
                .collect(Collectors.toList());
    }

    public List<Task> getByDueDate(String dueDate) {
        return this.taskRepository.findAllByDueDate(Date.valueOf(dueDate)).stream()
                .map(this::entitiyToObject)
                .collect(Collectors.toList());
    }

    public List<Task> getByStatus(TaskStatus status) {
        return this.taskRepository.findAllByStatus(status).stream()
                .map(this::entitiyToObject)
                .collect(Collectors.toList());
    }

    public Task getOne(Long id) {
        var entitiy = this.getById(id);
        return this.entitiyToObject(entitiy);
    }

    public Task update(Long id, String title, String description, LocalDate dueDate) {
        var exist= this.getById(id);
        exist.setTitle(Strings.isEmpty(title) ?
                exist.getTitle() : title);
        exist.setDescription(Strings.isEmpty(description) ?
                exist.getDescription():description);
        exist.setDueDate(Objects.isNull(dueDate) ?
                exist.getDueDate():Date.valueOf(dueDate));

        var updated=this.taskRepository.save(exist);
        return this.entitiyToObject(updated);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        var entity = this.getById(id);
        entity.setStatus(status);
        var saves=this.taskRepository.save(entity);

        return this.entitiyToObject(saves);
    }

    public boolean delete(Long id) {
        try{
            this.taskRepository.deleteById(id);
        } catch(Exception e) {
            log.error("an error occured while deleting task", e.toString());
            return false;
        }
        return true;
    }

    private TaskEntity getById(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(()->
                        new IllegalArgumentException(String.format("not exists task id [%d]", id)));
    }

    private Task entitiyToObject(TaskEntity e) {
        return Task.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .status(e.getStatus())
                .dueDate(e.getDueDate().toString())
                .createdAt(e.getCreatedAt().toLocalDateTime())
                .updatedAt(e.getUpdatedAt().toLocalDateTime())
                .build();
    }
}
