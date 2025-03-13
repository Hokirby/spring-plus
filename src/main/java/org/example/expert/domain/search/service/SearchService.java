package org.example.expert.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.response.SearchTodoResponse;
import org.example.expert.domain.todo.repository.TodoJpaQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final TodoJpaQueryDslRepository todoQueryDslRepository;

    public Page<SearchTodoResponse> searchTodo(int page, int size, String title, LocalDateTime startDate, LocalDateTime endDate, String nickname) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return todoQueryDslRepository.searchTodo(pageable, title, startDate, endDate, nickname);
    }
}
