package view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.View.OnClickListener;
import com.maidiantech.R;

/**
 * Created by lizisong on 2017/11/29.
 */

public class UpgradeAlertDialog implements View.OnClickListener{
    private Context mContext;

    private Dialog mDialog;


    private View.OnClickListener mPositiveButtonClickListener;
    private View.OnClickListener mNegativeButtonClickListener;
    private View.OnClickListener mNeutralButtonClickListener;

    private Object mTarObj;
    private int mWidth = 0;
    TextView title,content1,content2,content3,content4,content5;
    ImageView lijigengxin,close;
    public UpgradeAlertDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext, R.style.dialog);
        Window window = mDialog.getWindow();
        if (null != window) {
            window.setWindowAnimations(R.style.dialog_zoom_in_zoom_out);
        }
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        mWidth = wm.getDefaultDisplay().getWidth();

        mDialog.setContentView(R.layout.upgradealertdialog);

        if (null != window) {
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_width);
            int maxWidth = mWidth - 60;
            if (width > maxWidth) {
                width = maxWidth;
            }
            lp.width = width;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

//        mTitleTextView = (TextView) mDialog.findViewById(R.id.title);
//        mTitleTextView1 = (TextView) mDialog.findViewById(R.id.title1);
//        mMessageTextView = (TextView) mDialog.findViewById(R.id.message);
//        mContainer = (RelativeLayout) mDialog.findViewById(R.id.container);
//
//        mButtonLayout = (LinearLayout) mDialog.findViewById(R.id.button_layout);
//        mPositiveButton = (TextView) mDialog.findViewById(R.id.positive_button);
//        mNegativeButton = (TextView) mDialog.findViewById(R.id.negative_button);
//        mNeutralButton = (TextView) mDialog.findViewById(R.id.neutral_button);
//
//        mDivider0 = mDialog.findViewById(R.id.divider_0);
//        mDivider1 = mDialog.findViewById(R.id.divider_1);
//        mDivider2 = mDialog.findViewById(R.id.divider_2);
        close = (ImageView) mDialog.findViewById(R.id.close);
        title =(TextView)mDialog.findViewById(R.id.title);
        content1 = (TextView)mDialog.findViewById(R.id.content1);
        content2 = (TextView)mDialog.findViewById(R.id.content2);
        content3 = (TextView)mDialog.findViewById(R.id.content3);
        content4 = (TextView)mDialog.findViewById(R.id.content4);
        content5 = (TextView)mDialog.findViewById(R.id.content5);
        lijigengxin = (ImageView)mDialog.findViewById(R.id.lijigengxin);
        content1.setVisibility(View.GONE);
        content2.setVisibility(View.GONE);
        content3.setVisibility(View.GONE);
        content4.setVisibility(View.GONE);
        content5.setVisibility(View.GONE);
    }

    public Context getContext() {
        return mContext;
    }

    public void setObject(Object iObject) {
        mTarObj = iObject;
    }

    public Object getObject() {
        return mTarObj;
    }


    public void setPositiveButton(int textId, final View.OnClickListener listener) {
        setPositiveButton(mContext.getText(textId), listener);
    }

    public void setTitle(String txt){
        title.setText(txt);
    }

    public void setContext(String txt){
        if(txt != null){
           String[] arry = txt.split("\n");
           if(arry != null){
              for(int i=0;i<arry.length;i++){
                   String str = arry[i];
                   if(i == 0){
                       if(str != null && !str.equals("")){
                           content1.setVisibility(View.VISIBLE);
                           content1.setText(str);
                       }
                   }else if(i == 1){
                       if(str != null && !str.equals("")){
                           content2.setVisibility(View.VISIBLE);
                           content2.setText(str);
                       }
                   }else if(i == 2){
                       if(str != null && !str.equals("")){
                           content3.setVisibility(View.VISIBLE);
                           content3.setText(str);
                       }
                   }else if(i == 3){
                       if(str != null && !str.equals("")){
                           content4.setVisibility(View.VISIBLE);
                           content4.setText(str);
                       }
                   }else if(i == 4){
                       if(str != null && !str.equals("")){
                           content5.setVisibility(View.VISIBLE);
                           content5.setText(str);
                       }
                   }
              }
           }
        }
    }

    public void setPositiveButton(CharSequence text, final View.OnClickListener listener) {
        mPositiveButtonClickListener = listener;
        lijigengxin.setOnClickListener(this);
    }

    public void setNegativeButton(int textId, final View.OnClickListener listener) {
        setNegativeButton(mContext.getText(textId), listener);
    }

    public void setNegativeButton(CharSequence text, final View.OnClickListener listener) {

        close.setOnClickListener(this);
        mNegativeButtonClickListener = listener;

    }

    public void setNeutralButton(int textId, final View.OnClickListener listener) {
        setNeutralButton(mContext.getText(textId), listener);
    }

    public void setNeutralButton(CharSequence text, final View.OnClickListener listener) {


        close.setOnClickListener(this);
        mNeutralButtonClickListener = listener;

    }

    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    public void setCanceledOnTouchOutside(boolean cancelable) {
        mDialog.setCanceledOnTouchOutside(cancelable);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        mDialog.setOnCancelListener(onCancelListener);
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        mDialog.setOnKeyListener(onKeyListener);
    }

    public void setView(View view) {
//        mContainer.addView(view);
//        mContainer.setVisibility(View.VISIBLE);
//        mMessageTextView.setVisibility(View.GONE);
    }

    public void show() {
        try {
            mDialog.show();
        } catch (Exception e) {
        }
    }

    public boolean isShowing() {
        try {
            return mDialog.isShowing();
        } catch (Exception e) {
        }
        return false;
    }

    public void dismiss() {
        try {
            mDialog.dismiss();
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                if (mNegativeButtonClickListener != null) {
                    mNegativeButtonClickListener.onClick(v);
                }
                dismiss();
                break;
            case R.id.lijigengxin:
                if (mPositiveButtonClickListener != null) {
                    mPositiveButtonClickListener.onClick(v);
                }
                dismiss();
                break;
//            case R.id.neutral_button:
//                if (mNeutralButtonClickListener != null) {
//                    mNeutralButtonClickListener.onClick(v);
//                }
//                dismiss();
//                break;
            default:
                dismiss();
                break;
        }
    }
}
