package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.maidiantech.AuditActivity;
import com.maidiantech.BackHandledFragment;
import com.maidiantech.ChengZhangHuoBanActivity;
import com.maidiantech.ChengZhangHuoBanDeil;
import com.maidiantech.ChengZhangHuoBanShenQing;
import com.maidiantech.HuoBanActivity;
import com.maidiantech.MainActivity;
import com.maidiantech.MessageActivity;
import com.maidiantech.MyCoupon;
import com.maidiantech.MyXuqiuActivity;
import com.maidiantech.My_shezhi;
import com.maidiantech.My_yuyue;
import com.maidiantech.MycollectActivity;
import com.maidiantech.MydataActivityInfo;
import com.maidiantech.MyloginActivity;

import com.maidiantech.NewRenCaiTail;
import com.maidiantech.R;
import com.maidiantech.ReviewandPass;
import com.maidiantech.ShareActivity;
import com.maidiantech.ShenHeFail;
import com.maidiantech.TuiJianErWeiMa;
import com.maidiantech.UnitActivity;

import com.maidiantech.XinFanAnCeShi;
import com.maidiantech.XingquActivity;
import com.maidiantech.XuQiuBuChong;
import com.maidiantech.ZhuanLiShenQing;
import com.maidiantech.common.resquest.NetworkCom;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.utils.Log;
import com.umeng.analytics.MobclickAgent;

import com.umeng.socialize.UMShareAPI;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import Util.BitmapUtil;
import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import dao.Service.MainDianIcon;
import entity.Enterprise;
import entity.HuoBanEntity;
import entity.QiYeEntiry;
import entity.Usercode;
import entity.XuQiu;
import entity.userlogin;
import view.RoundImageView2;

public class My_page extends BackHandledFragment {
	private View view3;

	private RoundImageView2 my_login;
	private TextView my_username,chengzhanghuobanline;
	private MainActivity m;
	private String img;
	private String nickname;
	private String email;
	private String uerimg;
	private String uername;

	private ImageView img_icon,huo,ishuoban;
	private LinearLayout unit_info,layhuo;

	private LinearLayout  bamai_id, collectpage_id,yuyue_id,xingqu_id,my_tuijianerweima,youhuijuan_id,fenxiang_id,qiyehuoban_id,shenqingzhuanli_id;
	private TextView   bamai_line, collectpage_line,  yuyue_line,shezhi,xiaoxi,my_tian ,qiyehuoban_line,chuangxin_line;
    private LinearLayout my_xuqiu,my_zhuanjia,my_xiangmu,my_shebei,chuangxin_id;

	String loginState ="";
	String mtype = "";
	String company = "";
	String adress = "";
    String mobile = "";
	String mid = "";
	String boss;
	String hangye="";
	String product="";
	String tel="";
	String username="";
	private  String   ips;
	String LoginState="";
	private static final String PHOTO_FILE_NAME = "temp_photos.jpg";
	MainDianIcon mainDianIcon;
	int netWorkType;
	TextView message_count,shebei_count,xiangmu_count,zhuanjia_count,xuqiu_count,huiyuandanwei;
	RelativeLayout denglu;

