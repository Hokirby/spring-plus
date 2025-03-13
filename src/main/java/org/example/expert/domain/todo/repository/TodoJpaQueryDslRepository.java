package org.example.expert.domain.todo.repository;

import org.example.expert.domain.search.dto.response.SearchTodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoJpaQueryDslRepository {
    Page<Todo> findAllByWeatherAndBetweenStartDateAndEndDateOrderByModifiedAtDesc(Pageable pageable, String weather, LocalDateTime startDate, LocalDateTime endDate);

    Optional<Todo> findByIdWithUser(Long todoId);

    Page<SearchTodoResponse> searchTodo(Pageable pageable, String title, LocalDateTime startDate, LocalDateTime endDate, String nickname);

}
