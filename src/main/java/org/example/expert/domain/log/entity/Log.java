package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.manager.entity.Manager;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "logs")
@Entity
public class Log extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // 매니져 생성 실패해 값이 없을 수 있음
    @Column(nullable = true)
    private Long userId;
    @Column(nullable = true)
    private Long managerId;
    @Column(nullable = true)
    private Long todoId;
    // 관리자 생성 여부에 대한 메서지 저장
    @Column
    private String isSuccess;

    private Log(String isSuccess, Manager manager) {
        this.isSuccess = isSuccess;
        this.userId = manager.getUser().getId();
        this.managerId = manager.getId();
        this.todoId = manager.getTodo().getId();
    }

    public static Log from(String isSuccess, Manager manager) {
        return new Log(isSuccess, manager);
    }
}
