package receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.maidiantech.DetailsActivity;
import com.maidiantech.EarlyRef;
import com.maidiantech.LookQingBaoActivity;
import com.maidiantech.MessageActivity;
import com.maidiantech.MyXuqiuActivity;
import com.maidiantech.MyloginActivity;
import com.maidiantech.QingBaoActivity;
import com.maidiantech.QingBaoDeilActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.ZixunDetailsActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import Util.SharedPreferencesUtil;
import application.MyApplication;

public class MessageReceiver extends XGPushBaseReceiver {
	private Intent intent = new Intent("com.maidiantech.WebViewActivity");
	public static final String LogTag = "lizisong";

	private void show(Context context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
//		show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());

		//context.sendBroadcast(intent);
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		/*if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);*/

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		Log.d("lizisong", "context:"+context);
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			text = "通知被打开 :" + message;
			title= message.getContent();
//			Log.d("lizisong", "message.getCustomContent():"+message.getCustomContent());
			parseCustomContent(context,message.getCustomContent());
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
		/*Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
				Toast.LENGTH_SHORT).show();*/
		// 获取自定义key-value


		// APP自主处理的过程。。。


//		parseContent(context, message.getActivityName());
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理消息的过程...
		Log.d(LogTag, text);
		show(context, text);
	}
	String url;
	String aid;
	String title;

