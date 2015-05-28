package cn.xidianyaoyao.app.ui.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.AnimationTabHost;

public class Person_friendList extends FragmentActivity implements
		OnCheckedChangeListener {

	private static final String TAB_FOLLOWS = "tab_follows";
	private static final String TAB_FANS = "tab_fans";

	private AnimationTabHost mTabHost;
	private RadioGroup mRadioGroup;
	private RadioButton mDefaultBtn;

	private FragmentManager mFrgManager;
	private FragmentTransaction mFrgTran;
	private Fragment mTabFragment;

	private ImageView mFriendBack;// 从找回密码界面返回
	private ImageView mFriendSearch;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_friendlist);

		initView();
		setLister();

		initTabHost();
		initRadioGroup();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mFriendBack = (ImageView) findViewById(R.id.friend_back);
		mFriendSearch = (ImageView) findViewById(R.id.friend_find);
	}

	private void setLister() {
		mFriendBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		mFriendSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Person_friendList.this, Person_friendSearch.class);
				startActivity(intent);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});
	}

	private void initTabHost() {
		mTabHost = (AnimationTabHost) findViewById(R.id.anim_tabhost);
		mTabHost.setup();

		TabSpec ts1 = mTabHost.newTabSpec(TAB_FOLLOWS)
				.setIndicator(TAB_FOLLOWS);
		ts1.setContent(new Intent(this, Fragment_person_follows.class));
		mTabHost.addTab(ts1);

		TabSpec ts2 = mTabHost.newTabSpec(TAB_FANS).setIndicator(TAB_FANS);
		ts2.setContent(new Intent(this, Fragment_person_fans.class));
		mTabHost.addTab(ts2);

		mTabHost.setCurrentTab(0);// 设置默认标签
	}

	private void initRadioGroup() {
		mRadioGroup = (RadioGroup) findViewById(R.id.friend_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mDefaultBtn = (RadioButton) findViewById(R.id.friend_follows);
		mDefaultBtn.setChecked(true);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {

		mFrgManager = getSupportFragmentManager();
		mFrgTran = mFrgManager.beginTransaction();

		if (mTabFragment != null) {
			// 是否需要加入mTabFragment非空的判断，在每次切换时，销毁fragment或者缓存些数据！--待定
			mTabFragment.onDetach();
		}

		switch (checkedId) {
		case R.id.friend_follows:
			mTabFragment = new Fragment_person_follows();
			mTabHost.setCurrentTab(0);
			break;
		case R.id.friend_fans:
			mTabFragment = new Fragment_person_fans();
			mTabHost.setCurrentTab(1);
			break;
		}
		mFrgTran.replace(android.R.id.tabcontent, mTabFragment).commit();
	}
}
