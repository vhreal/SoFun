package cn.xidianyaoyao.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.person.Person_suggestion;
import cn.xidianyaoyao.app.ui.widget.AnimationTabHost;
import cn.xidianyaoyao.app.ui.widget.Dialog_Exit;

public class MainYaoyao extends FragmentActivity implements
		OnCheckedChangeListener {

	private static final String TAB_SHAKE = "tab_shake";
	private static final String TAB_SEARCH = "tab_search";
	private static final String TAB_RANK = "tab_rank";
	private static final String TAB_RECOMMEND = "tab_recommend";
	private static final String TAB_PERSON = "tab_person";

	private AnimationTabHost mTabHost;
	private RadioGroup mRadioGroup;
	private RadioButton mDefaultBtn;

	private FragmentManager mFrgManager;
	private FragmentTransaction mFrgTran;
	private Fragment mTabFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainyaoyao);

		initMainTabHost();
		initMainRadioGroup();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initMainTabHost() {
		mTabHost = (AnimationTabHost) findViewById(R.id.anim_main_tabhost);
		mTabHost.setup();

		TabSpec ts1 = mTabHost.newTabSpec(TAB_SHAKE).setIndicator(TAB_SHAKE);
		ts1.setContent(new Intent(this, Fragment_MainShake.class));
		mTabHost.addTab(ts1);

		TabSpec ts2 = mTabHost.newTabSpec(TAB_SEARCH).setIndicator(TAB_SEARCH);
		ts2.setContent(new Intent(this, Fragment_MainSearch.class));
		mTabHost.addTab(ts2);

		TabSpec ts3 = mTabHost.newTabSpec(TAB_RANK).setIndicator(TAB_RANK);
		ts3.setContent(new Intent(this, Fragment_MainRank.class));
		mTabHost.addTab(ts3);

		TabSpec ts4 = mTabHost.newTabSpec(TAB_RECOMMEND).setIndicator(
				TAB_RECOMMEND);
		ts4.setContent(new Intent(this, Fragment_MainRecommend.class));
		mTabHost.addTab(ts4);

		TabSpec ts5 = mTabHost.newTabSpec(TAB_PERSON).setIndicator(TAB_PERSON);
		ts5.setContent(new Intent(this, Fragment_MainPerson.class));
		mTabHost.addTab(ts5);

		mTabHost.setCurrentTab(0);// 设置默认标签
	}

	private void initMainRadioGroup() {
		mRadioGroup = (RadioGroup) findViewById(R.id.main_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mDefaultBtn = (RadioButton) findViewById(R.id.main_shake);
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
		case R.id.main_shake:
			mTabFragment = new Fragment_MainShake();
			mTabHost.setCurrentTab(0);
			break;
		case R.id.main_search:
			mTabFragment = new Fragment_MainSearch();
			mTabHost.setCurrentTab(1);
			break;
		case R.id.main_rank:
			mTabFragment = new Fragment_MainRank();
			mTabHost.setCurrentTab(2);
			break;
		case R.id.main_recommend:
			mTabFragment = new Fragment_MainRecommend();
			mTabHost.setCurrentTab(3);
			break;
		case R.id.main_person:
			mTabFragment = new Fragment_MainPerson();
			mTabHost.setCurrentTab(4);
			break;
		}
		mFrgTran.replace(android.R.id.tabcontent, mTabFragment).commit();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.backing:
			startActivity(new Intent().setClass(this, Person_suggestion.class));
			break;
		case R.id.setting:
			startActivity(new Intent().setClass(this, Main_set.class));
			break;
		case R.id.exiting:
			Dialog_Exit mExit = new Dialog_Exit(this);
			mExit.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private long exitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出SoFun",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				// finish();
				// System.exit(0);
				XidianYaoyaoApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
