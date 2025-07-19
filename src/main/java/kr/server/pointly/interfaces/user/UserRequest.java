package kr.server.pointly.interfaces.user;

import kr.server.pointly.domain.user.FindUserSortType;

public record UserRequest(

) {
    public record FindAll(
            int page,
            int size,
            FindUserSortType sortType
    ){

    }

    public record AddView(){

    }
}
