package com.thropic.talki.notification.infrastructure.messaging.consumer;

import com.thropic.talki.notification.domain.event.ScoringCompletedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class ScoreNotificationConsumerTest {

    @InjectMocks
    private ScoreNotificationConsumer consumer;

    @Test
    void onScoringCompleted_whenEventReceived_shouldProcessWithoutException() {
        ScoringCompletedEvent event = buildEvent("user-001", "session-abc", 85);

        assertThatNoException().isThrownBy(() -> consumer.onScoringCompleted(event));
    }

    @Test
    void onScoringCompleted_whenScoreIsZero_shouldStillProcess() {
        ScoringCompletedEvent event = buildEvent("user-002", "session-xyz", 0);

        assertThatNoException().isThrownBy(() -> consumer.onScoringCompleted(event));
    }

    @Test
    void onScoringCompleted_whenScoreIsPerfect_shouldProcess() {
        ScoringCompletedEvent event = buildEvent("user-003", "session-perfect", 100);

        assertThatNoException().isThrownBy(() -> consumer.onScoringCompleted(event));
    }

    private ScoringCompletedEvent buildEvent(String userId, String sessionId, int score) {
        ScoringCompletedEvent event = new ScoringCompletedEvent();
        event.setEventId("evt-" + userId);
        event.setEventType("scoring.completed");
        event.setOccurredAt(Instant.now());
        event.setUserId(userId);
        event.setSessionId(sessionId);
        event.setOverallScore(score);
        event.setSilenceRatio(0.05);
        event.setWordsPerMinute(130.0);
        return event;
    }
}
