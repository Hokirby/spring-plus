package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // fetch join 할 객체의 필드명을 배열로 지정
    // entity graph attribute: EAGER, 그외에는 entity 에 명시한 FetchType or default FetchType
    @EntityGraph(attributePaths = "user", type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT c FROM Comment c")
    List<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
