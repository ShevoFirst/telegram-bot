package pro.sky.telegrambot.listener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificarionTaskRepository;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private Pattern pattern = Pattern.compile(
            "(\\d{1,2}.\\d{1,2}.\\d{4} \\d{1,2}:\\d{2})\\s+([А-я\\d\\s.!,?])"
    );
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private NotificationTaskService service;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.stream()
                    .filter(update -> update.message()!=null)
                    .forEach(update -> {
                logger.info("Processing update: {}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();
                if ("/start".equals(text)) {
                    sendMessage(chatId,"Привет! Отправь свою задачу в ввиде дд.мм.гггг чч:мм текст");
                } else if (text!=null) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()){
                        LocalDateTime dateTime = parseDateTime(matcher.group(1));
                        if (Objects.isNull(dateTime)){
                            sendMessage(chatId, "неверный формат");
                        }else{
                            String txt = matcher.group(2);
                            NotificationTask task = new NotificationTask();
                            task.setChatId(chatId);
                            task.setMessage(text);
                            task.setNotificationDateTime(dateTime);
                            service.save(task);
                            sendMessage(chatId, "задача добавлена");
                        }
                    }else{

                    }
                }
            });
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    private void sendMessage(Long chatId, String text){
        SendMessage sendMessage = new SendMessage(chatId,text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()){
            logger.error("ошибка отправки текста: {}", sendResponse.description());
        }
    }
    @Nullable
    private LocalDateTime parseDateTime(String dateTime){
        try {
            return LocalDateTime.parse(dateTime, formatter);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
