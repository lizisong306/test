package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

public class PageFragmentAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragmentList;
	private FragmentManager fm;
	private FragmentTransaction ft;
	public PageFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragmentList=fragmentList;
		this.fm=fm;
	}
	@Override
	public Fragment getItem(int idx) {
		// TODO Auto-generated method stub
		return fragmentList.get(idx%fragmentList.size());
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList==null?0:fragmentList.size();
	}
	@Override  
	public int getItemPosition(Object object) {  
	   return POSITION_NONE;
	}
	public void setFragments(List<Fragment> fragments) {
		   if(this.fragmentList != null){
		       ft = fm.beginTransaction();
		      for(Fragment f:this.fragmentList){
		        ft.remove(f);
		      }
		      ft.commit();
		      ft=null;
		      fm.executePendingTransactions();
		   }
		  this.fragmentList = fragments;
		  notifyDataSetChanged();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
		Fragment fragment = fragmentList.get(position);
       fm.beginTransaction().hide(fragment).commit();
	}
	@Override
    public Fragment instantiateItem(ViewGroup container, int position) {
		        Fragment fragment = (Fragment) super.instantiateItem(container,
		               position);
		        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
		       return fragment;
	    }
}