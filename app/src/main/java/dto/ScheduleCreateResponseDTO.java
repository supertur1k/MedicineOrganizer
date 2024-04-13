package dto;

import java.util.List;

public class ScheduleCreateResponseDTO {

    private Long idOfSchedule;
    private String username;
    private String name;

    public Long getIdOfSchedule() {
        return idOfSchedule;
    }

    public void setIdOfSchedule(Long idOfSchedule) {
        this.idOfSchedule = idOfSchedule;
    }

    public ScheduleCreateResponseDTO(Long idOfSchedule, String username, String name, String comment, String amount, Integer duration, List<String> daysOfWeeks, List<String> times) {
        this.idOfSchedule = idOfSchedule;
        this.username = username;
        this.name = name;
        this.comment = comment;
        this.amount = amount;
        this.duration = duration;
        this.daysOfWeeks = daysOfWeeks;
        this.times = times;
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

    private String comment;
    private String amount;
    private Integer duration;
    private List<String> daysOfWeeks;
    private List<String> times;
}
