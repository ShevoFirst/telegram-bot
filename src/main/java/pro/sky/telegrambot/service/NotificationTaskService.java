package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificarionTaskRepository;

@Service
public class NotificationTaskService {
    @Autowired
    NotificarionTaskRepository notificarionTaskRepository;
    public void save(NotificationTask task){
        notificarionTaskRepository.save(task);
    }

}
