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

import static org.assertj.core.api.Assertions.assertThat;
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


}