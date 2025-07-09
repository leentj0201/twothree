package com.twothree.backend.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEvent extends ApplicationEvent {
    
    private final LocalDateTime eventTimestamp;
    private final String eventType;
    
    protected BaseEvent(Object source, String eventType) {
        super(source);
        this.eventTimestamp = LocalDateTime.now();
        this.eventType = eventType;
    }
} 