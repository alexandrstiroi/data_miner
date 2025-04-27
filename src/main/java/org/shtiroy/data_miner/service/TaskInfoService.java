package org.shtiroy.data_miner.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.entity.TaskInfo;
import org.shtiroy.data_miner.exception.DataMinerException;
import org.shtiroy.data_miner.repository.TaskInfoRepository;
import org.shtiroy.data_miner.util.TaskStatusEnum;
import org.shtiroy.data_miner.util.TaskType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Service
public class TaskInfoService {
    private final TaskInfoRepository repository;

    public TaskInfo createTask(TaskType taskType, String value){
        TaskInfo task = new TaskInfo();
        task.setTaskName(taskType.getNameEn());
        task.setTaskValue(value);
        task.setTaskStatus(TaskStatusEnum.START.getValue());
        task.setCreateStamp(LocalDateTime.now());
        task.setModifyStamp(LocalDateTime.now());
        log.info("Сохраняем таску {}", task);
        try {
            return repository.save(task);
        } catch (Exception exception){
            throw new DataMinerException("ошибка сохранения задачи");
        }
    }

    public TaskInfo updateTaskStatus(TaskInfo taskInfo, TaskStatusEnum taskStatusEnum){
        taskInfo.setTaskStatus(taskStatusEnum.getValue());
        taskInfo.setModifyStamp(LocalDateTime.now());
        try {
            return repository.save(taskInfo);
        } catch (Exception exception){
            throw new DataMinerException("ошибка сохранения задачи");
        }
    }

    public TaskInfo errorTask(TaskInfo taskInfo){
        taskInfo.setTaskStatus(TaskStatusEnum.ERROR.getValue());
        taskInfo.setModifyStamp(LocalDateTime.now());
        try {
            return repository.save(taskInfo);
        } catch (Exception exception){
            throw new DataMinerException("ошибка сохранения задачи");
        }
    }
}
