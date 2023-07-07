package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

@Service
public class NotificationTaskService {
    @Autowired
    NotificationTaskRepository notificationTaskRepository;
    public void save(NotificationTask task){
        notificationTaskRepository.save(task);
    }

}
