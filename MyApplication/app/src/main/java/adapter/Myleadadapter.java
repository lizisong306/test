package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maidiantech.R;


public class Myleadadapter extends PagerAdapter {
	private int[] imageicon = { R.mipmap.lead1, R.mipmap.lead2,
			R.mipmap.lead3,R.mipmap.lead4 };
	private int[] imageicon1 = { R.mipmap.zi1, R.mipmap.zi2,
			R.mipmap.zi3,R.mipmap.zi4 };
	private int[] imageicon2 = { R.mipmap.jindu1, R.mipmap.jindu2,
			R.mipmap.jindu3,R.mipmap.jindu4 };

	Context context;

	public Myleadadapter(Context context) {
		super();
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(context, R.layout.activity_lead_show, null);
		ImageView image = (ImageView) view.findViewById(R.id.lead_show);
		ImageView wenzi = (ImageView) view.findViewById(R.id.wenzi);
		ImageView bottmon = (ImageView) view.findViewById(R.id.bottmon);
		image.setImageResource(imageicon[position]);
		wenzi.setImageResource(imageicon1[position]);
		bottmon.setImageResource(imageicon2[position]);
		container.addView(view);
		return view;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
