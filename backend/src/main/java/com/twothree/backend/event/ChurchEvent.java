package com.twothree.backend.event;

import com.twothree.backend.entity.Church;
import lombok.Getter;

@Getter
public class ChurchEvent extends BaseEvent {
    
    private final Church church;
    private final String action;
    
    public ChurchEvent(Object source, Church church, String action) {
        super(source, "CHURCH_" + action.toUpperCase());
        this.church = church;
        this.action = action;
    }
    
    public static class ChurchCreated extends ChurchEvent {
        public ChurchCreated(Object source, Church church) {
            super(source, church, "CREATED");
        }
    }
    
    public static class ChurchUpdated extends ChurchEvent {
        public ChurchUpdated(Object source, Church church) {
            super(source, church, "UPDATED");
        }
    }
    
    public static class ChurchDeleted extends ChurchEvent {
        public ChurchDeleted(Object source, Church church) {
            super(source, church, "DELETED");
        }
    }
} 