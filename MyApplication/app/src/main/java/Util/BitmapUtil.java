package Util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

import com.maidiantech.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import dao.Service.MainDianIcon;

/**
 * @author yanzi
 *����Bitmap��byte[] Drawable����ת��
 */
public class BitmapUtil {
	
	/**
	 * @param drawable
	 * drawable ת  Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// ȡ drawable �ĳ���
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// ȡ drawable ����ɫ��ʽ
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565;
		// ������Ӧ bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// ������Ӧ bitmap �Ļ���
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// �� drawable ���ݻ���������
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * @param bitmap
	 * @param roundPx
	 * ��ȡԲ��ͼƬ
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


	public static void upUserImageData(ImageView mImvHead, MainDianIcon mainDianIcon) {// 到数据库中读取头像图片不是通过网络去下载图片
		Bitmap bitmap = null;
		String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
//		bitmap = mainDianIcon.get(mid);
//		String url1 =SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
//		if(url1 != null && !url1.equals("0") && !url1.equals("")){
//			ImageLoader.getInstance().displayImage(url1, mImvHead);
//			return;
//		}
		if(bitmap == null){
			String url =SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
			if(url != null && !url.equals("0") && !url.equals("")){
				ImageLoader.getInstance().displayImage(url, mImvHead);
			}else{
				mImvHead.setImageResource(R.mipmap.head_1);
			}

		}else{
			Drawable db =	getRoundedCornerBitmap(bitmap, 360.0f, 0);
			BitmapDrawable bd = (BitmapDrawable) db;
			if (bd == null) {
				return;
			}
			Bitmap bm = bd.getBitmap();
			if (bm != null) {
				mImvHead.setImageBitmap(bm);
			} else {
				mImvHead.setImageResource(R.mipmap.head_1);
			}

		}

	}

	public static  Drawable getRoundedCornerBitmap(Bitmap bitmap, float roundPx, int strokeWidth){
		if(bitmap != null){
			Bitmap output = Bitmap.createBitmap(
					bitmap.getWidth() + strokeWidth, bitmap.getHeight()
							+ strokeWidth, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(strokeWidth, strokeWidth,
					bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return new BitmapDrawable(output);
		}else{
			return null;
		}
	}

	/**
	 * 图片圆角
	 *
	 * @param
	 * @return
	 */

	@SuppressWarnings("deprecation")
	public static Drawable getRoundedCornerBitmap(String path, float roundPx,
												  int strokeWidth) {
		Bitmap bitmap = getBitmapWithPath(path);
		if (bitmap != null) {
			Bitmap output = Bitmap.createBitmap(
					bitmap.getWidth() + strokeWidth, bitmap.getHeight()
							+ strokeWidth, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(strokeWidth, strokeWidth,
					bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return new BitmapDrawable(output);
		} else {
			return null;
		}
	}
	public static Bitmap getBitmapWithPath(String path) {
		final File coverFile = new File(path);
		if (null == coverFile || !coverFile.exists()) {
			return null;
		}
		Bitmap bmp = null;
		FileInputStream fis = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPurgeable = true;
		try {
			fis = new FileInputStream(coverFile);
			bmp = BitmapFactory.decodeStream(fis, null, opts);
		} catch (IOException e) {
		} finally {
			try {
				if (null != fis) {
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
//				Ld.e("Utility", "decodeWithOptions error : " + e.toString());
			}

		}
		return bmp;
	}
}
