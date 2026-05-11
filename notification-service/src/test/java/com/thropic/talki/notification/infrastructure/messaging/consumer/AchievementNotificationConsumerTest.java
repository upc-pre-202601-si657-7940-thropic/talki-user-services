package com.thropic.talki.notification.infrastructure.messaging.consumer;

import com.thropic.talki.notification.domain.event.AchievementUnlockedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class AchievementNotificationConsumerTest {

    @InjectMocks
    private AchievementNotificationConsumer consumer;

    @Test
    void onAchievementUnlocked_whenEventReceived_shouldProcessWithoutException() {
        AchievementUnlockedEvent event = buildEvent(
                "user-001", "ach-streak-7",
                "Constancia Semanal", "Practicaste 7 días seguidos"
        );

        assertThatNoException().isThrownBy(() -> consumer.onAchievementUnlocked(event));
    }

    @Test
    void onAchievementUnlocked_whenDescriptionIsNull_shouldNotThrow() {
        AchievementUnlockedEvent event = buildEvent(
                "user-002", "ach-first-pitch",
                "Primer Pitch", null
        );

        assertThatNoException().isThrownBy(() -> consumer.onAchievementUnlocked(event));
    }

    @Test
    void onAchievementUnlocked_whenAchievementNameContainsSpecialChars_shouldNotThrow() {
        AchievementUnlockedEvent event = buildEvent(
                "user-003", "ach-thesis-master",
                "¡Defensor de Tesis!", "Completaste 10 sesiones thesis_defense"
        );

        assertThatNoException().isThrownBy(() -> consumer.onAchievementUnlocked(event));
    }

    @Test
    void onAchievementUnlocked_whenUserIdIsEmpty_shouldStillProcess() {
        AchievementUnlockedEvent event = buildEvent(
                "", "ach-anonymous",
                "Logro fantasma", "Sin usuario"
        );

        assertThatNoException().isThrownBy(() -> consumer.onAchievementUnlocked(event));
    }

    private AchievementUnlockedEvent buildEvent(String userId, String achievementId,
                                                  String name, String description) {
        AchievementUnlockedEvent event = new AchievementUnlockedEvent();
        event.setEventId("evt-" + achievementId);
        event.setEventType("achievement.unlocked");
        event.setOccurredAt(Instant.now());
        event.setUserId(userId);
        event.setAchievementId(achievementId);
        event.setAchievementName(name);
        event.setDescription(description);
        return event;
    }
}
