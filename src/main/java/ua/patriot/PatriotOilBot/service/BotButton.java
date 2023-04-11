package ua.patriot.PatriotOilBot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class BotButton {
    private final BotService botService;
    final static String PRICE = EmojiParser.parseToUnicode("Виберіть потрібний вид пального" + ":fuelpump:");

    public BotButton(BotService botService) {
        this.botService = botService;
    }

    public void sendPrice(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(PRICE);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var dieselButton = new InlineKeyboardButton();
        dieselButton.setText("ДП");
        dieselButton.setCallbackData("DIESEL_BUTTON");

        var gasolineButton = new InlineKeyboardButton();
        gasolineButton.setText("Бензин");
        gasolineButton.setCallbackData("GASOLINE_BUTTON");

        var gasButton = new InlineKeyboardButton();
        gasButton.setText("Газ");
        gasButton.setCallbackData("GAS_BUTTON");

        rowInLine.add(dieselButton);
        rowInLine.add(gasolineButton);
        rowInLine.add(gasButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        botService.executeMessage(message);
    }

    public void chooseGasoline(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Який бензин Вам потрібен?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> backRowInLine = new ArrayList<>();

        var button92 = new InlineKeyboardButton();
        button92.setText("А-92");
        button92.setCallbackData("BUTTON_92");

        var button95 = new InlineKeyboardButton();
        button95.setText("А-95");
        button95.setCallbackData("BUTTON_95");

        var button98 = new InlineKeyboardButton();
        button98.setText("А-98");
        button98.setCallbackData("BUTTON_98");

        var button100 = new InlineKeyboardButton();
        button100.setText("А-100");
        button100.setCallbackData("BUTTON_100");

        var backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("FUEL_MENU");

        rowInLine.add(button92);
        rowInLine.add(button95);
        rowInLine.add(button98);
        rowInLine.add(button100);
        backRowInLine.add(backButton);

        rowsInLine.add(rowInLine);
        rowsInLine.add(backRowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        botService.executeMessage(message);
    }

    public void sendBackButton(long chatId, String callbackData, String textMessage){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textMessage);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData(callbackData);

        rowInLine.add(backButton);
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        botService.executeMessage(message);
    }
}
