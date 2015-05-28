package cn.xidianyaoyao.app.ui.restaurant;

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
import android.widget.TextView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.main.MainYaoyao;
import cn.xidianyaoyao.app.ui.widget.AnimationTabHost;

public class Restau_info extends FragmentActivity implements
		OnCheckedChangeListener {

	private static final String TAB_INTRODUCE = "tab_introduce";
	private static final String TAB_PHOTO = "tab_photo";
	private static final String TAB_RECOM = "tab_recom";

	private ImageView mRestauBack;// 返回
	private ImageView mRestauHome;
	private TextView mTitleName;

	private AnimationTabHost mTabHost;
	private RadioGroup mRadioGroup;
	private RadioButton mDefaultBtn;

	private FragmentManager mFrgManager;
	private FragmentTransaction mFrgTran;
	private Fragment mTabFragment;

	private String mStringRestauName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restau_info);

		initView();
		setLister();

		initTabHost();
		initRadioGroup();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mRestauBack = (ImageView) findViewById(R.id.restau_back);
		mRestauHome = (ImageView) findViewById(R.id.restau_home);
		mTitleName = (TextView) findViewById(R.id.restau_titleName);

		mStringRestauName = getIntent().getStringExtra("RestauName");
		mTitleName.setText(mStringRestauName);
	}

	private void setLister() {
		mRestauBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		mRestauHome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Restau_info.this, MainYaoyao.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	private void initTabHost() {
		mTabHost = (AnimationTabHost) findViewById(R.id.anim_tabhost);
		mTabHost.setup();

		TabSpec ts1 = mTabHost.newTabSpec(TAB_INTRODUCE).setIndicator(
				TAB_INTRODUCE);
		ts1.setContent(new Intent(this, Fragment_Restau_Introduce.class));
		mTabHost.addTab(ts1);

		TabSpec ts2 = mTabHost.newTabSpec(TAB_PHOTO).setIndicator(TAB_PHOTO);
		ts2.setContent(new Intent(this, Fragment_Restau_Photo.class));
		mTabHost.addTab(ts2);

		TabSpec ts3 = mTabHost.newTabSpec(TAB_RECOM).setIndicator(TAB_RECOM);
		ts3.setContent(new Intent(this, Fragment_Restau_Recom.class));
		mTabHost.addTab(ts3);

		mTabHost.setCurrentTab(0);// 设置默认标签
	}

	private void initRadioGroup() {
		mRadioGroup = (RadioGroup) findViewById(R.id.restau_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mDefaultBtn = (RadioButton) findViewById(R.id.restau_introduce);
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
		case R.id.restau_introduce:
			mTabFragment = new Fragment_Restau_Introduce();
			Bundle restauIntro_bundle = new Bundle();
			restauIntro_bundle.putString("RestauId", getIntent()
					.getStringExtra("RestauId"));
			restauIntro_bundle.putString("RestauName", getIntent()
					.getStringExtra("RestauName"));
			restauIntro_bundle.putString("RestauScore", getIntent()
					.getStringExtra("RestauScore"));
			restauIntro_bundle.putString("RestauAddr", getIntent()
					.getStringExtra("RestauAddr"));
			restauIntro_bundle.putString("RestauCall", getIntent()
					.getStringExtra("RestauCall"));
			restauIntro_bundle.putString("RestauDescr", getIntent()
					.getStringExtra("RestauDescr"));
			restauIntro_bundle.putString("RestauLat", getIntent()
					.getStringExtra("RestauLat"));
			restauIntro_bundle.putString("RestauLon", getIntent()
					.getStringExtra("RestauLon"));
			mTabFragment.setArguments(restauIntro_bundle);
			mTabHost.setCurrentTab(0);
			break;
		case R.id.restau_photo:
			mTabFragment = new Fragment_Restau_Photo();
			Bundle restauPhoto_bundle = new Bundle();
			restauPhoto_bundle.putString("RestauId", getIntent()
					.getStringExtra("RestauId"));
			mTabFragment.setArguments(restauPhoto_bundle);
			mTabHost.setCurrentTab(1);
			break;
		case R.id.restau_recom:
			mTabFragment = new Fragment_Restau_Recom();
			Bundle restauRecom_bundle = new Bundle();
			restauRecom_bundle.putString("RestauId", getIntent()
					.getStringExtra("RestauId"));
			mTabFragment.setArguments(restauRecom_bundle);
			mTabHost.setCurrentTab(2);
			break;
		}
		mFrgTran.replace(android.R.id.tabcontent, mTabFragment).commit();
	}
}
