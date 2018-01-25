package com.safetylifeproperty.slpro.slp;

import java.util.ArrayList;

/**
 * Created by admin on 2017-09-03.
 */

public class medical_staff_main_list_position {
    //Properties of Position
    public String position;
    public String image;
    public ArrayList<String> players = new ArrayList<String>();

    public medical_staff_main_list_position(String position){
        this.position = position;
    }

    public String toString () {
        return position;
    }


}
