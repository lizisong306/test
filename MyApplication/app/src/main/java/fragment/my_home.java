package fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.maidiantech.BackHandledFragment;
import com.maidiantech.MainActivity;
import com.maidiantech.NoDoubleClickListener;
import com.maidiantech.R;
import com.maidiantech.SearchInfo;
import com.maidiantech.SearchPage;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import adapter.PageFragmentAdapter;
import application.MyApplication;
import db.ChannelDb;
import entity.Counts;
import entity.Hotcode;
import entity.Renlist;
import entity.Sblist;
import entity.Searchcode;
import entity.Shiyanshi;
import entity.Xmlist;
import entity.Zclist;
import entity.Zllist;
import entity.Ztlist;
import entity.Zxlist;
import entity.hotwords;
import entity.searchcount;
import entity.searchresult;
import view.StyleUtils;


public class my_home extends BackHandledFragment {
	private ViewPager viewPager;
	private RadioGroup rgChannel = null;
	private HorizontalScrollView hvChannel;
	private PageFragmentAdapter adapter = null;
	private List<Fragment> fragmentList = new ArrayList<>();
	private RadioButton rb;
	private List<entity.Channel> channelList;
	private boolean checked=true;
	private MainActivity main;
	private View view1;
	private String trim;
	private  int netWorkType;
	private String searchjsons;
	private   List<String> typeList;
	private  List<Object> dataList;
	private String redianStr;
	Gson g;
	private ImageView ivDeleteText;
	AutoCompleteTextView search;
	private ImageView home_search;
	private TextView search_jilu;
	public static boolean loginState = false;
	changeTitls change;
     int widthPixels;
	int heightPixels;
	String ips,hotjsons;
	private  List<hotwords> hotWordslist;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			g =new Gson();
			if(msg.what==0){
				try {
					Searchcode searchcode = g.fromJson(searchjsons, Searchcode.class);
					Counts data = searchcode.getData();
					searchcount count = data.getCount();

					searchresult result = data.getResult();
					if(result==null){
						Toast.makeText(main, "没有相关数据", Toast.LENGTH_SHORT).show();
					}
					dataList =new ArrayList<>();
					typeList = new ArrayList<>();
					List<Zxlist> zixun = result.getZixun();
					List<Renlist> rencai = result.getRencai();
					List<Sblist> shebei = result.getShebei();
					List<Xmlist> xiangmu = result.getXiangmu();
					List<Zclist> zhengce = result.getZhengce();
					List<Zllist> zhuanli = result.getZhuanli();
					List<Ztlist> zhuanti = result.getZhuanti();
                    List<Shiyanshi> shiyanshi = result.getShiyanshi();
                    if(xiangmu!=null) {
						dataList.add(xiangmu);
						typeList.add("xiangmu");
					}
					if(rencai!=null) {
						dataList.add(rencai);
						typeList.add("rencai");
					}
                    if(shiyanshi!=null) {
                        dataList.add(shiyanshi);
                        typeList.add("shiyanshi");
                   }
					if(shebei!=null) {
						dataList.add(shebei);
						typeList.add("shebei");
					}
					if(zhuanli!=null) {
						dataList.add(zhuanli);
						typeList.add("zhuanli");
					}
					if(zixun!=null) {
						dataList.add(zixun);
						typeList.add("zixun");
					}
					if(zhengce!=null) {
						dataList.add(zhengce);
						typeList.add("zhengce");
					}
					if(zhuanti!=null) {
						dataList.add(zhuanti);
						typeList.add("zhuanti");
					}
					// Log.i("dataList",dataList.size()+"......");
					if(searchcode.getMessage().equals("获取信息成功！")) {
						Intent intent = new Intent(getActivity(), SearchInfo.class);
						Bundle b = new Bundle();
						b.putSerializable("datalist", (Serializable) dataList);
						b.putSerializable("typelist", (Serializable) typeList);
						intent.putExtras(b);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					}
				}catch (Exception e){}
			}

