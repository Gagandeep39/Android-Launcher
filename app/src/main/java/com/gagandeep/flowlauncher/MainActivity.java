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
    int DRAWER_PEEK_HEIGHT = 200;
    GridView mDrawerGridView;
    BottomSheetBehavior mBottomSheetBehaviour;
    AppObject mAppDrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initializeHome();
        initializeDrawer();
    }

    ViewPagerAdapter mViewPagerAdapter;

    private void initializeHome() {
        ArrayList<PagerObject> pagerAppList = new ArrayList<>();
        ArrayList<AppObject> appList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            appList.add(new AppObject("", "", getResources().getDrawable(R.drawable.aryn_wallpaper)));
        }
        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));
        cellHeight = (getDisplayContentHeight() - DRAWER_PEEK_HEIGHT) / ROW_COUNT;

        mViewPager = findViewById(R.id.viewPager);
        mViewPagerAdapter = new ViewPagerAdapter(this, pagerAppList, cellHeight);

        mViewPager.setAdapter(mViewPagerAdapter);
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
        mDrawerGridView = findViewById(R.id.drawerGrid);
        mBottomSheetBehaviour = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehaviour.setHideable(false);
        mBottomSheetBehaviour.setPeekHeight(DRAWER_PEEK_HEIGHT);
        installedApplist = getInstalledAppList();
        appList = new ArrayList<>();
        mDrawerGridView.setAdapter(new AppAdapter(MainActivity.this, installedApplist, cellHeight));

        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED && mDrawerGridView.getChildAt(0).getY() != 0)
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);

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


    public void itemPress(AppObject app) {
        if (mAppDrag != null) {
            app.setPackageName(mAppDrag.getPackageName());
            app.setName(mAppDrag.getName());
            app.setImage(mAppDrag.getImage());
            mAppDrag = null;
            mViewPagerAdapter.notifyGridChange();
        } else {
            Intent launcherAppIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(app.getPackageName());
            if (launcherAppIntent != null)
                getApplicationContext().startActivity(launcherAppIntent);
        }
    }

    public void itemLongPress(AppObject appObject) {
        collapseDrawer();
        mAppDrag = appObject;
    }

    public void collapseDrawer() {
        mDrawerGridView.setY(DRAWER_PEEK_HEIGHT);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
