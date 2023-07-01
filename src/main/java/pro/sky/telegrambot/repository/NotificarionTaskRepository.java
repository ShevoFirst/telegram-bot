package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.entity.NotificationTask;

public interface NotificarionTaskRepository extends JpaRepository<NotificationTask,Long> {
}
