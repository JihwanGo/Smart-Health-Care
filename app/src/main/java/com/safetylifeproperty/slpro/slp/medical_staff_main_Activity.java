package com.safetylifeproperty.slpro.slp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

public class medical_staff_main_Activity extends AppCompatActivity {
    ExpandableListView elv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_staff_main_);
        //커스텀 액션바 생성
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);
        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        Button res_btn = (Button) findViewById(R.id.button6);
        res_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medical_staff_main_Activity.this, Patient_Registration_Activity.class);
                startActivity(intent);
            }
        });

        elv = (ExpandableListView) findViewById(R.id.elv);
        final ArrayList<medical_staff_main_list_position> position = getData();

        //create and bind to adatper
        medical_staff_main_list_adapter adapter = new medical_staff_main_list_adapter(this, position);
        elv.setAdapter(adapter);

        //set onclick listener
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), position.get(groupPosition).players.get(childPosition), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int lastClickedPosition = 0;

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // TODO Auto-generated method stub
                // 선택 한 groupPosition의 펼침/닫힘 상태 체크
                Boolean isExpand = (!elv.isGroupExpanded(groupPosition));
                // 이 전에 열려있던 group 닫기
                elv.collapseGroup(lastClickedPosition);
                if (isExpand) {
                    elv.expandGroup(groupPosition);
                }
                lastClickedPosition = groupPosition;
                return true;
            }
        });


    }

    //add and get data for list
    private ArrayList<medical_staff_main_list_position> getData() {
        medical_staff_main_list_position p1, p2, p3, p4;
        p1 = p2 = p3 = p4 = null;
        ArrayList<medical_staff_main_list_position> allposition = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            p1 = new medical_staff_main_list_position(Integer.toString(i));
            p1.players.add("20");
            allposition.add(p1);
            p2 = new medical_staff_main_list_position(Integer.toString(i));
            p2.players.add("20");
            allposition.add(p2);
            p3 = new medical_staff_main_list_position(Integer.toString(i));
            p3.players.add("20");
            allposition.add(p3);
            p4 = new medical_staff_main_list_position(Integer.toString(i));
            p4.players.add("20");
            allposition.add(p4);
        }
/*
        medical_staff_main_list_position p1 = new medical_staff_main_list_position("1");
        p1.players.add("20");

        medical_staff_main_list_position p2 = new medical_staff_main_list_position("2");
        p2.players.add("20");

        medical_staff_main_list_position p3 = new medical_staff_main_list_position("3");
        p3.players.add("20");

        medical_staff_main_list_position p4 = new medical_staff_main_list_position("4");
        p4.players.add("20");
*/


        return allposition;
    }


}
