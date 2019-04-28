package com.gagandeep.flowlauncher;
import android.graphics.drawable.Drawable;

public class AppObject {
    private String name,
            packageName;
    private Drawable image;
    private Boolean isAppInDrawer;

    public AppObject(String packageName, String name, Drawable image, Boolean isAppInDrawer) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.isAppInDrawer = isAppInDrawer;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Boolean getIsAppInDrawer() {
        return isAppInDrawer;
    }

    public void setIsAppInDrawer(Boolean image) {
        this.isAppInDrawer = isAppInDrawer;
    }

}