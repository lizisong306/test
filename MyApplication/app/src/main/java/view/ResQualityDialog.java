package view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.maidiantech.R;

/**
 * Created by lizisong on 2016/12/21.
 */

public class ResQualityDialog extends Dialog {
    private ResQualityChoseListener mChoseListener;

    private TextView mAutoText;
    private TextView mHighText;
    private TextView mLowText;
    private TextView mText;
    private TextView mTitleText;

    private TextView mTempSelectText;
    private Activity mContext;

    private String AutoText;
    private String HighText;
    private String LowText;
    private String title;
    private TextView view;

    public ResQualityDialog(Activity context, String title1, String title2, String title3,String title4) {
        super(context, R.style.dialog);
        mContext = context;
        getWindow().setWindowAnimations(R.style.dialogBootom2UpAnimation);
        getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        setContentView(R.layout.moreinfo_resquality_dialog_layout);

        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (mContext.getWindowManager().getDefaultDisplay().getWidth());
        getWindow().setAttributes(p);
        AutoText = title1;
        HighText = title2;
        LowText = title3;


        this.title = title4;
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.c_ResModeAuto:
                        if (mChoseListener != null) {
                            mChoseListener.choseModeAuto(AutoText);

                        }
                        break;
                    case R.id.c_ResModeHigh:
                        if (mChoseListener != null) {
                            mChoseListener.choseModeHigh(HighText);
                        }
                        break;
                    case R.id.c_ResModeLow:
                        if (mChoseListener != null) {
                            mChoseListener.choseModeLow(LowText);
                        }
                        break;
                    case R.id.c_ResModefour:
                        if(mChoseListener != null){
                            mChoseListener.choseTitle(title);
                        }
                        break;
                    default:
                        break;
                }
                if (getWindow() != null) {
                    dismiss();
                }
            }
        };
        mAutoText = (TextView) findViewById(R.id.c_ResModeAuto);
        mAutoText.setText(AutoText);
        mAutoText.setOnClickListener(clickListener);
        mHighText = (TextView) findViewById(R.id.c_ResModeHigh);
        mHighText.setText(HighText);
        mHighText.setOnClickListener(clickListener);
        mLowText = (TextView) findViewById(R.id.c_ResModeLow);
        mLowText.setText(LowText);
        mLowText.setOnClickListener(clickListener);
        mText = (TextView)findViewById(R.id.c_ResModefour);
        if(title4 == null){
            mText.setVisibility(View.GONE);
        }
        mText.setText(title);
        mText.setOnClickListener(clickListener);

        mTitleText = (TextView) findViewById(R.id.title);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void setTitleText(String text) {
        mTitleText.setText(text);
    }

    public void setResQualityChoseListener(ResQualityChoseListener listener) {
        this.mChoseListener = listener;
    }

}