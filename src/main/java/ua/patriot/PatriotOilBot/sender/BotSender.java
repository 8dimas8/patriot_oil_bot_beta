package ua.patriot.PatriotOilBot.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
@Component
public class BotSender extends DefaultAbsSender {
    @Value("${bot.token}")
    private String botToken;

    protected BotSender() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
