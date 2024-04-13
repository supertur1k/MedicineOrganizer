package com.example.medicineorganizer.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dto.FirstAidKit;
import dto.ScheduleCreateResponseDTO;

public class SchedulesDataHolder {
    private static SchedulesDataHolder instance;
    private List<ScheduleCreateResponseDTO> userSchedules;

    private SchedulesDataHolder() {
        userSchedules = new ArrayList<>();
    }

    public static SchedulesDataHolder getInstance() {
        if (instance == null) {
            instance = new SchedulesDataHolder();
        }
        return instance;
    }

    public List<ScheduleCreateResponseDTO> getUserSchedules() {
        return userSchedules;
    }

    public void setUserSchedules(List<ScheduleCreateResponseDTO> userSchedules) {
        this.userSchedules = userSchedules;
    }
}
