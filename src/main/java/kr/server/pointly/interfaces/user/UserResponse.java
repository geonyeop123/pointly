package kr.server.pointly.interfaces.user;

import kr.server.pointly.domain.user.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String name,
        Long viewCount,
        LocalDateTime createAt
) {

    public static UserResponse from(User user){
        return new UserResponse(user.getId(), user.getName(), user.getViewCount(), user.getCreatedAt());
    }

    public record AddView(
            Long userId,
            Long viewCount,
            LocalDateTime modifiedAt
    ){
        public static UserResponse.AddView from(User user){
            return new UserResponse.AddView(user.getId(), user.getViewCount(), user.getModifiedAt());
        }
    }
}
