package dto;

import java.util.List;

public class ScheduleDto {

    private String name;

    public ScheduleDto(String name, String comment, Object amount, Integer duration, List<String> daysOfWeeks, List<String> times) {
        this.name = name;
        this.comment = comment;
        this.amount = amount;
        this.duration = duration;
        this.daysOfWeeks = daysOfWeeks;
        this.times = times;
    }

    public ScheduleDto() {
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

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
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
    private Object amount;
    Integer duration;
    private List<String> daysOfWeeks;
    private List<String> times;

}
