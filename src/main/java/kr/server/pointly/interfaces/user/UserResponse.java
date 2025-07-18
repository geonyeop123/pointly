package kr.server.pointly.interfaces.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String name,
        Long viewCount,
        LocalDate createAt
) {
    public record AddView(
            Long userId,
            Long viewCount,
            LocalDateTime modifiedAt
    ){

    }
}
