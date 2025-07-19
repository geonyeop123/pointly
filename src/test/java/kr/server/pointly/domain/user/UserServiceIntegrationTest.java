package kr.server.pointly.domain.user;

import kr.server.pointly.infrastructure.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Nested
    class FindAll{

        @DisplayName("page와 size에 해당하는 유저 목록을 이름순으로 조회할 수 있다.")
        @Test
        void findAllSortedByName() {
            // given
            jpaUserRepository.saveAll(List.of(User.create("가"), User.create("나"), User.create("다"), User.create("라"), User.create("마")));
            UserCommand.FindAll command = new UserCommand.FindAll(1, 4, FindUserSortType.NAME);

            // when
            Page<User> page = userService.findAll(command);

            // then
            assertThat(page).isNotNull();
            assertThat(page.getContent().size()).isEqualTo(4);
            assertThat(page.getTotalPages()).isEqualTo(2);
            assertThat(page.getTotalElements()).isEqualTo(5);
            assertThat(page.getContent()).extracting("name").containsExactly("가", "나", "다", "라");
        }

        @DisplayName("유저 목록을 등록 최신순으로 조회할 수 있다.")
        @Test
        void findAllSortedByCreatedAt() {
            // given
            jpaUserRepository.save(User.create("첫번째"));
            jpaUserRepository.save(User.create("두번째"));
            jpaUserRepository.save(User.create("세번째"));
            jpaUserRepository.save(User.create("네번째"));
            jpaUserRepository.save(User.create("다섯번째"));
            UserCommand.FindAll command = new UserCommand.FindAll(1, 5, FindUserSortType.CREATED_AT);

            // when
            Page<User> page = userService.findAll(command);

            // then
            assertThat(page.getContent()).extracting("name").containsExactly("첫번째", "두번째", "세번째", "네번째", "다섯번째");
        }

        // 생성순 정렬 조회
        @DisplayName("유저 목록을 조회수 순으로 정렬할 수 있다.")
        @Test
        @Sql(scripts = "/sql/userView.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void findAllSortedByViewCount() {
            // given
            UserCommand.FindAll command = new UserCommand.FindAll(1, 5, FindUserSortType.VIEW_COUNT);

            // when
            Page<User> page = userService.findAll(command);

            // then
            assertThat(page.getContent()).extracting("viewCount").containsExactly(5L, 4L, 3L, 2L, 1L);
        }

        @DisplayName("조회할 유저 목록이 없는 경우 빈 목록을 반환한다.")
        @Test
        void emptyList() {
            // given
            UserCommand.FindAll command = new UserCommand.FindAll(1, 5, FindUserSortType.VIEW_COUNT);

            // when
            Page<User> page = userService.findAll(command);

            // then
            assertThat(page.getContent()).isEmpty();
            assertThat(page.getTotalElements()).isEqualTo(0L);
        }
    }

}