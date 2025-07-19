package kr.server.pointly.interfaces.user;

import kr.server.pointly.domain.user.User;
import kr.server.pointly.domain.user.UserService;
import kr.server.pointly.interfaces.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class UserController implements UserDocs{

    private final UserService userService;

    @Override
    @GetMapping("/api/v1/users")
    public ResponseEntity<PageResponse<UserResponse>> findAll(UserRequest.FindAll request) {

        Page<User> result = userService.findAll(request.toCommand());

        PageResponse<UserResponse> response = new PageResponse<>(
                result.getContent().stream().map(UserResponse::from).toList(),
                // jpa의 page Number는 0부터 세므로 1을 추가
                result.getNumber() + 1, result.getSize(), result.getTotalElements(), result.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/api/v1/users/{userId}/view")
    public ResponseEntity<UserResponse.AddView> addView(
                    @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(new UserResponse.AddView(1L, 5L, LocalDateTime.of(2025, 7, 18, 0, 0, 0)));
    }
}
