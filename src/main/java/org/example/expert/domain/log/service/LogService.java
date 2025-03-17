package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.example.expert.domain.manager.entity.Manager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LogService {

    private final LogRepository logRepository;

    // 관리자 저장시 log 객체 저장메소드
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveManagerLog(String isSuccess, Manager manager) {
        Log log = Log.from(isSuccess, manager);
        logRepository.save(log);
    }
}
