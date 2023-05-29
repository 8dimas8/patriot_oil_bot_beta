package ua.patriot.PatriotOilBot.central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.patriot.PatriotOilBot.config.BotConfig;
import ua.patriot.PatriotOilBot.dispatcher.Dispatcher;
import ua.patriot.PatriotOilBot.sender.MessageSender;
import ua.patriot.PatriotOilBot.service.Register;

@Component
public class PatriotOilBot extends TelegramLongPollingBot {
    @Autowired
    private BotConfig config;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private Register register;
    @Autowired
    private Dispatcher dispatcher;


    // отримує ім'я чат бота
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    // отримує токен
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    // приймає оновлення
    @Override
    public void onUpdateReceived(Update update) {
            dispatcher.messageHandler(update);
    }
}
