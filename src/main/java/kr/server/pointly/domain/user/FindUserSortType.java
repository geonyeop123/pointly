package kr.server.pointly.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FindUserSortType {

    NAME("name"),
    VIEW_COUNT("viewCount"),
    CREATED_AT("createdAt");

    private final String field;
}
