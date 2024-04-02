package com.example.medicineorganizer.actions;

import com.example.medicineorganizer.data.FirstAidKitsDataHolder;

import java.util.ArrayList;
import java.util.List;

import dto.FirstAidKit;

public class MainPageActions {

    public static ArrayList<String> getArrayListOfFirstAidKitsNames() {
        List<FirstAidKit> firstAidKits = FirstAidKitsDataHolder.getInstance().getFirstAidKits();
        ArrayList<String> firstAidKitsNames = new ArrayList<>();
        if (firstAidKits != null) {
            firstAidKits.forEach(x -> firstAidKitsNames.add(x.getName_of_the_first_aid_kit()));
        }
        return firstAidKitsNames;
    }


    public static void createFAKServerRequest() {

    }


}
