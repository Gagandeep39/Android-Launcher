package com.gagandeep.flowlauncher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends BaseAdapter {

    Context context;
    List<AppObject> appList;

    public AppAdapter(Context context, List<AppObject> appList) {
        this.context = context;
        this.appList = appList;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_app, parent, false);
        } else {
            v = convertView;
        }

        ImageView mImage = v.findViewById(R.id.image);
        TextView mTextLabel = v.findViewById(R.id.appLabel);
        LinearLayout mLayout = v.findViewById(R.id.layout);

        mImage.setImageDrawable(appList.get(position).getImage());
        mTextLabel.setText(appList.get(position).getName());
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchAppIntent = context.getPackageManager().getLaunchIntentForPackage(appList.get(position).getPackageName());
                if (launchAppIntent != null)
                    context.startActivity(launchAppIntent);
            }
        });
        return v;
    }
}
