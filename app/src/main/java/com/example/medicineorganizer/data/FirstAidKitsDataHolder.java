package com.example.medicineorganizer.data;

import java.util.ArrayList;
import java.util.List;

import dto.FirstAidKit;

public class FirstAidKitsDataHolder {
    private static FirstAidKitsDataHolder instance;
    private List<FirstAidKit> firstAidKits;

    private FirstAidKitsDataHolder() {
        firstAidKits = new ArrayList<>();
    }

    public static FirstAidKitsDataHolder getInstance() {
        if (instance == null) {
            instance = new FirstAidKitsDataHolder();
        }
        return instance;
    }

    public List<FirstAidKit> getFirstAidKits() {
        return firstAidKits;
    }

    public void setFirstAidKits(List<FirstAidKit> firstAidKits) {
        this.firstAidKits = firstAidKits;
    }
}
