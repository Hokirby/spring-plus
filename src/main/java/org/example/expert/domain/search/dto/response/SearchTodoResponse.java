package org.example.expert.domain.search.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class SearchTodoResponse {
    private final String title;
    private final int managersCount;
    private final int commentsCount;

    @QueryProjection
    public SearchTodoResponse(String title, int managersCount, int commentsCount) {
        this.title = title;
        this.managersCount = managersCount;
        this.commentsCount = commentsCount;
    }
}
