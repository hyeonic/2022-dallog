package com.allog.dallog.subscription.repository;

import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_1;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_2;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_3;
import static com.allog.dallog.common.fixtures.MemberFixtures.CREATOR;
import static com.allog.dallog.common.fixtures.MemberFixtures.MEMBER;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_BLUE;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_RED;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_YELLOW;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.global.config.JpaConfig;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.subscription.domain.Subscription;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
class SubscriptionRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        Member member = memberRepository.save(MEMBER);

        Member creator = memberRepository.save(CREATOR);
        Category category1 = categoryRepository.save(CATEGORY_1);
        Category category2 = categoryRepository.save(CATEGORY_2);
        Category category3 = categoryRepository.save(CATEGORY_3);

        Subscription subscription1 = new Subscription(member, category1, COLOR_RED);
        Subscription subscription2 = new Subscription(member, category2, COLOR_BLUE);
        Subscription subscription3 = new Subscription(member, category3, COLOR_YELLOW);

        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);
        subscriptionRepository.save(subscription3);

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).hasSize(3);
    }

    @DisplayName("회원의 구독 정보가 존재하지 않는 경우 빈 리스트가 조회된다.")
    @Test
    void 회원의_구독_정보가_존재하지_않는_경우_빈_리스트가_조회된다() {
        // given
        Member member = memberRepository.save(MEMBER);

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).isEmpty();
    }
}
