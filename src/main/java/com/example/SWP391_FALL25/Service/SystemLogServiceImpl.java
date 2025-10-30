package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.SystemLog;
import com.example.SWP391_FALL25.Repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class SystemLogServiceImpl implements SystemLogService {
    @Autowired
    private SystemLogRepository systemLogRepository;


    @Override
    public void log(Long userId, String action){
        SystemLog systemLog = new SystemLog();
        systemLog.setUserID(userId);
        systemLog.setAction(action);
        systemLog.setTimestamp(LocalDateTime.now());
        systemLogRepository.save(systemLog);
    }
}