			if(msg.what==1){
				try {
					Hotcode hotcode = g.fromJson(hotjsons, Hotcode.class);
					hotWordslist = hotcode.getData();
					search_jilu.setText(hotWordslist.get(0).getKeyword());
				}catch (JsonSyntaxException e) {
						e.printStackTrace();
				}catch (IllegalStateException i){
				}
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(change);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view1 = inflater.inflate(R.layout.fragment_home, null);
		DisplayMetrics metrics=new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 widthPixels=metrics.widthPixels;
		 heightPixels=metrics.heightPixels;


		main= (MainActivity) getActivity();
		//LinearLayout header=(LinearLayout) view1.findViewById(R.id.head1);

		initView();
		ivDeleteText=(ImageView) view1.findViewById(R.id.ivDeleteText);
		home_search=(ImageView) view1.findViewById(R.id.home_search);
		search_jilu=(TextView) view1.findViewById(R.id.search_jilu);
		 search=(AutoCompleteTextView) view1.findViewById(R.id.search);

		home_search.setOnClickListener(new NoDoubleClickListener() {

			@Override
			protected void onNoDoubleClick(View v) {
				Intent intent = new Intent(getActivity(), SearchPage.class);
				startActivity(intent);
			}
		});
		netWorkType = NetUtils.getNetWorkType(MyApplication
				.getContext());
		if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
			Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
		}else {
			ips = MyApplication.ip;
			gethot();
		}
		set_eSearch_TextChanged();//设置eSearch搜索框的文本改变时监听器
//		set_ivDeleteText_OnClick();//设置叉叉的监听器
		search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = search.getText().toString().trim();
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
						if(trim.equals("")){
                               Toast.makeText(main, "关键词不能为空", Toast.LENGTH_SHORT).show();
                            }
						else if(TelNumMatch.issearch(trim)==false){
							Toast.makeText(main, "不能输入表情符号", Toast.LENGTH_SHORT).show();
						}
						else{

							if(event.getAction()==KeyEvent.ACTION_UP){

								Intent intent = new Intent(getActivity(), SearchInfo.class);
								intent.putExtra("trim",trim);
								startActivity(intent);
							}


                            }

//                        try {
//                            if(trim.equals("")){
//                                Toast.makeText(main, "关键词不能为空", Toast.LENGTH_SHORT).show();
//                            }else{
//                                getInstance(getActivity()).search_add(trim);
//
//                                gethistory();
//                            }
//                        }catch (Exception e){}

                    }
                    return true;
                }
                return false;
            }
        });
		return view1;
	}
	private void set_ivDeleteText_OnClick() {
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search.setText("");
			}
		});

	}
	private void set_eSearch_TextChanged() {
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {


			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() == 0){
					ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
					adapter.notifyDataSetChanged();
                   /* search_listview.setVisibility(View.GONE);
                    search_gone.setVisibility(View.VISIBLE);*/
				}
				else {
					ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
					ivDeleteText.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							search.setText("");
						}
					});
				}
			}
		});
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		StyleUtils.initSystemBar(getActivity());
		change = new changeTitls();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("changetitles");
        getActivity().registerReceiver(change,intentFilter);
		StyleUtils.setStyle(getActivity());
	}
	private void initView() {
		rgChannel = (RadioGroup) view1.findViewById(R.id.rgChannel);
		viewPager = (ViewPager) view1.findViewById(R.id.vpNewsdaoList);
//		viewPager.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//
//				return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//			}
//
//
//		});
		hvChannel = (HorizontalScrollView) view1.findViewById(R.id.hvChannel);
		rgChannel
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						viewPager.setCurrentItem(checkedId);
					}
				});
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}
			@Override
			public void onPageSelected(int position) {
				setTab(position);

			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		initTab();
		initViewPager();
		rgChannel.check(0);
	}
	private void initTab() {
		rgChannel.removeAllViews();
		List<entity.Channel> channelList = ChannelDb.getSelectedChannel();
		for (int i = 0; i < channelList.size(); i++) {
			rb = (RadioButton) LayoutInflater.from(getActivity()).inflate(
					R.layout.tab_rb, null);
			rb.setId(i);
			rb.setText(channelList.get(i).getName());
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT);
			if(widthPixels<=480){
				params.setMargins(25, 0, 25, 0);
			}else if(widthPixels<=720){
				params.setMargins(45, 0, 45, 0);
			}else if(widthPixels<=1080){
				params.setMargins(55, 0, 55, 0);
			}else if(widthPixels>=1440){
				params.setMargins(85, 0, 85, 0);
			}

			rgChannel.addView(rb, params);
		}
	}
	private void initViewPager() {
		channelList = ChannelDb.getSelectedChannel();
		for (int i = 0; i < channelList.size(); i++) {
			if(i==0){
				Recommend frag = new Recommend();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);
			}else if(i==1){

				Region frag = new Region();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);

			}
			else if(i==2){
				kejiku frag = new kejiku();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);

			}else if(i==3){

				My_project frag = new My_project();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);
			}
