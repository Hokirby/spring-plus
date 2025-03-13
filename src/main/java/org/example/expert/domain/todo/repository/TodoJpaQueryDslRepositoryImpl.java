package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.response.QSearchTodoResponse;
import org.example.expert.domain.search.dto.response.SearchTodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;

@RequiredArgsConstructor
@Repository
public class TodoJpaQueryDslRepositoryImpl implements TodoJpaQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Todo> findAllByWeatherAndBetweenStartDateAndEndDateOrderByModifiedAtDesc(Pageable pageable,
                                              @Param("weather") String weather,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate) {

        // 조건 설정
        BooleanBuilder whereClause = new BooleanBuilder();
        // 날씨 값이 없을 경우
        if (weather != null) {
            whereClause.and(todo.weather.eq(weather));
        }
        // 기간 값이 없을 경우
        if (startDate != null && endDate != null) {
            whereClause.and(todo.modifiedAt.between(startDate, endDate));
        }

        // 데이터 조회
        List<Todo> todos = queryFactory.selectFrom(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(whereClause)
                .orderBy(todo.modifiedAt.desc())
                // 페이징
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                // 목록 가져옴
                .fetch();

        // 전체 개수 조회
        // 개수가 0일 경우(null 값일 경우) NPE 방지를 위한 메소드
        long total = Objects.requireNonNullElse(queryFactory.select(todo.count())
                .from(todo)
                .where(whereClause)
                // 값 하나만 가져옴
                // 기본 값 0
                .fetchOne(),0L);

        return new PageImpl<>(todos, pageable, total);
    }

    public Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(todo)
                .join(todo.user).fetchJoin()
                .where(todo.id.eq(todoId))
                // 값 하나만 가져옴
                .fetchOne());
    }

    public Page<SearchTodoResponse> findAllByTitleAndBetweenStartDateAndEndDateOrderByCreatedAtDesc(Pageable pageable,
                                                                                                    String title,
                                                                                                    LocalDateTime startDate, LocalDateTime endDate,
                                                                                                    String nickname) {
        BooleanBuilder whereClause = new BooleanBuilder();
        // 제목 값 정보가 null 이 아닌 경우
        if (title != null) {
            whereClause.and(todo.title.contains(title));
        }
        // 기간 값 정보가 모두 null 이 아닌 경우
        if (startDate != null && endDate != null) {
            whereClause.and(todo.createdAt.between(startDate, endDate));
        // 시작 값 정보만 존재할 경우 -> 시작값보다 크거나 같은 값만 가져옴
        } else if (startDate != null) {
            whereClause.and(todo.createdAt.goe(startDate));
        // 종료 값 정보만 존재할 경우 -> 종료값보다 크거나 같은 값만 가져옴
        } else if (endDate != null) {
            whereClause.and(todo.createdAt.loe(endDate));
        }
        // 닉네임 정보가 null 이 아닌 경유
        if (nickname != null) {
            whereClause.and(manager.todo.eq(todo))
                    .and(manager.user.nickname.contains(nickname));
        }

        List<SearchTodoResponse> results = queryFactory.select(new QSearchTodoResponse(todo.title, manager.count().intValue(), comment.count().intValue()))
                .from(todo)
                .join(comment.todo, todo)
                .join(manager.todo, todo)
                .where(whereClause)
                .groupBy(todo.title)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Objects.requireNonNullElse(queryFactory
                .select(todo.count())
                .from(todo)
                .where(whereClause)
                .fetchOne(), 0L);

        return new PageImpl<>(results, pageable, total);
    }
}
