package kr.server.pointly.domain.user;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class FindAll{

        @DisplayName("page와 pageNo를 받아 페이징된 유저 목록을 조회한다.")
        @Test
        void findAll() {
            // given
            UserCommand.FindAll command = new UserCommand.FindAll(1, 10, null);
            List<User> content = List.of(User.create("이건엽"), User.create("홍길동"));
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> page = new PageImpl<>(content, pageable, 2);
            when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

            // when
            Page<User> users = userService.findAll(command);

            // then
            assertThat(users.getContent().size()).isEqualTo(2);
            verify(userRepository, times(1)).findAll(any(Pageable.class));
        }

    }

    @Nested
    class AddView {
        @DisplayName("정상적인 userId로 조회수 증가를 요청하면 해당하는 유저의 조회수를 증가시킨 후 유저를 반환한다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            UserCommand.AddView command = new UserCommand.AddView(userId);
            when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(User.create("이건엽")));

            // when
            User user = userService.addView(command);

            // then
            assertThat(user).isNotNull();
            verify(userRepository, times(1)).findByIdForUpdate(userId);
        }

        @DisplayName("userId에 해당하는 유저가 없는 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail() {
            // given
            Long userId = 1L;
            UserCommand.AddView command = new UserCommand.AddView(userId);

            // when

            assertThatThrownBy(() ->userService.addView(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당되는 유저가 없습니다.");

            // then
        }
    }


}