    private Receiver receiver = new Receiver();
	TextView qiyehuobantxt, qiyehuobandes,chengzhanghuoban,compan;
	ScrollView scroll;

//	UMShareAPI mShareAPI;
//	private ShareAction action;
//	private UMImage image;
	Enterprise enterprise;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view3 = inflater.inflate(R.layout.my_page, null);
		ips = MyApplication.ip;
		m= (MainActivity) getActivity();
		return view3;
	}

	public void updatacount(){
		String messagecount = SharedPreferencesUtil.getString(SharedPreferencesUtil.INFOR_NEW,"0");
		String shebeicount  = SharedPreferencesUtil.getString(SharedPreferencesUtil.REQUIRE_NEW_7, "0");
		String xiangmucount = SharedPreferencesUtil.getString(SharedPreferencesUtil.REQUIRE_NEW_2,"0");
		String zhuanjiacount = SharedPreferencesUtil.getString(SharedPreferencesUtil.REQUIRE_NEW_4,"0");
		String xuqiucount = SharedPreferencesUtil.getString(SharedPreferencesUtil.REQUIRE_NEW_0,"0");
		message_count.setText(messagecount);
		shebei_count.setText(shebeicount);
		xiangmu_count.setText(xiangmucount);
		zhuanjia_count.setText(zhuanjiacount);
		xuqiu_count.setText(xuqiucount);
		if(messagecount.equals("0")){
			message_count.setVisibility(View.GONE);
		}else{
			message_count.setVisibility(View.VISIBLE);
		}

		if(shebeicount.equals("0")){
			shebei_count.setVisibility(View.GONE);
		}else{
			shebei_count.setVisibility(View.VISIBLE);
		}

		if(xiangmucount.equals("0")){
			xiangmu_count.setVisibility(View.GONE);
		}else{
			xiangmu_count.setVisibility(View.VISIBLE);
		}

		if(zhuanjiacount.equals("0")){
			zhuanjia_count.setVisibility(View.GONE);
		}else{
			zhuanjia_count.setVisibility(View.VISIBLE);
		}

		if(xuqiucount.equals("0")){
			xuqiu_count.setVisibility(View.GONE);
		}else{
			xuqiu_count.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		mShareAPI = UMShareAPI.get(getActivity());
		IntentFilter oninfilter = new IntentFilter();
		oninfilter.addAction("action_update_conut");
		getActivity().registerReceiver(receiver, oninfilter);
		mainDianIcon = MainDianIcon.getInstance(getContext());
		message_count = (TextView)view3.findViewById(R.id.message_count);
		shebei_count = (TextView)view3.findViewById(R.id.shebei_count);
		xiangmu_count = (TextView)view3.findViewById(R.id.xiangmu_count);
		zhuanjia_count = (TextView)view3.findViewById(R.id.zhuanjia_count);
		compan = (TextView)view3.findViewById(R.id.compan);
		chengzhanghuobanline = (TextView)view3.findViewById(R.id.chengzhanghuobanline);
		layhuo = (LinearLayout)view3.findViewById(R.id.layhuo);
		xuqiu_count =(TextView)view3.findViewById(R.id.xuqiu_count);
		youhuijuan_id = (LinearLayout)view3.findViewById(R.id.youhuijuan_id);
		fenxiang_id = (LinearLayout)view3.findViewById(R.id.fenxiang_id);
		shenqingzhuanli_id = (LinearLayout)view3.findViewById(R.id.shenqingzhuanli_id);
		chuangxin_id = (LinearLayout) view3.findViewById(R.id.chuangxin_id);
		chuangxin_line = (TextView) view3.findViewById(R.id.chuangxin_line);
		huiyuandanwei = (TextView)view3.findViewById(R.id.huiyuandanwei);
		denglu = (RelativeLayout)view3.findViewById(R.id.denglu);
		chengzhanghuoban = (TextView)view3.findViewById(R.id.chengzhanghuoban);

		ishuoban =(ImageView)view3.findViewById(R.id.ishuoban);
		scroll =(ScrollView)view3.findViewById(R.id.scroll);
		shenqingzhuanli_id.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(loginState.equals("1")){
					Intent intent = new Intent(getActivity(), ZhuanLiShenQing.class);
					startActivity(intent);
				}else {
					Intent intent=new Intent(getActivity(), MyloginActivity.class);
					//getActivity().startActivity(intent);
					//m.animations();
					startActivity(intent);
				}
			}
		});


		if(MainActivity.hasSoftKeys(getActivity().getWindowManager())){
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scroll.getLayoutParams();

//			if(Build.MODEL.equals("SM-N9500") || Build.MODEL.equals("KNT-UL10")||MyApplication.maxHeight >MyApplication.MINHEIGHT){
				layoutParams.bottomMargin=MyApplication.navigationbar+150;
//			}else{
//				layoutParams.bottomMargin=MyApplication.navigationbar;
//			}
			scroll.setLayoutParams(layoutParams);
		}

		denglu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loginState.equals("1")){
//					mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
//					if(mtype.equals("个人")){
					Intent intent =new Intent(getActivity(),MydataActivityInfo.class);
					startActivity(intent);
//					}else if(mtype.equals("企业")){
//						Intent intent =new Intent(getActivity(),Enterpriseinfo.class);
//						startActivity(intent);
//					}
				}else{
					Intent intent=new Intent(getActivity(), MyloginActivity.class);
					//getActivity().startActivity(intent);
					//m.animations();
					startActivity(intent);
				}
			}
		});

		shezhi = (TextView)view3.findViewById(R.id.shezhi);
		xiaoxi = (TextView)view3.findViewById(R.id.xiaoxi);
		my_tian =(TextView)view3.findViewById(R.id.my_tian);
		my_tian.setText("登录后可享受更多服务");
		shezhi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), My_shezhi.class);
				startActivity(intent);
