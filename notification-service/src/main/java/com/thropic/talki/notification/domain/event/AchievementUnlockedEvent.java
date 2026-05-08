package com.thropic.talki.notification.domain.event;

import java.time.Instant;

public class AchievementUnlockedEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;
    private String userId;
    private String achievementId;
    private String achievementName;
    private String description;

    public AchievementUnlockedEvent() {}

    public String getEventId() { return eventId; }
    public void setEventId(String e) { this.eventId = e; }
    public String getEventType() { return eventType; }
    public void setEventType(String e) { this.eventType = e; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant o) { this.occurredAt = o; }
    public String getUserId() { return userId; }
    public void setUserId(String u) { this.userId = u; }
    public String getAchievementId() { return achievementId; }
    public void setAchievementId(String a) { this.achievementId = a; }
    public String getAchievementName() { return achievementName; }
    public void setAchievementName(String a) { this.achievementName = a; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
}