	private void parseCustomContent(Context context,String text){
		try{

			if (text != null && text.length() != 0) {
				try {
					JSONObject obj = new JSONObject(text);
					String type = obj.getString("type");
					if(type != null && !type.equals("")){
						if(type.equals("other")){
//							url = obj.getString("url");
							Message msg = Message.obtain();
							msg.what=7;
							msg.obj = url;
							myHandler.sendMessageDelayed(msg,3000);
						}else if(type.equals("referplan")){
							aid = obj.getString("content");
							Message msg = Message.obtain();
							msg.what=2;
							msg.obj = url;
							myHandler.sendMessageDelayed(msg,3000);
						}else if(type.equals("infor")){
//							aid = obj.getString("content");
							Message msg = Message.obtain();
							msg.what=3;
							myHandler.sendMessageDelayed(msg,3000);
						}/*else if(type.equals("telig")){
							Message msg = Message.obtain();
							msg.what=4;
							myHandler.sendMessageDelayed(msg,3000);
						}*/else if(type.equals("news")){
							String category ,content="";

							category =  obj.getString("category");
							Message msg = Message.obtain();

							if(category.equals("1")){
							    aid = obj.getString("typeid");
								content =  obj.getString("aid");
                                msg.arg1 = 1;
							}else if(category.equals("0")){
							   content =  obj.getString("content");
							   msg.arg1 = 0;
							}
							msg.what=5;
							msg.obj = content;
							myHandler.sendMessageDelayed(msg,3000);
						}else if(type.equals("telig")){
							aid = obj.getString("content");
							Message msg = Message.obtain();
							msg.what=6;
							myHandler.sendMessageDelayed(msg,3000);
						}else{
							url = obj.getString("url");
							Message msg = Message.obtain();
							msg.what=1;
							msg.obj = url;
							myHandler.sendMessageDelayed(msg,3000);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}catch (Exception e){

		}
	}

	private void parseContent(Context context,String text){
		try {
			String txt = "";
			if(text != null){
				txt = text.substring(6,text.length());
				String[] type = txt.split("/");
				String mdType = type[0];
				String mdSecType = type[1];
				String mdId = type[2];
				if(mdType.equals("doc")){
					if(mdSecType.equals("zixun")){
						Intent intent = new Intent(MyApplication.context, ZixunDetailsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", mdId);
						intent.putExtra("name","资讯");
						//intent.putExtra("pic", postsListData.get(position-2).getLitpic());
						context.startActivity(intent);

					}else if(mdSecType.equals("xiangmu")){
						Intent intent = new Intent(MyApplication.context, DetailsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", mdId);
						intent.putExtra("name","项目");
						//intent.putExtra("pic", postsListData.get(position-2).getLitpic());
						context.startActivity(intent);

					}else if(mdSecType.equals("zhengce")){
						Intent intent = new Intent(MyApplication.context, DetailsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", mdId);
						intent.putExtra("name","政策");
						//intent.putExtra("pic", postsListData.get(position-2).getLitpic());
						context.startActivity(intent);

					}else if(mdSecType.equals("rencai")){
						Log.d("lizisong", "跳转到人才");
						Intent intent = new Intent(MyApplication.context, DetailsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", mdId);
						intent.putExtra("name","人才");
						//intent.putExtra("pic", postsListData.get(position-2).getLitpic());
						context.startActivity(intent);
						Log.d("lizisong", "跳转到人才2");

					}else if(mdSecType.equals("shebei")){
						Intent intent = new Intent(MyApplication.context, DetailsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", mdId);
						intent.putExtra("name","设备");
						//intent.putExtra("pic", postsListData.get(position-2).getLitpic());
						context.startActivity(intent);

					}else if(mdSecType.equals("shiyanshi")){
						Intent intent = new Intent(MyApplication.context, DetailsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", mdId);
						intent.putExtra("name","实验室");
						//intent.putExtra("pic", postsListData.get(position-2).getLitpic());
						context.startActivity(intent);

					}else if(mdSecType.equals("zhuanli")){
						Intent intent = new Intent(MyApplication.context, DetailsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", mdId);
						intent.putExtra("name","专利");
						//intent.putExtra("pic", postsListData.get(position-2).getLitpic());
						context.startActivity(intent);

					}
				}
			}
		}catch (Exception e){

		}
	}
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if(msg.what == 1){
				Intent intent = new Intent(MyApplication.context, WebViewActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("title", "推荐");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				MyApplication.context.startActivity(intent);
			}else if(msg.what == 2){
				Intent intent = new Intent(MyApplication.context, EarlyRef.class);
				intent.putExtra("id", aid);
				intent.putExtra("title", "科讯早参");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				MyApplication.context.startActivity(intent);
			}else if(msg.what == 3){
				String  state = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE,"0");
				if(state.equals("1")){
					Intent intent = new Intent(MyApplication.context, MessageActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					MyApplication.context.startActivity(intent);
				}else{
					Toast.makeText(MyApplication.context,"登录以后才能跳转到消息界面",Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == 4){
				Intent intent = new Intent(MyApplication.context, QingBaoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				MyApplication.context.startActivity(intent);
			}else if(msg.what == 5){
				String content= (String)msg.obj;
				if(msg.arg1 == 1){
					Intent intent = new Intent(MyApplication.context, DetailsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					intent.putExtra("id", content);
					if(aid.equals("1")){
						intent.putExtra("name", "资讯");
					}else if(aid.equals("2")){
						intent.putExtra("name", "项目");
					}else if(aid.equals("6")){
						intent.putExtra("name", "政策");
					}else if(aid.equals("8")){
						intent.putExtra("name", "研究所");
					}else if(aid.equals("4")){
						intent.putExtra("name", "专家");
					}else if(aid.equals("5")){
						intent.putExtra("name", "专利");
					}else if(aid.equals("7")){
						intent.putExtra("name", "设备");
					}
					MyApplication.context.startActivity(intent);
				}else if(msg.arg1==0){
					Intent intent = new Intent(MyApplication.context,  WebViewActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					intent.putExtra("url", content);
					intent.putExtra("title", title);
					MyApplication.context.startActivity(intent);
				}

			}else if(msg.what == 6){
				Intent intent = new Intent(MyApplication.context,LookQingBaoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				intent.putExtra("teligjournalid",aid);
				MyApplication.context.startActivity(intent);
			}else if(msg.what == 7){
				String state = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
				if(state.equals("1")){
					Intent intent = new Intent(MyApplication.context, MyXuqiuActivity.class);
					intent.putExtra("type","-1");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					MyApplication.context.startActivity(intent);
				}else{
					Intent intent = new Intent(MyApplication.context,MyloginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					MyApplication.context.startActivity(intent);
				}
			}
		}
	};

}
