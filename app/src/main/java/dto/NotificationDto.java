package dto;

import java.time.LocalDate;

public class NotificationDto {

    private Long idOfNotification;
    private Long idOfTheSchedule;
    private String username;
    private String name;
    private String comment;
    private String amount;
    private String dayOfTheWeek;
    private String time;

    public Long getIdOfNotification() {
        return idOfNotification;
    }

    public void setIdOfNotification(Long idOfNotification) {
        this.idOfNotification = idOfNotification;
    }

    public NotificationDto(Long idOfNotification, Long idOfTheSchedule, String username, String name, String comment, String amount, String dayOfTheWeek, String time) {
        this.idOfNotification = idOfNotification;
        this.idOfTheSchedule = idOfTheSchedule;
        this.username = username;
        this.name = name;
        this.comment = comment;
        this.amount = amount;
        this.dayOfTheWeek = dayOfTheWeek;
        this.time = time;
    }

    public Long getIdOfTheSchedule() {
        return idOfTheSchedule;
    }

    public void setIdOfTheSchedule(Long idOfTheSchedule) {
        this.idOfTheSchedule = idOfTheSchedule;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
