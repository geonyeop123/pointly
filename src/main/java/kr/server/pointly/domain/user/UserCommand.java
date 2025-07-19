package kr.server.pointly.domain.user;

import org.springframework.data.domain.Sort;

public record UserCommand(

) {
    public record FindAll(
            int page,
            int size,
            FindUserSortType sortType
    ) {

        public FindAll(int page, int size, FindUserSortType sortType) {
            this.page = Math.max(page, 1);
            this.size = Math.max(size, 1);
            this.sortType = sortType == null ? FindUserSortType.NAME : sortType;
        }

        public Sort getSort(){
            if(sortType == FindUserSortType.NAME){
                return Sort.by(sortType.getField()).ascending();
            }
            return Sort.by(sortType.getField()).descending();
        }

    }
}
