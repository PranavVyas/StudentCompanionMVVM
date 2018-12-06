package com.vyas.pranav.studentcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main_activity)
    Toolbar toolbarMainActivity;
    @BindView(R.id.frame_main_activity_container)
    FrameLayout frameFragmentContainer;

    Drawer mDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbarMainActivity);
        mDrawer = NavigationDrawerUtil.getMaterialDrawer(this,toolbarMainActivity);
    }
}
