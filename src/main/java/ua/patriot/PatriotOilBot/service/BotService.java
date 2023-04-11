package ua.patriot.PatriotOilBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.patriot.PatriotOilBot.sender.BotSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BotService {
    final static String LOG_TEXT = "Error occurred: ";
    private final BotSender botSender;

    public BotService(BotSender botSender) {
        this.botSender = botSender;
    }

    public void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatId));
        messageText.setText(text);
        messageText.setMessageId((int) messageId);

        try {
            botSender.execute(messageText);
        } catch (TelegramApiException e) {
            log.error(LOG_TEXT + e.getMessage());
        }
    }

    public void executeMessage(SendMessage message){
        try {
            botSender.execute(message);
        } catch (TelegramApiException e) {
            log.error(LOG_TEXT + e.getMessage());
        }
    }

    public void menu(){
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Почати роботу"));
        listOfCommands.add(new BotCommand("/register","Реєстрація"));
        listOfCommands.add(new BotCommand("/help", "Допомога"));
        listOfCommands.add(new BotCommand("/price", "Актуальні ціни на пальне"));
        listOfCommands.add(new BotCommand("/map","Карта АЗС"));
        listOfCommands.add(new BotCommand("/bonus", "Бонусна картка"));
        listOfCommands.add(new BotCommand("/work","Графік роботи"));
        listOfCommands.add(new BotCommand("/feedback","Залишити відгук"));
        listOfCommands.add(new BotCommand("/settings", "Налаштування"));
        try {
            botSender.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(LOG_TEXT + e.getMessage());
        }
    }
}
