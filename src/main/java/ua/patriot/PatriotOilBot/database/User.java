package ua.patriot.PatriotOilBot.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Entity(name = "usersDataTable")
@Component
public class User {
    @Id
    private long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
    private String userFeedback;
    private int userBonus;
    private boolean feedbackStatus;
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    // гетери та сетери для даних користувача, які зберігаються в бд

    public boolean getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(boolean feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }


    public int getUserBonus() {
        return userBonus;
    }

    public void setUserBonus(int userBonus) {
        this.userBonus = userBonus;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }


    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
