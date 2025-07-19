package kr.server.pointly.interfaces.common;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalCount,
        int totalPages
) {

}
