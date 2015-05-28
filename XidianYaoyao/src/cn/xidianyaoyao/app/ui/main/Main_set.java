package cn.xidianyaoyao.app.ui.main;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.person.Person_login;
import cn.xidianyaoyao.app.ui.person.Person_suggestion;
import cn.xidianyaoyao.app.ui.restaurant.Restau_Add;
import cn.xidianyaoyao.app.ui.widget.Dialog_ClearCache;
import cn.xidianyaoyao.app.ui.widget.Dialog_Exit;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Main_set extends Activity {

	private Button mMainSetExit;// 从设置中退出
	private Button mPersonExit;
	private Button mPersonLogin;
	private ImageView mMainSetBack;// 从设置中返回

	private RelativeLayout mLayVoice;
	private RelativeLayout mLayVibrate;
	private CheckBox mMainSetVoice;
	private CheckBox mMainSetVibrate;
	private RelativeLayout mMainSetClearCache;
	private RelativeLayout mMainSetRestauAdd;
	private RelativeLayout mMainSetMsgReturn;
	private RelativeLayout mMainSetAbout;

	private PreferencesService preferencesService;
	private Map<String, String> params;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_set);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mMainSetExit = (Button) findViewById(R.id.main_set_exit);
		mPersonExit = (Button) findViewById(R.id.user_exit);
		mPersonLogin = (Button) findViewById(R.id.user_login);
		mMainSetBack = (ImageView) findViewById(R.id.main_set_back);

		mLayVoice = (RelativeLayout) findViewById(R.id.layout_voice);
		mLayVibrate = (RelativeLayout) findViewById(R.id.layout_vibrate);
		mMainSetVoice = (CheckBox) findViewById(R.id.main_set_voice);
		mMainSetVibrate = (CheckBox) findViewById(R.id.main_set_vibrate);
		mMainSetClearCache = (RelativeLayout) findViewById(R.id.main_set_clearcache);
		mMainSetRestauAdd = (RelativeLayout) findViewById(R.id.main_set_addrestau);
		mMainSetMsgReturn = (RelativeLayout) findViewById(R.id.main_set_msg);
		mMainSetAbout = (RelativeLayout) findViewById(R.id.main_set_about);

		preferencesService = new PreferencesService(this);
		params = preferencesService.cusInfo_getPreferences();

		if (params.get("cusName").equals("")) {
			mPersonExit.setVisibility(View.GONE);
			mPersonLogin.setVisibility(View.VISIBLE);
		} else if (!params.get("cusName").equals("")) {
			mPersonLogin.setVisibility(View.GONE);
			mPersonExit.setVisibility(View.VISIBLE);
		}
		mMainSetVoice.setChecked(preferencesService.voiceSet_getPreferences());// 默认值为关闭
		mMainSetVibrate.setChecked(preferencesService
				.vibrateSet_getPreferences());// 默认值为关闭
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:// 登录成功
			mPersonLogin.setVisibility(View.GONE);
			mPersonExit.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void setLister() {
		mMainSetExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Dialog_Exit mExit = new Dialog_Exit(Main_set.this);
				mExit.show();
			}
		});
		mPersonLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(Main_set.this,
						Person_login.class), 1);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		mPersonExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPersonExit.setVisibility(View.GONE);
				mPersonLogin.setVisibility(View.VISIBLE);
				Toast.makeText(Main_set.this, "退出登录成功！", Toast.LENGTH_SHORT)
						.show();
				preferencesService.save_cusInfo("", "", "", "");// 退出登录后，使保存的用户信息为空
			}
		});

		mMainSetBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		mLayVoice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mMainSetVoice.isChecked()) {
					mMainSetVoice.setChecked(false);
					preferencesService.save_voiceSet(false);
				} else {
					mMainSetVoice.setChecked(true);
					preferencesService.save_voiceSet(true);
				}
			}
		});
		mLayVibrate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mMainSetVibrate.isChecked()) {
					mMainSetVibrate.setChecked(false);
					preferencesService.save_vibrateSet(false);
				} else {
					mMainSetVibrate.setChecked(true);
					preferencesService.save_vibrateSet(true);
				}
			}
		});

		mMainSetVoice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mMainSetVoice.isChecked()) {
					preferencesService.save_voiceSet(true);
				} else {
					preferencesService.save_voiceSet(false);
				}
			}
		});

		mMainSetVibrate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mMainSetVibrate.isChecked()) {
					preferencesService.save_vibrateSet(true);
				} else {
					preferencesService.save_vibrateSet(false);
				}
			}
		});

		mMainSetClearCache.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Dialog_ClearCache mClearCache = new Dialog_ClearCache(
						Main_set.this);
				mClearCache.show();
			}
		});

		mMainSetRestauAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Main_set.this, Restau_Add.class);
				startActivity(intent);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});
		mMainSetMsgReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Main_set.this, Person_suggestion.class);
				startActivity(intent);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});

		mMainSetAbout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Main_set.this, Main_about.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}
}
