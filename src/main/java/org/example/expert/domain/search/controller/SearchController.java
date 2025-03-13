package org.example.expert.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.response.SearchTodoResponse;
import org.example.expert.domain.search.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Page<SearchTodoResponse>> getTodoSearch(@RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(required = false) String title,
                                                                  @RequestParam(required = false) LocalDateTime startDate,
                                                                  @RequestParam(required = false) LocalDateTime endDate,
                                                                  @RequestParam(required = false) String nickname) {
        return ResponseEntity.ok(searchService.findTodoSearch(page, size, title, startDate, endDate, nickname));
    }


}