//				Intent intent = new Intent(getActivity(), XuQiuBuChong.class);
//				startActivity(intent);
			}
		});
		xiaoxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(LoginState.equals("1")){
					Intent intent = new Intent(getActivity(), MessageActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});




		bamai_id = (LinearLayout)view3.findViewById(R.id.bamai_id);
		bamai_line = (TextView)view3.findViewById(R.id.baimai_line);
		bamai_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(LoginState.equals("1")){
					((MainActivity)getActivity()).changeBaimai();
				}else{
					Intent intent = new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});
		huo = (ImageView)view3.findViewById(R.id.huo);
		qiyehuoban_id = (LinearLayout)view3.findViewById(R.id.qiyehuoban_id);
		qiyehuoban_line = (TextView)view3.findViewById(R.id.qiyehuoban_line);
		qiyehuobantxt =(TextView)view3.findViewById(R.id.qiyehuobantxt);
		qiyehuobandes = (TextView)view3.findViewById(R.id.qiyehuobandes);
		qiyehuoban_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//AuditActivity
				//ReviewandPass
				//ShenHeFail
				//ChengZhangHuoBanShenQing
				if(enterprise != null){
					if(enterprise.data != null){
						if(enterprise.data.status.equals("0")){
							Intent intent = new Intent(getActivity(), MyloginActivity.class);
							startActivity(intent);
						}else if(enterprise.data.status.equals("1")){
							Intent intent = new Intent(getActivity(), AuditActivity.class);
							startActivity(intent);
						}else if(enterprise.data.status.equals("2")){
							Intent intent = new Intent(getActivity(), ChengZhangHuoBanDeil.class);
							startActivity(intent);
						}else if(enterprise.data.status.equals("3")){
							Intent intent = new Intent(getActivity(), ShenHeFail.class);
							startActivity(intent);
						}else if(enterprise.data.status.equals("-1")){
							Intent intent = new Intent(getActivity(), ChengZhangHuoBanActivity.class);
							startActivity(intent);
						}
					}

				}

