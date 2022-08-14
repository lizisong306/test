package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.maidiantech.R;

import java.util.ArrayList;
import java.util.LinkedList;

import adapter.TextAdapter;
import application.MyApplication;
import entity.city_index;
import entity.citystrcut;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {
	
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups = new ArrayList<String>();
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
	private TextAdapter plateListViewAdapter;
	private TextAdapter earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString = "不限";

	public ViewMiddle(Context context) {
		super(context);
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
//		plateListView.setVisibility(GONE);
		setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_round_pay));

//		for(int i=0;i< MyApplication.citystrcutList.size();i++){
//			citystrcut item = MyApplication.citystrcutList.get(i);
//			groups.add(item.key);
//			LinkedList<String> tItem = new LinkedList<>();
//			for(int j=0;j<item.item.size();j++){
//				city_index pos = item.item.get(j);
//				tItem.add(pos.name);
//			}
//			children.put(i, tItem);
//		}

		groups.add("全国");
		groups.add("华北地区");
		groups.add("东北地区");
		groups.add("华东地区");
		groups.add("中南地区");
		groups.add("西北地区");
		groups.add("西南地区");


		for(int i=0;i<MyApplication.citystrcutList.size();i++){
			groups.get(i);
			citystrcut item=MyApplication.citystrcutList.get(i);
			LinkedList<String> tItem = new LinkedList<>();
			for(int j=0;j<item.item.size();j++){
				city_index city=item.item.get(j);
				tItem.add(city.name);
			}
			children.put(i,tItem);
		}

		earaListViewAdapter = new TextAdapter(context, groups,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(17);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
//						plateListView.setVisibility(VISIBLE);
						if (position < children.size()) {
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(tEaraPosition));
		plateListViewAdapter = new TextAdapter(context, childrenItem,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
//						plateListView.setVisibility(GONE);
						String text;
						text =groups.get(earaListViewAdapter.getSelectedPosition());
						showString = childrenItem.get(position);
						if(showString.equals("不限")){
							showString = text;
						}
						String value="";
						try {
							 value = MyApplication.citystrcutList.get(earaListViewAdapter.getSelectedPosition()).item.get(position).value;
						}catch (Exception e){

						}

						if (mOnSelectListener != null) {
							mOnSelectListener.getValue(showString, value);

						}

					}
				});
		if (tBlockPosition < childrenItem.size())
			showString = childrenItem.get(tBlockPosition);
		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText,String value);
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		Log.d("lizisong", "hide");
//		plateListView.setVisibility(GONE);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
