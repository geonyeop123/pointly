package kr.server.pointly.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserTest {

    @DisplayName("유저를 생성하면 해당 유저의 viewCount는 0이다.")
    @Test
    void create() {
        // given // when
        User user = User.create("이건엽");

        // then
        assertThat(user.getViewCount()).isEqualTo(0);
    }

}