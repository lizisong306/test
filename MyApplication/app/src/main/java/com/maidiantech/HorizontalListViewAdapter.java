package com.maidiantech;
import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import entity.LangyaSimple;

public class HorizontalListViewAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;

	private List<LangyaSimple> list;
	private Handler mHandler;

	public HorizontalListViewAdapter(Context context, List<LangyaSimple> list, Handler handler){
		this.mContext = context;
        this.mHandler = handler;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		LangyaSimple item = list.get(position);
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.choice_adapter, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.delete);
			holder.mTitle=(TextView)convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.mImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				list.remove(position);
				Message msg = Message.obtain();
				msg.what=10001;
				mHandler.sendMessage(msg);

			}
		});
		holder.mTitle.setText(item.getTitle());
		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle ;
		private ImageView mImage;
	}
}