//				Intent intent = new Intent(getActivity(), ChengZhangHuoBanShenQing.class);
//				startActivity(intent);
			}
		});

		collectpage_id =(LinearLayout)view3.findViewById(R.id.collectpage_id);
		collectpage_line = (TextView)view3.findViewById(R.id.collectpage_line);
		collectpage_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(LoginState.equals("1")){
					Intent intent = new Intent(getActivity(), MycollectActivity.class);
					intent.putExtra("type", "1");
					startActivity(intent);
				}else{
					Intent intent = new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}

			}
		});


		yuyue_id = (LinearLayout)view3.findViewById(R.id.yuyue_id);
		yuyue_line = (TextView)view3.findViewById(R.id.yuyue_line);
		yuyue_id.setVisibility(View.GONE);
		yuyue_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(LoginState.equals("1")){
					Intent intent = new Intent(getActivity(), My_yuyue.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});

		xingqu_id = (LinearLayout)view3.findViewById(R.id.xingqu_id);
		xingqu_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(LoginState.equals("1")){
					Intent intent = new Intent(getActivity(), XingquActivity.class);
					intent.putExtra("id","1");
					startActivity(intent);
				}else{
					Intent intent = new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});
		youhuijuan_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(LoginState.equals("1")){
					Intent intent = new Intent(getActivity(), MyCoupon.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});
		unit_info=(LinearLayout) view3.findViewById(R.id.unit_info);
		unit_info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(LoginState.equals("1")){
					Intent intent = new Intent(getActivity(), UnitActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});


		my_login=(RoundImageView2) view3.findViewById(R.id.my_login);

		my_username=(TextView) view3.findViewById(R.id.my_username);


		my_login.setOnClickListener(new NoDoubleClickListener() {
			@Override
			public void onNoDoubleClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(loginState.equals("1")){
//					mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
//					if(mtype.equals("个人")){
						Intent intent =new Intent(getActivity(),MydataActivityInfo.class);
						startActivity(intent);
//					}else if(mtype.equals("企业")){
//						Intent intent =new Intent(getActivity(),Enterpriseinfo.class);
//						startActivity(intent);
//					}
				}else{
					Intent intent=new Intent(getActivity(), MyloginActivity.class);
					//getActivity().startActivity(intent);
					//m.animations();
					startActivity(intent);
				}

			}
		});
		my_tuijianerweima = (LinearLayout)view3.findViewById(R.id.my_tuijianerweima);
		my_tuijianerweima.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), TuiJianErWeiMa.class);
				startActivity(intent);

			}
		});

		my_xuqiu = (LinearLayout)view3.findViewById(R.id.my_xuqiu);
		my_zhuanjia = (LinearLayout)view3.findViewById(R.id.my_zhuanjia);
		my_xiangmu = (LinearLayout)view3.findViewById(R.id.my_xiangmu);
		my_shebei = (LinearLayout)view3.findViewById(R.id.my_shebei);
		my_xuqiu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(loginState.equals("1")){
					Intent intent = new Intent(getActivity(), MyXuqiuActivity.class);
					intent.putExtra("type","-1");
					startActivity(intent);
				}else{
					Intent intent=new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}

			}
		});
		my_zhuanjia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(loginState.equals("1")){
					Intent intent = new Intent(getActivity(), MyXuqiuActivity.class);
					intent.putExtra("type","4");
					startActivity(intent);
				}else{
					Intent intent=new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});
		my_xiangmu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(loginState.equals("1")){
					Intent intent = new Intent(getActivity(), MyXuqiuActivity.class);
					intent.putExtra("type","2");
					startActivity(intent);
				}else{
					Intent intent=new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});
		my_shebei.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(loginState.equals("1")){
					Intent intent = new Intent(getActivity(), MyXuqiuActivity.class);
					intent.putExtra("type","7");
					startActivity(intent);
				}else{
					Intent intent=new Intent(getActivity(), MyloginActivity.class);
					startActivity(intent);
				}
			}
		});
		fenxiang_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ShareActivity.class);
				intent.putExtra("title", "钛领科技，让科技变简单");
				intent.putExtra("txt", "助力企业解决技术难题，促进院企科技成果转移");
				intent.putExtra("Tarurl", "http://www.zhongkechuangxiang.com/plus/maidianm/#page1");
				intent.putExtra("imageurl", "http://"+ips+"/uploads/logo/logo.png");
                startActivity(intent);
			}
		});
		chuangxin_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), HuoBanActivity.class);
				intent.putExtra("id",SharedPreferencesUtil.getString(SharedPreferencesUtil.HUIYUAN_ID, ""));
				startActivity(intent);
			}
		});


	}

	public abstract class NoDoubleClickListener implements View.OnClickListener {
		public static final int MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间
		private long lastClickTime = 0;
		protected abstract void onNoDoubleClick(View v);
		@Override
		public void onClick(View v) {
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
				lastClickTime = currentTime;
				onNoDoubleClick(v);
			}
		}
	}

	@Override
	protected boolean onBackPressed() {
		return false;
	}
	public void onResume() {
		super.onResume();
//		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        int type = getActivity().getWindow().getAttributes().softInputMode;
//        Log.d("lizisong", "type:"+type);
//		WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//		if (params.softInputMode == 34) {
//			// 隐藏软键盘
//			Log.d("lizisong", "隐藏软键盘");
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//		}
		yuyue_id.setVisibility(View.GONE);
		yuyue_line.setVisibility(View.GONE);
//		int hidestate = SharedPreferencesUtil.getInt(SharedPreferencesUtil.LOGIN_HIDE,0);
//		if(hidestate == 0){
//
//		}else {
//
//		}
		loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
		if(loginState.equals("1")){
			netWorkType =  NetUtils.getNetWorkType(MyApplication
					.getContext());
			if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {

			}else{
				ips = MyApplication.ip;
//				xuqiuUpdata();
				Background_login();
			}

			mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
			company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");
			username = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_NAME, "0");
			nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0");
			mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
			adress = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ADRESS, "0");
			hangye=SharedPreferencesUtil.getString(SharedPreferencesUtil.HANGYE, "");
			mobile = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_MOBILE, "0");
			boss = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_BOSS,"0");
			email = SharedPreferencesUtil.getString(SharedPreferencesUtil.EMAIL, "0");
			product=SharedPreferencesUtil.getString(SharedPreferencesUtil.PRODUCT, "0");
			tel=SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
			img=SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
			if(mtype.equals("个人")){

				bamai_id.setVisibility(View.VISIBLE);
				bamai_line.setVisibility(View.VISIBLE);

				collectpage_id.setVisibility(View.GONE);
				collectpage_line.setVisibility(View.GONE);

				yuyue_id.setVisibility(View.GONE);
				yuyue_line.setVisibility(View.GONE);
				String url =SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
				if(url != null && !url.equals("0") && !url.equals("")){
					ImageLoader.getInstance().displayImage(url, my_login);
				}else {
					BitmapUtil.upUserImageData(my_login, mainDianIcon);
				}

				my_username.setText(SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0"));
			}else if(mtype.equals("企业")){

				bamai_id.setVisibility(View.VISIBLE);
				bamai_line.setVisibility(View.VISIBLE);

				collectpage_id.setVisibility(View.GONE);
				collectpage_line.setVisibility(View.GONE);

				yuyue_id.setVisibility(View.GONE);
				yuyue_line.setVisibility(View.GONE);

				my_username.setText( SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0"));
				String url =SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
				if(url != null && !url.equals("0") && !url.equals("")){
					ImageLoader.getInstance().displayImage(url, my_login);
				}else{
				    BitmapUtil.upUserImageData(my_login, mainDianIcon);
				}
			}
			qiyejson();

//			String is_member = SharedPreferencesUtil.getString(SharedPreferencesUtil.HUIYUAN_IS_MEMBER,"");
//			if(is_member.equals("1")){
//				chuangxin_id.setVisibility(View.VISIBLE);
//				chuangxin_line.setVisibility(View.VISIBLE);
//				huiyuandanwei.setVisibility(View.VISIBLE);
//			}else{
//				chuangxin_id.setVisibility(View.GONE);
//				chuangxin_line.setVisibility(View.GONE);
//				huiyuandanwei.setVisibility(View.GONE);
//			}

		}else{
			bamai_id.setVisibility(View.VISIBLE);
			bamai_line.setVisibility(View.VISIBLE);
			my_tian.setText("登录后可享受更多服务");
			collectpage_id.setVisibility(View.GONE);
			collectpage_line.setVisibility(View.GONE);
			yuyue_id.setVisibility(View.GONE);
			yuyue_line.setVisibility(View.GONE);
			message_count.setVisibility(View.GONE);
			shebei_count.setVisibility(View.GONE);
			xiangmu_count.setVisibility(View.GONE);
			zhuanjia_count.setVisibility(View.GONE);
			xuqiu_count.setVisibility(View.GONE);
			my_login.setImageResource(R.drawable.log);
			chuangxin_id.setVisibility(View.GONE);
			chuangxin_line.setVisibility(View.GONE);
			huiyuandanwei.setVisibility(View.GONE);
			my_username.setText("登录/注册");
		}
		MobclickAgent.onPageStart("我的"); //统计页面，"MainScreen"为页面名称，可自定义
		EnterpriseGrowthPartner();
	}
	public void onPause() {
		super.onPause();
		//getActivity().overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
		MobclickAgent.onPageEnd("我的");
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
		if(resultCode==2){
			SharedPreferences logn = getActivity().getSharedPreferences("logn", Context.MODE_PRIVATE);
			 uerimg = logn.getString("uerimg", "");
			 uername = logn.getString("uername", "");
			 my_username.setText(uername);

		}

	}


	XuQiu xuqiuOBJ;
	String xuqiu="";
	/**
	 * 需求接口调用
	 * 增加加密
	 */
	public void xuqiuUpdata(){
		loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
		if(loginState.equals("1")){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ArrayList<String> sorlist = new ArrayList<String>();
						String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
						String time = System.currentTimeMillis()+"";
						sorlist.add("mid"+mid);
						sorlist.add("timestamp"+time);
						sorlist.add("version"+MyApplication.version);
						String sign = KeySort.keyScort(sorlist);
						String url = "http://"+ips+"/api/requireNum.php?mid="+mid+"&sign="+sign+"&timestamp="+time+"&version="+MyApplication.version;
						xuqiu =  OkHttpUtils.loaudstringfromurl(url);
						if(xuqiu != null){
							Message msg = Message.obtain();
							msg.what = 3;
							handler1.sendMessage(msg);
						}
					}catch (Exception e){

					}

				}
			}).start();
		}
	}


	/**
	 * 静默登录
	 */
	String loginstr="";
	public void Background_login(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
				String timestamp = System.currentTimeMillis()+"";
				String sign="";
				ArrayList<String> sort = new ArrayList<String>();
				sort.add("appid"+MainActivity.feixinCode);
				sort.add("clienttype"+"2");
				sort.add("timestamp"+timestamp);
				sort.add("accessid"+MyApplication.deviceid);
				sort.add("version"+MyApplication.version);
				sort.add("mid"+mid);
				sign = KeySort.keyScort(sort);
				String url = "http://"+ips+"/api/quiesce_login.php?mid="+mid+"&appid="+MainActivity.feixinCode+"&clienttype=2"+"&timestamp="+timestamp+"&version="+MyApplication.version+"&sign="+sign+"&accessid="+MyApplication.accessid;
				Log.d("lizisong","url:"+url);
				loginstr = OkHttpUtils.loaudstringfromurl(url);
				if(loginstr != null){
					Message msg = Message.obtain();
					msg.what = 4;
					handler1.sendMessage(msg);
				}
			}
		}).start();
	}

	/**
	 * 企业成长伙伴计划
	 */
	public void EnterpriseGrowthPartner(){
		String url ="http://"+MyApplication.ip+"/api/user_growth_partner.php";
		HashMap<String, String> map = new HashMap<>();
		map.put("method","userShow");
		String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
		if(loginState.equals("1")){
			String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
			map.put("mid",mid);
		}
		NetworkCom networkCom = NetworkCom.getNetworkCom();
		networkCom.getJson(url,map,handler1,10,0);
	}


	Handler handler1 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try{
			if(msg.what == 3){
				try {
					Gson gson = new Gson();
					xuqiuOBJ =  gson.fromJson(xuqiu, XuQiu.class);
					if(xuqiuOBJ != null){
						MainActivity.xuqiucount = xuqiuOBJ.data.num;
					}
				}catch (Exception e){

				}
//				if(MainActivity.xuqiucount > 0){
//					xuqiu_dian.setVisibility(View.VISIBLE);
//					xuqiu_dian.setText(MainActivity.xuqiucount+"");
//				}else{
//					xuqiu_dian.setVisibility(View.GONE);
//				}

				Intent intent = new Intent();
				intent.setAction("showred");
				MyApplication.context.sendBroadcast(intent);

			}
			if(msg.what == 4){
					Gson g=new Gson();
					Usercode qqcode = g.fromJson(loginstr, Usercode.class);
					if(qqcode != null){

					final userlogin data = qqcode.getData();
					String loginFlag = data.getLoginFlag();
//					MainActivity.xuqiucount = data.num;

					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, qqcode.getCode()+"");
					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, data.getUsername());
					SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, data.getNickname());
					SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, data.getMid());
					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, data.mtype);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, data.address);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, data.linkman);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, data.product);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, data.vocation);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.IMG, data.img);

					SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE,data.wq_num);
					SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE, data.patent_sq);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.JNINTIME,data.jointime);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.INFOR_NEW,data.infor_new);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_0,data.require_new_0);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_4,data.require_new_4);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_2,data.require_new_2);
					SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_7,data.require_new_7);
					updatacount();
					String time = SharedPreferencesUtil.getString(SharedPreferencesUtil.JNINTIME, "0");
					if(!time.equals("0")){
						long cha = System.currentTimeMillis()- Long.parseLong(time)*1000;
						long tian   = cha/86400000;
						my_tian.setText("已加入钛领"+(tian+2)+"天");
					}
					String url =SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
					if(url != null && !url.equals("0") && !url.equals("")){
						ImageLoader.getInstance().displayImage(url, my_login);
					}else{
						BitmapUtil.upUserImageData(my_login, mainDianIcon);
					}
