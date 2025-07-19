package kr.server.pointly.interfaces.user;

import kr.server.pointly.domain.user.FindUserSortType;
import kr.server.pointly.domain.user.UserCommand;

public record UserRequest(

) {
    public record FindAll(
            int page,
            int size,
            FindUserSortType sortType
    ){
        public UserCommand.FindAll toCommand(){
            return new UserCommand.FindAll(page, size, sortType);
        }
    }
}
