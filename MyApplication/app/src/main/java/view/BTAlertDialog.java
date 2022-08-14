package view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.View.OnClickListener;
import com.maidiantech.R;

/**
 * Created by lizisong on 2017/1/4.
 */

public class BTAlertDialog implements View.OnClickListener {
    private Context mContext;

    private Dialog mDialog;
    private TextView mTitleTextView;
    private TextView mTitleTextView1;
    private TextView mMessageTextView;
    private RelativeLayout mContainer;
    private LinearLayout mButtonLayout;

    private TextView mPositiveButton;
    private TextView mNegativeButton;
    private TextView mNeutralButton;

    private View mDivider0;
    private View mDivider1;
    private View mDivider2;

    private View.OnClickListener mPositiveButtonClickListener;
    private View.OnClickListener mNegativeButtonClickListener;
    private View.OnClickListener mNeutralButtonClickListener;

    private Object mTarObj;
    private int mWidth = 0;

    public BTAlertDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext, R.style.dialog);
        Window window = mDialog.getWindow();
        if (null != window) {
            window.setWindowAnimations(R.style.dialog_zoom_in_zoom_out);
        }
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        mWidth = wm.getDefaultDisplay().getWidth();

        mDialog.setContentView(R.layout.alert_dialog);

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

        mTitleTextView = (TextView) mDialog.findViewById(R.id.title);
        mTitleTextView1 = (TextView) mDialog.findViewById(R.id.title1);
        mMessageTextView = (TextView) mDialog.findViewById(R.id.message);
        mContainer = (RelativeLayout) mDialog.findViewById(R.id.container);

        mButtonLayout = (LinearLayout) mDialog.findViewById(R.id.button_layout);
        mPositiveButton = (TextView) mDialog.findViewById(R.id.positive_button);
        mNegativeButton = (TextView) mDialog.findViewById(R.id.negative_button);
        mNeutralButton = (TextView) mDialog.findViewById(R.id.neutral_button);

        mDivider0 = mDialog.findViewById(R.id.divider_0);
        mDivider1 = mDialog.findViewById(R.id.divider_1);
        mDivider2 = mDialog.findViewById(R.id.divider_2);
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

    public void setTitle1(int titleId) {
        mTitleTextView1.setText(titleId);
        mTitleTextView1.setVisibility(View.VISIBLE);
    }

    public void setTitle1(CharSequence title) {
        mTitleTextView1.setText(title);
        mTitleTextView1.setVisibility(View.VISIBLE);
    }
    public void setTitle(int titleId) {
        mTitleTextView.setText(titleId);
    }

    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }

    public void setMessage(int messageId) {
        mMessageTextView.setVisibility(View.VISIBLE);
        mMessageTextView.setText(messageId);
//        mContainer.setVisibility(View.GONE);
//        mContainer.removeAllViews();
    }

    public void setMessage(CharSequence message) {
        mMessageTextView.setVisibility(View.VISIBLE);
        mMessageTextView.setText(message);
//        mContainer.setVisibility(View.GONE);
//        mContainer.removeAllViews();
    }

    public void setPositiveButton(int textId, final OnClickListener listener) {
        setPositiveButton(mContext.getText(textId), listener);
    }

    public void setPositiveButton(CharSequence text, final OnClickListener listener) {
        mButtonLayout.setVisibility(View.VISIBLE);
        mDivider0.setVisibility(View.VISIBLE);
        mPositiveButton.setText(text);
        mPositiveButton.setOnClickListener(this);
        mPositiveButtonClickListener = listener;
        mPositiveButton.setVisibility(View.VISIBLE);
        if (mNegativeButton.getVisibility() == View.VISIBLE) {
            mDivider1.setVisibility(View.VISIBLE);
        } else {
            mDivider1.setVisibility(View.GONE);
        }
        if (mNeutralButton.getVisibility() == View.VISIBLE) {
            mDivider2.setVisibility(View.VISIBLE);
        } else {
            mDivider2.setVisibility(View.GONE);
        }
    }

    public void setNegativeButton(int textId, final OnClickListener listener) {
        setNegativeButton(mContext.getText(textId), listener);
    }

    public void setNegativeButton(CharSequence text, final OnClickListener listener) {
        mButtonLayout.setVisibility(View.VISIBLE);
        mDivider0.setVisibility(View.VISIBLE);
        mNegativeButton.setText(text);
        mNegativeButton.setOnClickListener(this);
        mNegativeButtonClickListener = listener;
        mNegativeButton.setVisibility(View.VISIBLE);
        if (mPositiveButton.getVisibility() == View.VISIBLE) {
            mDivider2.setVisibility(View.VISIBLE);
        } else {
            mDivider2.setVisibility(View.GONE);
        }
        if (mNeutralButton.getVisibility() == View.VISIBLE) {
            mDivider1.setVisibility(View.VISIBLE);
        } else {
            mDivider1.setVisibility(View.GONE);
        }
    }

    public void setNeutralButton(int textId, final OnClickListener listener) {
        setNeutralButton(mContext.getText(textId), listener);
    }

    public void setNeutralButton(CharSequence text, final OnClickListener listener) {
        mButtonLayout.setVisibility(View.VISIBLE);
        mDivider0.setVisibility(View.VISIBLE);
        mNeutralButton.setText(text);
        mNeutralButton.setOnClickListener(this);
        mNeutralButtonClickListener = listener;
        mNeutralButton.setVisibility(View.VISIBLE);
        if (mPositiveButton.getVisibility() == View.VISIBLE) {
            mDivider2.setVisibility(View.VISIBLE);
        } else {
            mDivider2.setVisibility(View.GONE);
        }
        if (mNegativeButton.getVisibility() == View.VISIBLE) {
            mDivider1.setVisibility(View.VISIBLE);
        } else {
            mDivider1.setVisibility(View.GONE);
        }
    }

    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    public void setCanceledOnTouchOutside(boolean cancelable) {
        mDialog.setCanceledOnTouchOutside(cancelable);
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        mDialog.setOnCancelListener(onCancelListener);
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        mDialog.setOnKeyListener(onKeyListener);
    }

    public void setView(View view) {
        mContainer.addView(view);
        mContainer.setVisibility(View.VISIBLE);
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
            case R.id.positive_button:
                if (mPositiveButtonClickListener != null) {
                    mPositiveButtonClickListener.onClick(v);
                }
                dismiss();
                break;
            case R.id.negative_button:
                if (mNegativeButtonClickListener != null) {
                    mNegativeButtonClickListener.onClick(v);
                }
                dismiss();
                break;
            case R.id.neutral_button:
                if (mNeutralButtonClickListener != null) {
                    mNeutralButtonClickListener.onClick(v);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

}
