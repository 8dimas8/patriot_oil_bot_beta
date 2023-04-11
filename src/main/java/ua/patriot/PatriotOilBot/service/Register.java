package ua.patriot.PatriotOilBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.patriot.PatriotOilBot.database.User;
import ua.patriot.PatriotOilBot.database.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class Register {
    @Autowired
    private UserRepository userRepository;
    final static String REGISTER_YES_BUTTON = "REGISTER_YES";
    final static String REGISTER_NO_BUTTON = "REGISTER_NO";
    private final BotService botService;

    public Register(BotService botService) {
        this.botService = botService;
    }

    public void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Ви дійсно бажаєте зареєструватись?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Так");
        yesButton.setCallbackData(REGISTER_YES_BUTTON);

        var noButton = new InlineKeyboardButton();
        noButton.setText("Ні");
        noButton.setCallbackData(REGISTER_NO_BUTTON);

        rowInline.add(yesButton);
        rowInline.add(noButton);
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        botService.executeMessage(message);
    }

    public void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()){
            var chaId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setChatId(chaId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("User saved " + user);
        }
    }

    public boolean checkUser(Message message){
        if (userRepository.findById(message.getChatId()).isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

}
