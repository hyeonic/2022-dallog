package com.allog.dallog.domain.schedule.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.관리자_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.리버_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.매트_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.파랑_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.후디_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_1분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_18시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_20시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_31일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_7일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_14시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_17시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_23시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_종료일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회식_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleServiceTest extends ServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @DisplayName("새로운 일정을 생성한다.")
    @Test
    void 새로운_일정을_생성한다() {
        // given & when
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 알록달록_회의 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        // then
        assertThat(알록달록_회의.getTitle()).isEqualTo(알록달록_회의_제목);
    }

    @DisplayName("새로운 일정을 생성 할 떄 일정 제목의 길이가 50을 초과하는 경우 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_일정_제목의_길이가_50을_초과하는_경우_예외를_던진다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        String 잘못된_일정_제목 = "일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일";
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(잘못된_일정_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시,
                알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디_id, BE_일정.getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("새로운 일정을 생성 할 떄 일정 메모의 길이가 255를 초과하는 경우 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_일정_메모의_길이가_255를_초과하는_경우_예외를_던진다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        String 잘못된_일정_메모 = "1".repeat(256);
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시,
                잘못된_일정_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디_id, BE_일정.getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("새로운 일정을 생성 할 떄 종료일시가 시작일시 이전이라면 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_종료일시가_시작일시_이전이라면_예외를_던진다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        LocalDateTime 시작일시 = 날짜_2022년_7월_15일_16시_0분;
        LocalDateTime 종료일시 = 날짜_2022년_7월_1일_0시_0분;
        ScheduleCreateRequest 일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 시작일시, 종료일시, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디_id, BE_일정.getId(), 일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("일정 생성 요청자가 카테고리의 생성자가 아닌경우 예외를 던진다")
    @Test
    void 일정_생성_요청자가_카테고리의_생성자가_아닌경우_예외를_던진다() {
        // given
        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        LocalDateTime 시작일시 = 날짜_2022년_7월_15일_16시_0분;
        LocalDateTime 종료일시 = 날짜_2022년_7월_31일_0시_0분;
        ScheduleCreateRequest 일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 시작일시, 종료일시, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(리버_id, BE_일정.getId(), 일정_생성_요청)).
                isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("일정 생성시 전달한 카테고리가 존재하지 않는다면 예외를 던진다.")
    @Test
    void 일정_생성시_전달한_카테고리가_존재하지_않는다면_예외를_던진다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());

        LocalDateTime 시작일시 = 날짜_2022년_7월_15일_16시_0분;
        LocalDateTime 종료일시 = 날짜_2022년_7월_31일_0시_0분;
        ScheduleCreateRequest 일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 시작일시, 종료일시, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디_id, 0L, 일정_생성_요청)).
                isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("일정 생성시 전달한 카테고리가 외부 연동 카테고리라면 예외를 던진다.")
    @Test
    void 일정_생성시_전달한_카테고리가_외부_연동_카테고리라면_예외를_던진다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse 대한민국_공휴일 = categoryService.save(후디_id, 대한민국_공휴일_생성_요청);

        LocalDateTime 시작일시 = 날짜_2022년_7월_15일_16시_0분;
        LocalDateTime 종료일시 = 날짜_2022년_7월_31일_0시_0분;
        ScheduleCreateRequest 일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 시작일시, 종료일시, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디_id, 대한민국_공휴일.getId(), 일정_생성_요청)).
                isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("일정의 ID로 단건 일정을 조회한다.")
    @Test
    void 일정의_ID로_단건_일정을_조회한다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 알록달록_회의 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        // when
        ScheduleResponse response = scheduleService.findById(알록달록_회의.getId());

        // then
        assertAll(() -> {
            assertThat(response.getId()).isEqualTo(알록달록_회의.getId());
            assertThat(response.getTitle()).isEqualTo(알록달록_회의_제목);
            assertThat(response.getStartDateTime()).isEqualTo(알록달록_회의_시작일시);
            assertThat(response.getEndDateTime()).isEqualTo(알록달록_회의_종료일시);
            assertThat(response.getMemo()).isEqualTo(알록달록_회의_메모);
        });
    }

    @DisplayName("존재하지 않는 일정 ID로 단건 일정을 조회하면 예외를 던진다.")
    @Test
    void 존재하지_않는_일정_ID로_단건_일정을_조회하면_예외를_던진다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);
        Long 잘못된_아이디 = 0L;

        // when & then
        assertThatThrownBy(() -> scheduleService.findById(잘못된_아이디));
    }

    @DisplayName("월별 일정 조회 시, 통합일정 정보를 반환한다.")
    @Test
    void 월별_일정_조회_시_통합일정_정보를_반환한다() {
        // given
        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(리버_id, BE_일정_생성_요청);
        ScheduleResponse 알록달록_회의 = scheduleService.save(리버_id, BE_일정.getId(), 알록달록_회의_생성_요청);
        ScheduleResponse 알록달록_회식 = scheduleService.save(리버_id, BE_일정.getId(), 알록달록_회식_생성_요청);

        // when
        List<IntegrationSchedule> schedules = scheduleService.findInternalByMemberIdAndDateRange(리버_id,
                new DateRangeRequest("2022-07-01T00:00", "2022-08-15T23:59"));

        // then
        assertThat(schedules).hasSize(2);
        assertAll(
                () -> {
                    assertThat(schedules.get(0).getId()).isEqualTo(String.valueOf(알록달록_회의.getId()));
                    assertThat(schedules.get(0).getCategoryType()).isEqualTo(NORMAL);
                    assertThat(schedules.get(1).getId()).isEqualTo(String.valueOf(알록달록_회식.getId()));
                    assertThat(schedules.get(1).getCategoryType()).isEqualTo(NORMAL);
                }
        );
    }

    @DisplayName("일정을 수정한다.")
    @Test
    void 일정을_수정한다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 기존_일정 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(레벨_인터뷰_제목, 레벨_인터뷰_시작일시, 레벨_인터뷰_종료일시, 레벨_인터뷰_메모);

        // when
        scheduleService.update(기존_일정.getId(), 후디_id, 일정_수정_요청);

        // then
        ScheduleResponse actual = scheduleService.findById(기존_일정.getId());
        assertAll(
                () -> {
                    assertThat(actual.getId()).isEqualTo(기존_일정.getId());
                    assertThat(actual.getTitle()).isEqualTo(레벨_인터뷰_제목);
                    assertThat(actual.getStartDateTime()).isEqualTo(레벨_인터뷰_시작일시);
                    assertThat(actual.getEndDateTime()).isEqualTo(레벨_인터뷰_종료일시);
                    assertThat(actual.getMemo()).isEqualTo(레벨_인터뷰_메모);
                }
        );
    }

    @DisplayName("일정 수정 시 일정의 카테고리에 대한 권한이 없을 경우 예외가 발생한다.")
    @Test
    void 일정_수정_시_일정의_카테고리에_대한_권한이_없을_경우_예외가_발생한다() {
        // given
        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 기존_일정 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(레벨_인터뷰_제목, 레벨_인터뷰_시작일시, 레벨_인터뷰_종료일시, 레벨_인터뷰_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.update(기존_일정.getId(), 리버_id, 일정_수정_요청))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("일정 수정 시 존재하지 않은 일정일 경우 예외가 발생한다.")
    @Test
    void 일정_수정_시_존재하지_않은_일정일_경우_예외가_발생한다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 기존_일정 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(레벨_인터뷰_제목, 레벨_인터뷰_시작일시, 레벨_인터뷰_종료일시, 레벨_인터뷰_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.update(기존_일정.getId() + 1, 후디_id, 일정_수정_요청))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @DisplayName("일정을 삭제한다.")
    @Test
    void 일정을_삭제한다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 알록달록_회의 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        // when
        scheduleService.delete(알록달록_회의.getId(), 후디_id);

        // then
        assertThatThrownBy(() -> scheduleService.findById(알록달록_회의.getId()))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @DisplayName("일정 삭제 시 일정의 카테고리에 대한 권한이 없을 경우 예외가 발생한다.")
    @Test
    void 일정_삭제_시_일정의_카테고리에_대한_권한이_없을_경우_예외가_발생한다() {
        // given
        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 알록달록_회의 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(알록달록_회의.getId(), 리버_id))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("일정 삭제 시 존재하지 않은 일정일 경우 예외가 발생한다.")
    @Test
    void 일정_삭제_시_존재하지_않은_일정일_경우_예외가_발생한다() {
        // given
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);
        ScheduleResponse 알록달록_회의 = scheduleService.save(후디_id, BE_일정.getId(), 알록달록_회의_생성_요청);

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(알록달록_회의.getId() + 1, 후디_id))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @DisplayName("카테고리를 구독하는 유저들의 모든 내부 일정을 가져온다.")
    @Test
    void 카테고리를_구독하는_유저들의_모든_내부_일정을_가져온다() {
        // given
        Long 관리자_id = parseMemberId(관리자_인증_코드_토큰_요청());
        CategoryResponse 공통_일정 = categoryService.save(관리자_id, 공통_일정_생성_요청);
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 1", 날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_10일_0시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 2", 날짜_2022년_7월_10일_11시_59분, 날짜_2022년_7월_15일_16시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 3", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, ""));

        Long 매트_id = parseMemberId(매트_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(매트_id, BE_일정_생성_요청);
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 1", 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_20시_0분, ""));
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 2", 날짜_2022년_7월_16일_20시_0분, 날짜_2022년_7월_20일_0시_0분, ""));
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 3", 날짜_2022년_7월_20일_11시_59분, 날짜_2022년_7월_27일_0시_0분, ""));

        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());
        Long 파랑_id = parseMemberId(파랑_인증_코드_토큰_요청());
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());

        subscriptionService.save(매트_id, 공통_일정.getId());
        subscriptionService.save(리버_id, 공통_일정.getId());
        subscriptionService.save(파랑_id, 공통_일정.getId());
        subscriptionService.save(후디_id, 공통_일정.getId());

        DateRangeRequest request = new DateRangeRequest("2022-07-07T16:00", "2022-08-15T12:00");

        // when
        List<IntegrationSchedule> actual = scheduleService.findInMembersByCategoryIdAndDateRange(공통_일정.getId(),
                request);

        // then
        assertThat(actual).hasSize(6);
    }

    @DisplayName("카테고리를 구독하는 유저들의 특정 구간의 내부 일정을 가져온다.")
    @Test
    void 카테고리를_구독하는_유저들의_특정_구간의_내부_일정을_가져온다() {
        // given
        Long 관리자_id = parseMemberId(관리자_인증_코드_토큰_요청());
        CategoryResponse 공통_일정 = categoryService.save(관리자_id, 공통_일정_생성_요청);
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 1", 날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_10일_0시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 2", 날짜_2022년_7월_10일_11시_59분, 날짜_2022년_7월_15일_16시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 3", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, ""));

        Long 매트_id = parseMemberId(매트_인증_코드_토큰_요청());

        CategoryResponse BE_일정 = categoryService.save(매트_id, BE_일정_생성_요청);
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 1", 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_20시_0분, ""));
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 2", 날짜_2022년_7월_16일_20시_0분, 날짜_2022년_7월_20일_0시_0분, ""));
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 3", 날짜_2022년_7월_20일_11시_59분, 날짜_2022년_7월_27일_0시_0분, ""));
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 4", 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, ""));
        scheduleService.save(매트_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 5", 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_23시_59분, ""));

        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());
        Long 파랑_id = parseMemberId(파랑_인증_코드_토큰_요청());
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());

        subscriptionService.save(매트_id, 공통_일정.getId());
        subscriptionService.save(리버_id, 공통_일정.getId());
        subscriptionService.save(파랑_id, 공통_일정.getId());
        subscriptionService.save(후디_id, 공통_일정.getId());

        DateRangeRequest request = new DateRangeRequest("2022-07-07T16:00", "2022-08-15T12:00");

        // when
        List<IntegrationSchedule> actual = scheduleService.findInMembersByCategoryIdAndDateRange(공통_일정.getId(),
                request);

        // then
        assertThat(actual).hasSize(6);
    }
}