//				    String infor_new = SharedPreferencesUtil.getString(SharedPreferencesUtil.INFOR_NEW, "0");
//					String require_new_0 = SharedPreferencesUtil.getString(SharedPreferencesUtil.REQUIRE_NEW_0, "0");
//					String 	require_new_2 = SharedPreferencesUtil.getString(SharedPreferencesUtil.REQUIRE_NEW_2, "0");
//					String require_new_7 = 	 SharedPreferencesUtil.getString(SharedPreferencesUtil.REQUIRE_NEW_7, "0");
					try {
						if(!data.infor_new.equals("0") || !data.require_new_0.equals("0") || !data.require_new_4.equals("0")
								|| !data.require_new_2.equals("0") || !data.require_new_7.equals("0")){
							MainActivity.xuqiucount = 1;
						}else{
							MainActivity.xuqiucount = 0;
						}
						Intent intent = new Intent();
						intent.setAction("showred");
						MyApplication.context.sendBroadcast(intent);
					}catch (Exception e){

					}


				}
			}
			if(msg.what == 10){
				String json = (String)msg.obj;
				Gson g=new Gson();
				enterprise = g.fromJson(json, Enterprise.class);
				if(enterprise != null){
					if(enterprise.code.equals("1")){
						qiyehuobantxt.setText(enterprise.data.str_name);
						qiyehuobandes.setText(enterprise.data.str_content);
					    if(enterprise.data.state.equals("1")){
							qiyehuoban_id.setVisibility(View.VISIBLE);
							qiyehuoban_line.setVisibility(View.VISIBLE);
							if(enterprise.data.status.equals("0") || enterprise.data.status.equals("-1")){
								huo.setVisibility(View.VISIBLE);
								qiyehuobandes.setTextColor(0xffff6363);
								chengzhanghuoban.setVisibility(View.GONE);
								ishuoban.setVisibility(View.GONE);
								chengzhanghuobanline.setVisibility(View.VISIBLE);

								qiyehuobantxt.setText(enterprise.data.str_name);
								qiyehuobandes.setText(enterprise.data.str_content);
								qiyehuobandes.setGravity(Gravity.RIGHT);
							}else{
								huo.setVisibility(View.GONE);
								if(enterprise.data.status.equals("2")){
									qiyehuobandes.setTextColor(0xffc3a05d);
									chengzhanghuoban.setVisibility(View.GONE);
									ishuoban.setVisibility(View.GONE);
									layhuo.setBackgroundResource(R.mipmap.icon_qiyuhuobang);
								}else{
									qiyehuobandes.setTextColor(0xffff6363);
									chengzhanghuoban.setVisibility(View.VISIBLE);
									ishuoban.setVisibility(View.VISIBLE);
									layhuo.setBackgroundResource(R.color.white);
									chengzhanghuobanline.setVisibility(View.GONE);
								}
								qiyehuobantxt.setText(enterprise.data.str_name);
								qiyehuobandes.setText(enterprise.data.str_content);
								qiyehuobandes.setGravity(Gravity.RIGHT);

							}
							if(enterprise.data.status.equals("2")){
								chengzhanghuoban.setVisibility(View.VISIBLE);
								ishuoban.setVisibility(View.VISIBLE);
								chengzhanghuobanline.setVisibility(View.GONE);
								layhuo.setBackgroundResource(R.color.white);
							}else{
								chengzhanghuoban.setVisibility(View.GONE);
								ishuoban.setVisibility(View.GONE);
								chengzhanghuobanline.setVisibility(View.VISIBLE);
								layhuo.setBackgroundResource(R.mipmap.icon_qiyuhuobang);
							}

						}else{
							qiyehuoban_id.setVisibility(View.GONE);
							qiyehuoban_line.setVisibility(View.GONE);
						}
					}
				}

			}
				if(msg.what == 13){
					String ret =(String) msg.obj;
					Gson gs = new Gson();
					QiYeEntiry qiye = gs.fromJson(ret, QiYeEntiry.class);
					if(qiye != null){
						if(qiye.code.equals("1")){
							SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ID,qiye.data.id);
							SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ENTERPRISE,qiye.data.enterprise_name);
							SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_TELEPHONE, qiye.data.telephone);
							SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_SIGN_TIME, qiye.data.sign_time);
							SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_REGION_NAME, qiye.data.region_name);
							SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ENAME, qiye.data.ename);
							SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_IS_MEMBER, qiye.data.is_member);
							String is_member = SharedPreferencesUtil.getString(SharedPreferencesUtil.HUIYUAN_IS_MEMBER,"");
							if(is_member.equals("1")){
								chuangxin_id.setVisibility(View.VISIBLE);
								chuangxin_line.setVisibility(View.VISIBLE);
								huiyuandanwei.setVisibility(View.VISIBLE);
								my_tian.setText("会员有效期至："+qiye.data.sign_time);
								compan.setText(qiye.data.enterprise_name);
							}else{
								chuangxin_id.setVisibility(View.GONE);
								chuangxin_line.setVisibility(View.GONE);
								huiyuandanwei.setVisibility(View.GONE);
							}
						}
					}
				}
			}catch (Exception e){

			}
		}
	};
	class Receiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("action_update_conut")) {
				Background_login();
			}else if(intent.getAction().equals("action_show_chuangxin")){
				chuangxin_id.setVisibility(View.VISIBLE);
				chuangxin_line.setVisibility(View.VISIBLE);
				huiyuandanwei.setVisibility(View.VISIBLE);
			}else if(intent.getAction().equals("action_hide_chuangxin")){
				chuangxin_id.setVisibility(View.GONE);
				chuangxin_line.setVisibility(View.GONE);
				huiyuandanwei.setVisibility(View.GONE);
			}
		}
	}



