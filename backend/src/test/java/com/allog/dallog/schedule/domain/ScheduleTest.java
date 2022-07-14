package com.allog.dallog.schedule.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.schedule.exception.InvalidScheduleException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ScheduleTest {

    @DisplayName("일정을 생성한다.")
    @Test
    void 일정을_생성한다() {
        // given
        String title = "알록달록 회의";
        LocalDateTime startDateTime = LocalDateTime.of(2022, 7, 5, 12, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 7, 6, 14, 30);
        String memo = "알록달록 팀회의 - 선릉 큰 강의실";

        // when & then
        assertDoesNotThrow(() -> new Schedule(title, startDateTime, endDateTime, memo));
    }

    @DisplayName("일정 제목의 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"일이삼사오육칠팔구십일이삼사오육칠팔구십일",
            "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 회의"})
    void 일정_제목의_길이가_20을_초과하는_경우_예외를_던진다(final String title) {
        //given
        String titleForSave = title;
        LocalDateTime startDateTime = LocalDateTime.of(2022, 7, 5, 12, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 7, 6, 14, 30);
        String memo = "알록달록 팀회의 - 선릉 큰 강의실";

        // when & then
        assertThatThrownBy(() -> new Schedule(titleForSave, startDateTime, endDateTime, memo))
                .isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("일정 메모의 길이가 255를 초과하는 경우 예외를 던진다.")
    @Test
    void 일정_메모의_길이가_255를_초과하는_경우_예외를_던진다() {
        // given
        String title = "알록달록 회의";
        LocalDateTime startDateTime = LocalDateTime.of(2022, 7, 5, 12, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 7, 6, 14, 30);
        String memo = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Schedule(title, startDateTime, endDateTime, memo))
                .isInstanceOf(InvalidScheduleException.class);
    }
}