// else  if(i==4){
//				Policy frag = new Policy();
//				Bundle bundle = new Bundle();
//				bundle.putString("weburl", channelList.get(i).getWeburl());
//				bundle.putString("name", channelList.get(i).getName());
//				bundle.putInt("id",channelList.get(i).getId());
//				frag.setArguments(bundle);
//				fragmentList.add(frag);
//			}
 else if(i==4){
				My_Personnel frag = new My_Personnel();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);
			}else if(i==5){

				Equipment frag = new Equipment();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);
			}else if (i==6){
				Laboratory frag = new Laboratory();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);

			}else if (i==7){



				Patent frag = new Patent();
				Bundle bundle = new Bundle();
				bundle.putString("weburl", channelList.get(i).getWeburl());
				bundle.putString("name", channelList.get(i).getName());
				bundle.putInt("id",channelList.get(i).getId());
				frag.setArguments(bundle);
				fragmentList.add(frag);
			}
//			else{
//				HomeFragment frag = new HomeFragment();
//				Bundle bundle = new Bundle();
//				bundle.putString("weburl", channelList.get(i).getWeburl());
//				bundle.putString("name", channelList.get(i).getName());
//				bundle.putInt("id",channelList.get(i).getId());
//				frag.setArguments(bundle);
//				fragmentList.add(frag);
//			}

		}
		try {
			adapter = new PageFragmentAdapter(getChildFragmentManager(),
					fragmentList);
			viewPager.setOffscreenPageLimit(1);
			viewPager.setAdapter(adapter);
		}catch ( IllegalStateException ex){

		}
		catch (Exception e){

		}

	}
	public void gethistory() {
		try {
			new Thread(){
				@Override
				public void run() {
					super.run();
					String searchjson="http://123.207.164.210/api/search.php?keyword="+trim+"";
					searchjsons= OkHttpUtils.loaudstringfromurl(searchjson);

					Message msg=new Message();
					msg.what=0;
					handler.sendMessage(msg);


				}
			}.start();
		}catch (Exception e){
		}
	}
	/**
	 * 滑动ViewPager时调整ScroollView的位置以便显示按钮
	 *
	 * @param idx
	 */
	private void setTab(int idx) {
		 rb = (RadioButton) rgChannel.getChildAt(idx);
		rb.setChecked(checked);
		int left = rb.getLeft();
		int width = rb.getMeasuredWidth();
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int len = left + width / 2 - screenWidth / 2;
		hvChannel.smoothScrollTo(len, 0);// 滑动ScroollView

		for (int i = 0; i < channelList.size(); i++) {
			if (i == idx) {
				((RadioButton) rgChannel.getChildAt(i)).setTextColor(Color.parseColor("#3385ff"));
			} else {
				((RadioButton) rgChannel.getChildAt(i)).setTextColor(Color.parseColor("#181818"));
			}
		}
//		kejiku frag_zixun = (kejiku)fragmentList.get(2);
//		frag_zixun.stopLunBo();
		Recommend frag_tuijian = (Recommend)fragmentList.get(0);
		frag_tuijian.stopLunBo();
      if(idx ==0){
		  Recommend frag = (Recommend)fragmentList.get(idx);
		  frag.startLunBo();
		  frag.canelDownRefrsh();

		  Region Regfrag = (Region)fragmentList.get(1);
		  Regfrag.stopLunBo();

	  }else if(idx==1){

		  Region frag = (Region)fragmentList.get(idx);
		  frag.stopLunBo();
		  frag.canelDownRefrsh();

		  Recommend refrag = (Recommend)fragmentList.get(0);
		  refrag.stopLunBo();
	  }
	  else if(idx==2){
		  kejiku frag = (kejiku)fragmentList.get(idx);
		  frag.canelDownRefrsh();

		  Recommend refrag = (Recommend)fragmentList.get(0);
		  refrag.stopLunBo();

		  Region Regfrag = (Region)fragmentList.get(1);
		  Regfrag.stopLunBo();
		  Regfrag.canelDownRefrsh();

	  }else if(idx==3){

		  My_project frag = (My_project)fragmentList.get(idx);
		  frag.startLunBo();
		  frag.canelDownRefrsh();
	  }
//	  else if(idx==3){
//
//		  Policy frag = (Policy)fragmentList.get(idx);
//		  frag.startLunBo();
//		  frag.canelDownRefrsh();
//	  }
	  else if(idx==4){

		  My_Personnel frag = (My_Personnel)fragmentList.get(idx);
		  frag.startLunBo();
		  frag.canelDownRefrsh();
	  }else  if(idx==5){

		  Equipment frag = (Equipment)fragmentList.get(idx);
		  frag.startLunBo();
		  frag.canelDownRefrsh();
	  }else if (idx==6){

		  Laboratory frag = (Laboratory)fragmentList.get(idx);
		  frag.startLunBo();
		  frag.canelDownRefrsh();
	  }else if (idx==7){
		  Patent frag = (Patent)fragmentList.get(idx);
		  frag.startLunBo();
		  frag.canelDownRefrsh();

	  }
//	  else {
//		  HomeFragment frag = (HomeFragment)fragmentList.get(idx);
//		  frag.startLunBo();
//		  frag.canelDownRefrsh();
//	  }

//		if(frag != null){
//			if(idx == 0){
//				frag.startLunBo();
//				HomeFragment frag_zixun = (HomeFragment)fragmentList.get(1);
//				frag_zixun.stopLunBo();
//			}else if(idx == 1){
//				frag.startLunBo();
//				HomeFragment frag_tuijian = (HomeFragment)fragmentList.get(0);
//				frag_tuijian.stopLunBo();
//			}else{
//				HomeFragment frag_zixun = (HomeFragment)fragmentList.get(1);
//				frag_zixun.stopLunBo();
//				HomeFragment frag_tuijian = (HomeFragment)fragmentList.get(0);
//				frag_tuijian.stopLunBo();
//			}
//
//		}
	}
	@Override
	protected boolean onBackPressed() {
			return false;
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("主页"); //统计页面，"MainScreen"为页面名称，可自定义
//		redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
//		if(redianStr != null && !redianStr.equals("")){
//			String[] temp = redianStr.split(";");
//			if(temp != null){
//				search_jilu.setText(temp[0]);
//			}
//		}

	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("主页");
	}

	class changeTitls extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String acticion = intent.getAction();
             if(acticion.equals("changetitles")){
//				 initTab();
//				 rgChannel.check(1);
//				 setTab(1);
				 ((RadioButton) rgChannel.getChildAt(1)).setText(SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY,"全国"));
			 }
		}
	}
	public void gethot() {
		try {
			new Thread(){
				@Override
				public void run() {
					super.run();
					try {
						String timestamp = System.currentTimeMillis()+"";
						String sign="";
						ArrayList<String> sort = new ArrayList<String>();
						sort.add("timestamp"+timestamp);
						sign= KeySort.keyScort(sort);
						String hotjson="http://"+ips+"/api/hotWords.php";
						hotjsons=OkHttpUtils.loaudstringfromurl(hotjson);
						if(hotjsons != null){
							Message msg=new Message();
							msg.what=1;
							handler.sendMessage(msg);
						}
					}catch (Exception e){

					}

				}
			}.start();
		}catch (Exception e){}

	}
}
