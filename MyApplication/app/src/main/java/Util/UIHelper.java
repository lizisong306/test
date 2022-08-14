package Util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maidiantech.R;
import com.umeng.socialize.Config;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * @author AA
 * @Date 2014-11-23
 */
public class UIHelper {

	/** 加载数据对话框 */
	private static Dialog mLoadingDialog;
	private static AnimationDrawable anim;
	static TextView loadingText;
	
	/**
	 *
	 */
	public static void showDialogForLoading(Activity context, String msg, boolean cancelable) {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
		 loadingText = (TextView)view.findViewById(R.id.id_tv_loading_dialog_text);
		if(msg != null && !msg.equals("")){
			loadingText.setVisibility(View.VISIBLE);
			loadingText.setText(msg);
		}else{
			loadingText.setVisibility(View.GONE);
		}
//		ImageView ivAdam=(ImageView) view.findViewById(R.id.log_img);
//		 anim = (AnimationDrawable)ivAdam.getBackground();// 获取到动画资源
//		anim.setOneShot(true); // 设置是否重复播放



//		if(mLoadingDialog != null && mLoadingDialog.isShowing()){
//			mLoadingDialog.dismiss();
//		}
//

		mLoadingDialog = new Dialog(context, R.style.MyDialogStyle);
//		Config.dialog=mLoadingDialog;
////
//		mLoadingDialog.setCancelable(cancelable);
		mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mLoadingDialog.show();
//		anim.start();// 开始动画
	}

	public static void setLoadingText(String msg){
		if(loadingText != null){
			if(msg != null && !msg.equals("")){
				loadingText.setVisibility(View.VISIBLE);
				loadingText.setText(msg);
			}else{
				loadingText.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 关闭加载对话框
	 */
	public static void hideDialogForLoading() {
//		if(anim.isRunning())//是否正在运行？
//		{
//			anim.stop();//停止
//		}
		if(mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.cancel();
		}
	}

//
//
//	//设置动画背景
//	_imageView1.setBackgroundResource(R.anim.animation_list);//其中R.anim.animation_list就是上一步准备的动画描述文件的资源名
//	//获得动画对象
//	_animaition = (AnimationDrawable)_imageView1.getBackground();
//	最后，就可以启动动画了，代码如下：
//			//是否仅仅启动一次？
//			_animaition.setOneShot(false);
//	if(_animaition.isRunning())//是否正在运行？
//	{
//		_animaition.stop();//停止
//	}
//	_animaition.start();//启动


}
