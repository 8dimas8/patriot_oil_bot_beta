package ua.patriot.PatriotOilBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.patriot.PatriotOilBot.database.User;
import ua.patriot.PatriotOilBot.database.UserRepository;
import ua.patriot.PatriotOilBot.dispatcher.Dispatcher;
import ua.patriot.PatriotOilBot.sender.BotSender;
import ua.patriot.PatriotOilBot.sender.MessageSender;

import java.sql.Timestamp;

@Slf4j
@Component
public class Register {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BotFunction botFunction;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private BotSender botSender;
    @Autowired
    @Lazy
    private Dispatcher dispatcher;
    @Autowired
    @Lazy
    private BotButton botButton;

    // реєструє користувача та зберігає дані в бд
    public void registerUser(Update update) {
        if (userRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isEmpty()) {
            var chaId = update.getCallbackQuery().getMessage().getChatId();
            var chat = update.getCallbackQuery().getMessage().getChat();

            User user = new User();
            user.setChatId(chaId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            user.setUserBonus(100);
            userRepository.save(user);
            log.info("User saved " + user);
        }
    }
}

