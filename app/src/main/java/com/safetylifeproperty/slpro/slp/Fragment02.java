package com.safetylifeproperty.slpro.slp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment02 extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup parentView, Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment02, parentView,false);

/* Button btn = (Button) rootView.findViewById(R.id.btn02);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Patient_Registration_Activity mainActivity = (Patient_Registration_Activity) getActivity();
                mainActivity.onFragmentChanged(1);
            }
        });
*/
        return rootView;
    }
}
