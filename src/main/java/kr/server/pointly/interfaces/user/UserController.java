package kr.server.pointly.interfaces.user;

import kr.server.pointly.interfaces.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserController implements UserDocs{

    @Override
    @GetMapping("/api/v1/users")
    public ResponseEntity<PageResponse<UserResponse>> findAll(UserRequest.FindAll request) {
        return ResponseEntity.ok(new PageResponse<>(
                List.of( new UserResponse(1L, "이건엽", 5L, LocalDate.of(2025, 7, 14))
                ,new UserResponse(1L, "홍길동", 10L, LocalDate.of(2025, 7, 13))
                ,new UserResponse(1L, "정지훈", 20L, LocalDate.of(2025, 7, 12))
                ,new UserResponse(1L, "박재혁", 30L, LocalDate.of(2025, 7, 11))),
                        1, 10, 4, 1));
    }

    @Override
    @PatchMapping("/api/v1/users/{userId}/view")
    public ResponseEntity<UserResponse.AddView> addView(
                    @PathVariable Long userId,
                    UserRequest.AddView request) {
        return ResponseEntity.ok(new UserResponse.AddView(1L, 5L, LocalDateTime.of(2025, 7, 18, 0, 0, 0)));
    }
}