//	private UMShareListener umShareListener = new UMShareListener() {
//		@Override
//		public void onResult(SHARE_MEDIA platform) {
//			if (platform.name().equals("WEIXIN_FAVORITE")) {
//			} else {
//			}
//		}
//
//		@Override
//		public void onError(SHARE_MEDIA platform, Throwable t) {
//			Toast.makeText(getActivity(), platform + "分享失败", Toast.LENGTH_SHORT).show();
//			if (t != null) {
//			}
//		}
//
//		@Override
//		public void onCancel(SHARE_MEDIA platform) {
//			//Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
//		}
//	};

//	private void shareonclick() {
//		try {
////			UIHelper.showDialogForLoading(QingBaoDeilActivity.this, "", true);
//
//			image = new UMImage(getActivity(), "http://"+ips+"/uploads/logo/logo.png");
//
//
//			final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//					{
//							SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//							SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//					};
//			action = new ShareAction(getActivity());
//
//			action.setPlatform(SHARE_MEDIA.QQ)
//					.withText("助力企业解决技术难题，促进院企科技成果转移")
//					.withTitle("钛领科技，让科技变简单")
//					.withTargetUrl("http://www.zhongkechuangxiang.com/plus/maidianm/#page1")
//					.withMedia(image)
//					.setCallback(umShareListener)
//					.share();
////			action.setDisplayList(displaylist)
////					.withText("助力企业解决技术难题，促进院企科技成果转移")
////					.withTitle("钛领科技，让科技变简单")
////					.withTargetUrl("http://www.zhongkechuangxiang.com/plus/maidianm/#page1")
////					.withMedia(image)
////					.setCallback(umShareListener)
////					.open();
//
//		} catch (Exception e) {
//		}
//	}
	private void qiyejson(){
		String url = "http://erp.zhongkechuangxiang.com/api/Product/MPList";
		HashMap<String, String > map = new HashMap<>();
		map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, ""));
		NetworkCom networkCom = NetworkCom.getNetworkCom();
		networkCom.getJson(url,map,handler1,13,0);
	}

}
