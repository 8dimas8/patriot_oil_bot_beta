package ua.patriot.PatriotOilBot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class BotMethod{

    private final BotService botService;

    public BotMethod(BotService botService) {
        this.botService = botService;
    }

    public void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привіт, " + name + ":wave:"+
                "\nРаді бачити Вас у нашому чат боті, тут ви знайдете усю необхідну інформацію щодо цін на пальне," +
                " ваших бонусів, розташування АЗС та багато іншого" + ":fuelpump:");

        sendMessage(chatId, answer);
        log.info("Replied to user " + name);
    }

    public void sendMessage(long chatId, String textToSend){
        prepareAndSendMessage(chatId, textToSend);
    }

    public void prepareAndSendMessage(long chaId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chaId));
        message.setText(textToSend);
        botService.executeMessage(message);
    }

}
