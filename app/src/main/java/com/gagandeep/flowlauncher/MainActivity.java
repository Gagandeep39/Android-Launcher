package com.gagandeep.flowlauncher;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AppObject> appList, installedApplist;
    ViewPager mViewPager;
    int cellHeight;
    int ROW_COUNT = 5;
    int DRAWER_PEEK_HEIGHT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initializeHome();
        initializeDrawer();
    }

    private void initializeHome() {
        ArrayList<PagerObject> pagerAppList = new ArrayList<>();
        ArrayList<AppObject> appList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            appList.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground)));
        }
        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));
        cellHeight = (getDisplayContentHeight() - DRAWER_PEEK_HEIGHT) / ROW_COUNT;

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ViewPagerAdapter(this, pagerAppList, cellHeight));
    }

    private int getDisplayContentHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeigt = 0, actionBarHeight = 0, statusbarHeight = 0;
        if (getActionBar() != null)
            actionBarHeight = getActionBar().getHeight();

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusbarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        int contentTop = (findViewById(android.R.id.content)).getTop();
        windowManager.getDefaultDisplay().getSize(size);
        screenHeigt = size.y;
        return screenHeigt - contentTop - actionBarHeight - statusbarHeight;
    }

    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        final GridView mDrawerGridView = findViewById(R.id.drawerGrid);
        final BottomSheetBehavior mBottomSheetBehaviour = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehaviour.setHideable(false);
        mBottomSheetBehaviour.setPeekHeight(200);
        installedApplist = getInstalledAppList();
        appList = new ArrayList<>();
        mDrawerGridView.setAdapter(new AppAdapter(getApplicationContext(), installedApplist, cellHeight));

        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN && mDrawerGridView.getChildAt(0).getY() != 0)
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

                if (i == BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).getY() != 0)
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private List<AppObject> getInstalledAppList() {
        List<AppObject> list = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo app : untreatedAppList) {
            String appName = app.activityInfo.loadLabel(getPackageManager()).toString();
            String packageName = app.activityInfo.packageName;
            Drawable appImage = app.activityInfo.loadIcon(getPackageManager());

            AppObject appObject = new AppObject(appName, packageName, appImage);
            if (!list.contains(appObject))
                list.add(appObject);
        }
        return list;
    }
}
