package com.dne.ems.event;

import org.springframework.context.ApplicationEvent;

import com.dne.ems.model.Task;

import lombok.Getter;

@Getter
public class TaskReadyForAssignmentEvent extends ApplicationEvent {

    private final Task task;

    public TaskReadyForAssignmentEvent(Object source, Task task) {
        super(source);
        this.task = task;
    }
} 