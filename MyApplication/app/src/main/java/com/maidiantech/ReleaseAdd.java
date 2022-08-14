package com.maidiantech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import Util.AppUtils;
import Util.GetPath;
import entity.Releasebean;
import view.StyleUtils;
import view.StyleUtils1;
import view.T;

/**
 * Created by 13520 on 2016/9/14.
 */
public class ReleaseAdd extends AutoLayoutActivity {
    private static final String PHOTO_FILE_NAME = "xiangji";
    private  List<Releasebean> list;
    private Bitmap bitmap;
    private String introduction;
    private  String realease;
    private  String trim;
    private ImageView head_imgs;
    private PopupWindow mPopBottom;
    private TextView realease_linyu;
    private EditText my_release_name;
    private  EditText realease_introduction;
    private  String imageString;
    private String path;
    private  int i=0;
    private String [] releaseArray={"电子信息领域", "新材料及精细化工", "节能环保领域", "生物医药领域", "先进制造领域", "新能源领域", "其他"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_add);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
         head_imgs=(ImageView) findViewById(R.id.head_imgs);
        TextView  release_submit=(TextView) findViewById(R.id.release_submit);
         LinearLayout my_lingyu=(LinearLayout) findViewById(R.id.my_lingyu);
          my_release_name=(EditText) findViewById(R.id.my_release_name);
        LinearLayout release_layout=(LinearLayout) findViewById(R.id.release_layout);
        release_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        my_release_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                trim= my_release_name.getText().toString().trim();
            }
        });
         realease_introduction=(EditText) findViewById(R.id.realease_introduction);
        realease_introduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                introduction=realease_introduction.getText().toString().trim();
            }
        });
          realease_linyu=(TextView) findViewById(R.id.realease_linyu);
        my_lingyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ReleaseAdd.this)
                        .setItems(releaseArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                realease_linyu.setText(releaseArray[which]);
                                 realease=realease_linyu.getText().toString().trim();
                                Toast.makeText(ReleaseAdd.this, realease, Toast.LENGTH_SHORT).show();
                            }
                        })

                	.show();
            }
        });
        head_imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // showFullPop();
            }
        });
        release_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(trim!=null&realease!=null&introduction!=null){
                   ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
                    byte[] byteArray=byteArrayOutputStream.toByteArray();
                    imageString=new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
                    SharedPreferences spf=getSharedPreferences("list"+i,MODE_PRIVATE);
                    SharedPreferences.Editor edt = spf.edit();
                    edt.putString("trim",trim);
                    edt.putString("realease",realease);
                    edt.putString("introduction",introduction);
                    edt.putString("imageString",imageString);
                    edt.putBoolean("flag", true);
                    edt.commit();
                   /* Releasebean re= new Releasebean(trim,realease,introduction,path);
                    Collectionss c=new Collectionss(ReleaseAdd.this);
                    c.collect_add(re);*/
                    //Toast.makeText(ReleaseAdd.this, re.toString(), Toast.LENGTH_SHORT).show();
                //  setResult(2,getIntent());
                 //   ReleaseAdd.this.finish();
                }
            }
        });
    }
    private void showFullPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_full,
                null);
        LinearLayout layout_all;
        RelativeLayout layout_choose;
        RelativeLayout layout_photo;
        RelativeLayout layout_cancel;
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_cancel=(RelativeLayout) view.findViewById(R.id.layout_cancel);
        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                T.showLong(ReleaseAdd.this,"点击拍照");
                // 当点击照相按钮 打开照相机 拍照 照片回传
                // 打开照相机的意图
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (AppUtils.hasSdcard()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                }
                // 开启意图 ，设置请求码
                startActivityForResult(intent, 10001);
                mPopBottom.dismiss();
            }
        });
        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 定义打开相册的意图
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 10002);
                T.showLong(ReleaseAdd.this,"点击相册");
                mPopBottom.dismiss();
            }
        });
        layout_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                T.showLong(ReleaseAdd.this,"点击取消");
                mPopBottom.dismiss();
            }
        });
        mPopBottom = new PopupWindow(view);
        mPopBottom.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setTouchable(true);
        mPopBottom.setFocusable(true);
        mPopBottom.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        mPopBottom.setBackgroundDrawable(dw);
        // 动画效果 从底部弹起
        mPopBottom.setAnimationStyle(R.style.Animations_GrowFromBottom);
        mPopBottom.showAtLocation(head_imgs, Gravity.BOTTOM, 0, 0);//parent view随意
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            // 获取一下照相的图片 Serializable
            if (AppUtils.hasSdcard()) {
                File     tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_FILE_NAME);
                Uri   uri = Uri.fromFile(tempFile);
                 path = tempFile.getPath();
                 bitmap = getimage(path);
                head_imgs.setImageBitmap(bitmap);
               // crop(uri);
            } else {
                Toast.makeText(ReleaseAdd.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }
          /*  bitmap = data.getParcelableExtra("data");
            head_imgs.setImageBitmap(bitmap);*/
        } else {
            if (requestCode == 10002) {
                Uri uri = data.getData();
                path = GetPath.getPath(ReleaseAdd.this, uri);
                // 设置图片
                // imageView.setImageURI(uri);
                crop(uri);
            } else {
                if (requestCode == 10003) {
                    // 裁剪后返回的图片
                  // bitmap = data.getParcelableExtra("data");
                    bitmap = getimage(path);
                    head_imgs.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 是否裁剪 true 是
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", false);// 取消人脸识别
        // 是否返回值 返回
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, 10003);
    }
    //按比例压缩
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
    //质量压缩方法
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        System.out.println("开始质量压缩原来图片大小kb=="+baos.toByteArray().length/1024);
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        System.out.println("结束质量图片的大小kb=="+baos.toByteArray().length/1024);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}


