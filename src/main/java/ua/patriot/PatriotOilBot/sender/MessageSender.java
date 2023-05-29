package ua.patriot.PatriotOilBot.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MessageSender {
    final static String LOG_TEXT = "Error occurred: ";
    @Autowired
    private BotSender botSender;

    // надсилає користувачу текстове повідомлення
    public void executeMessage(SendMessage message){
        try {
            botSender.execute(message);
        } catch (TelegramApiException e) {
            log.error(LOG_TEXT + e.getMessage());
        }
    }

    // надсилає фото
    public void executePhoto(SendPhoto sendPhoto){
        try {
            botSender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(LOG_TEXT + e.getMessage());
        }
    }

    // метод головного меню
    public void menu(){
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Почати роботу"));
        listOfCommands.add(new BotCommand("/help", "Допомога"));
        listOfCommands.add(new BotCommand("/price", "Актуальні ціни на пальне"));
        listOfCommands.add(new BotCommand("/location","Розташування АЗС"));
        listOfCommands.add(new BotCommand("/bonus", "Бонусна картка"));
        listOfCommands.add(new BotCommand("/work","Графік роботи"));
        listOfCommands.add(new BotCommand("/feedback","Залишити відгук"));
        try {
            botSender.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(LOG_TEXT + e.getMessage());
        }
    }
}
