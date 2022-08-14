package com.maidiantech.common.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by lizisong on 2017/4/24.
 */

public abstract class AbsCommonAdapter extends BaseAdapter {
    public ArrayList mDataList = null;
    protected Activity mActivity;

    public AbsCommonAdapter(Activity activity, ArrayList aDataList) {
        this.mActivity = activity;
        mDataList = aDataList;
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mDataList != null && position >= 0 && position < mDataList.size()) {
            return mDataList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View iView = convertView;
        if (iView == null) {
            iView = createNewsConvertView(position, parent);
        }
        configValue(position, iView);
        return iView;
    }

    public abstract View createNewsConvertView(int position, ViewGroup parent);

    public abstract void configValue(int position, View convertView);
}
