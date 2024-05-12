package com.example.medicineorganizer.data;

import java.util.Collection;
import java.util.List;

import dto.NotificationDto;

public class HistoryDataHolder {
    private static HistoryDataHolder instance;
    private List<NotificationDto> notifications;

    private HistoryDataHolder() {
    }

    public static HistoryDataHolder getInstance() {
        if (instance == null) {
            instance = new HistoryDataHolder();
        }
        return instance;
    }

    public List<NotificationDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDto> notifications) {
        this.notifications = notifications;
    }

    public void setNotifications(Collection<NotificationDto> notifications) {
        this.notifications = (List<NotificationDto>) notifications;
    }
}
