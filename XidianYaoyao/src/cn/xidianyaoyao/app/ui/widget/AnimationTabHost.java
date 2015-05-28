package cn.xidianyaoyao.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import cn.xidianyaoyao.app.R;

/**
 * FragmentTab切换动画
 * 
 * @author WangTanyun
 * 
 */

public class AnimationTabHost extends TabHost {

	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	private int mTabCount;// 记录当前标签页的总数

	public AnimationTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		slideLeftIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_left);
		slideLeftOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_left);
		slideRightIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_right);
		slideRightOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_right);
	}

	public int getTabCount() {
		return mTabCount;
	}

	public void addTab(TabSpec tabSpec) {
		mTabCount++;
		super.addTab(tabSpec);
	}

	public void setCurrentTab(int index) {
		int mCurrentTabID = getCurrentTab();

		// 定义退出屏幕动画
		if (getCurrentTabView() != null) {// 第一次进入该函数时候为空
			if (mCurrentTabID == (mTabCount - 1) && index == 0) {
				getCurrentView().startAnimation(slideLeftOut);
			} else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
				getCurrentView().startAnimation(slideRightOut);
			} else if (index > mCurrentTabID) {
				getCurrentView().startAnimation(slideLeftOut);
			} else if (index < mCurrentTabID) {
				getCurrentView().startAnimation(slideRightOut);
			}

		}

		// 定义进入屏幕动画
		if (getCurrentTabView() != null) {
			if (mCurrentTabID == (mTabCount - 1) && index == 0) {
				getCurrentView().startAnimation(slideLeftIn);
			} else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
				getCurrentView().startAnimation(slideRightIn);
			} else if (index > mCurrentTabID) {
				getCurrentView().startAnimation(slideLeftIn);
			} else if (index < mCurrentTabID) {
				getCurrentView().startAnimation(slideRightIn);
			}
		}
	}
}
