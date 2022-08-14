package view;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast缁熶竴绠＄悊绫� */
public class T
{
	private static Toast toast;

	/**
	 * 鐭椂闂存樉绀篢oast
	 */
	public static void showShort(Context context, CharSequence message)
	{
		if (null == toast)
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}
		else
		{
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 鐭椂闂存樉绀篢oast
	 */
	public static void showShort(Context context, int message)
	{
		if (null == toast)
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}
		else
		{
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 闀挎椂闂存樉绀篢oast
	 */
	public static void showLong(Context context, CharSequence message)
	{
		if (null == toast)
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}
		else
		{
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 闀挎椂闂存樉绀篢oast
	 */
	public static void showLong(Context context, int message)
	{
		if (null == toast)
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}
		else
		{
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 鑷畾涔夋樉绀篢oast鏃堕棿
	 */
	public static void show(Context context, CharSequence message, int duration)
	{
		if (null == toast)
		{
			toast = Toast.makeText(context, message, duration);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}
		else
		{
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 鑷畾涔夋樉绀篢oast鏃堕棿
	 */
	public static void show(Context context, int message, int duration)
	{
		if (null == toast)
		{
			toast = Toast.makeText(context, message, duration);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}
		else
		{
			toast.setText(message);
		}
		toast.show();
	}

	/** Hide the toast, if any. */
	public static void hideToast()
	{
		if (null != toast)
		{
			toast.cancel();
		}
	}
}
