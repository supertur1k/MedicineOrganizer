package com.example.medicineorganizer.data;

import java.util.ArrayList;
import java.util.List;

import dto.FirstAidKit;

public class ActiveFirstAidKitDataHolder {
    private static ActiveFirstAidKitDataHolder instance;
    private FirstAidKit firstAidKit;

    private ActiveFirstAidKitDataHolder() {
    }

    public static ActiveFirstAidKitDataHolder getInstance() {
        if (instance == null) {
            instance = new ActiveFirstAidKitDataHolder();
        }
        return instance;
    }

    public FirstAidKit getFirstAidKit() {
        return firstAidKit;
    }

    public void setFirstAidKit(FirstAidKit firstAidKit) {
        this.firstAidKit = firstAidKit;
    }
}
