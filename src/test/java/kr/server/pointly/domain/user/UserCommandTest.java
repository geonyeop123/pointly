package kr.server.pointly.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class UserCommandTest {

    @Nested
    class FindAll{
        @DisplayName("FindAll을 생성했을 때 모든 필드는 null 값을 받았을 때 기본 값을 가진다.")
        @Test
        void createDefaultValue() {

            // given // when
            UserCommand.FindAll command = new UserCommand.FindAll(0, 0, null);

            // then
            assertThat(command.page()).isEqualTo(1);
            assertThat(command.size()).isEqualTo(1);
            assertThat(command.sortType()).isEqualTo(FindUserSortType.NAME);
        }

        @DisplayName("Command에 세팅된 정렬유형에 따라 Sort값을 받을 수 있다.")
        @Test
        void getSort() {
            // given
            UserCommand.FindAll command = new UserCommand.FindAll(0, 0, FindUserSortType.VIEW_COUNT);
            // when
            Sort sort = command.getSort();

            // then
            assertThat(sort).isNotNull();
        }
    }


}