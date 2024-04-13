package dto;

import java.util.List;

public class ScheduleCreateRequestDTO {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String name;
    private String comment;
    private String amount;
    private Integer duration;
    private List<String> daysOfWeeks;

    public ScheduleCreateRequestDTO(String username, String name, String comment, String amount, Integer duration, List<String> daysOfWeeks, List<String> times) {
        this.username = username;
        this.name = name;
        this.comment = comment;
        this.amount = amount;
        this.duration = duration;
        this.daysOfWeeks = daysOfWeeks;
        this.times = times;
    }

    private List<String> times;

    public ScheduleCreateRequestDTO() {
        this.name = null;
        this.comment = null;
        this.amount = null;
        this.duration = null;
        this.daysOfWeeks = null;
        this.times = null;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<String> getDaysOfWeeks() {
        return daysOfWeeks;
    }

    public void setDaysOfWeeks(List<String> daysOfWeeks) {
        this.daysOfWeeks = daysOfWeeks;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

}
