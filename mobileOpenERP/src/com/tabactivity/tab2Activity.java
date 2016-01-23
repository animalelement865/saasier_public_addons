package com.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class tab2Activity  extends Activity
{
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            
            TextView  tv=new TextView(this);
            tv.setTextSize(25);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setText("This Is Tab1 Activity");
            
            setContentView(tv);
        }
}

