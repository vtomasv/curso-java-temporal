package com.sigeo.clase15.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "inbox_messages")
public class InboxMessage {

    @Id
    private String messageId;
    private LocalDateTime processedAt;

    protected InboxMessage() {}

    public InboxMessage(String messageId, LocalDateTime processedAt) {
        this.messageId = messageId;
        this.processedAt = processedAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}
