package fragment;

import android.support.v4.app.Fragment;

public abstract class  BaseFragment extends Fragment {
	
	/** Fragment当前状态是否可见 */
	protected boolean isVisible;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}
	/**
	 * 可见
	 */
	protected void onVisible() {
		lazyLoad();
	}
	/**
	 * 不可见
	 */
	protected void onInvisible() {
	}
	/** 
	 * 延迟加载
	 * 子类必须重写此方法
	 */
	protected abstract void lazyLoad();
	public void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart("MainScreen"); //统计页面，"MainScreen"为页面名称，可自定义
	}
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("MainScreen");
	}
}
