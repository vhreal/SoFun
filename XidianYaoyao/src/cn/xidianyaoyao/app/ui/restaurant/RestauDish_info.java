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

public class RestauDish_info extends FragmentActivity implements
		OnCheckedChangeListener {

	private static final String TAB_DISH = "tab_dish";
	private static final String TAB_INTRODUCE = "tab_introduce";
	private static final String TAB_PHOTO = "tab_photo";
	private static final String TAB_RECOM = "tab_recom";

	private ImageView mRestauDishBack;// 返回
	private ImageView mRestauDishHome;
	private TextView mTitleName;

	private AnimationTabHost mTabHost;
	private RadioGroup mRadioGroup;
	private RadioButton mDishBtn;
	private RadioButton mDefaultBtn;

	private FragmentManager mFrgManager;
	private FragmentTransaction mFrgTran;
	private Fragment mTabFragment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restau_dish_info);

		initView();
		setLister();

		initTabHost();
		initRadioGroup();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mRestauDishBack = (ImageView) findViewById(R.id.restauDish_back);
		mRestauDishHome = (ImageView) findViewById(R.id.restauDish_home);
		mDishBtn = (RadioButton) findViewById(R.id.restauDish_dish);
		mTitleName = (TextView) findViewById(R.id.restauDish_titleName);

		mDishBtn.setText(getIntent().getStringExtra("DishName"));
		mTitleName.setText(getIntent().getStringExtra("RestauName"));
	}

	private void setLister() {
		mRestauDishBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		mRestauDishHome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RestauDish_info.this, MainYaoyao.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	private void initTabHost() {
		mTabHost = (AnimationTabHost) findViewById(R.id.anim_tabhost);
		mTabHost.setup();

		TabSpec ts1 = mTabHost.newTabSpec(TAB_DISH).setIndicator(TAB_DISH);
		ts1.setContent(new Intent(this, Dish_EvaluateSee.class));
		mTabHost.addTab(ts1);

		TabSpec ts2 = mTabHost.newTabSpec(TAB_INTRODUCE).setIndicator(
				TAB_INTRODUCE);
		ts2.setContent(new Intent(this, Fragment_Restau_Introduce.class));
		mTabHost.addTab(ts2);

		TabSpec ts3 = mTabHost.newTabSpec(TAB_PHOTO).setIndicator(TAB_PHOTO);
		ts3.setContent(new Intent(this, Fragment_Restau_Photo.class));
		mTabHost.addTab(ts3);

		TabSpec ts4 = mTabHost.newTabSpec(TAB_RECOM).setIndicator(TAB_RECOM);
		ts4.setContent(new Intent(this, Fragment_Restau_Recom.class));
		mTabHost.addTab(ts4);

		mTabHost.setCurrentTab(0);// 设置默认标签
	}

	private void initRadioGroup() {
		mRadioGroup = (RadioGroup) findViewById(R.id.restauDish_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mDefaultBtn = (RadioButton) findViewById(R.id.restauDish_dish);
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
		case R.id.restauDish_dish:
			mTabFragment = new Fragment_Restau_Dish();
			Bundle dish_bundle = new Bundle();
			dish_bundle.putString("DishId", getIntent()
					.getStringExtra("DishId"));
			dish_bundle.putString("DishImage",
					getIntent().getStringExtra("DishImage"));
			dish_bundle.putString("DishName",
					getIntent().getStringExtra("DishName"));
			dish_bundle.putString("DishScore",
					getIntent().getStringExtra("DishScore"));
			dish_bundle.putString("DishPrice",
					getIntent().getStringExtra("DishPrice"));
			dish_bundle.putString("DishTaste",
					getIntent().getStringExtra("DishTaste"));
			dish_bundle.putString("DishNutrition",
					getIntent().getStringExtra("DishNutrition"));
			dish_bundle.putString("RestauId",
					getIntent().getStringExtra("RestauId"));
			dish_bundle.putString("RestauName",
					getIntent().getStringExtra("RestauName"));
			dish_bundle.putString("RestauScore",
					getIntent().getStringExtra("RestauScore"));
			dish_bundle.putString("RestauAddr",
					getIntent().getStringExtra("RestauAddr"));
			dish_bundle.putString("RestauCall",
					getIntent().getStringExtra("RestauCall"));
			dish_bundle.putString("RestauDescr",
					getIntent().getStringExtra("RestauDescr"));
			dish_bundle.putString("RestauLat",
					getIntent().getStringExtra("RestauLat"));
			dish_bundle.putString("RestauLon",
					getIntent().getStringExtra("RestauLon"));
			mTabFragment.setArguments(dish_bundle);
			mTabHost.setCurrentTab(0);
			break;
		case R.id.restauDish_introduce:
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
			mTabHost.setCurrentTab(1);
			break;
		case R.id.restauDish_photo:
			mTabFragment = new Fragment_Restau_Photo();
			Bundle restauPhoto_bundle = new Bundle();
			restauPhoto_bundle.putString("RestauId", getIntent()
					.getStringExtra("RestauId"));
			mTabFragment.setArguments(restauPhoto_bundle);
			mTabHost.setCurrentTab(2);
			break;
		case R.id.restauDish_recom:
			mTabFragment = new Fragment_Restau_Recom();
			Bundle restauRecom_bundle = new Bundle();
			restauRecom_bundle.putString("RestauId", getIntent()
					.getStringExtra("RestauId"));
			mTabFragment.setArguments(restauRecom_bundle);
			mTabHost.setCurrentTab(3);
			break;
		}
		mFrgTran.replace(android.R.id.tabcontent, mTabFragment).commit();
	}
}
