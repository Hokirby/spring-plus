package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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

import static org.example.expert.domain.todo.entity.QTodo.todo;

@RequiredArgsConstructor
@Repository
public class TodoJpaQueryDslRepositoryImpl implements TodoJpaQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable,
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
}
