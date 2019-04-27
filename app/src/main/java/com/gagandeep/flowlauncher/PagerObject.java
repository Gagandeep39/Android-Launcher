package com.gagandeep.flowlauncher;

import java.util.ArrayList;

class PagerObject {
    ArrayList<AppObject> appList;

    public PagerObject(ArrayList<AppObject> appList) {
        this.appList = appList;
    }

    public ArrayList<AppObject> getAppList() {
        return appList;
    }
}
