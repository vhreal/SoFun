package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Person_changepwd extends Activity {

	private ImageView mChangepwdBack;// 从找回密码界面返回

	private EditText mChangepwdYuan;
	private EditText mChangepwdXin;
	private EditText mChangepwdDing;

	private Button mChangeSubmit;

	private AsyncChangepwdTask mTask;
	private ProgressDialog mProgress;

	private String YuanStr;
	private String XinStr;
	private PreferencesService preferencesService;
	private Map<String, String> params;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_changepwd);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mChangepwdBack = (ImageView) findViewById(R.id.change_pwd_back);

		mChangepwdYuan = (EditText) findViewById(R.id.change_pwd_yuan);
		mChangepwdXin = (EditText) findViewById(R.id.change_pwd_xin);
		mChangepwdDing = (EditText) findViewById(R.id.change_pwd_ding);
		mChangeSubmit = (Button) findViewById(R.id.change_pwd_sumbit);

		preferencesService = new PreferencesService(this);
		params = preferencesService.cusInfo_getPreferences();
	}

	private void setLister() {
		mChangepwdBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mChangeSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (changepwd_inputIsSuccess()) {
					YuanStr = mChangepwdYuan.getText().toString();
					XinStr = mChangepwdXin.getText().toString();
					if (mTask != null
							&& mTask.getStatus() == AsyncChangepwdTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncChangepwdTask();
					mTask.execute(params.get("cusName"), YuanStr, XinStr);
				} else {
					mChangepwdYuan.setText("");
					mChangepwdXin.setText("");
					mChangepwdDing.setText("");
				}
			}
		});
	}

	private boolean pwdIsSame(String first, String second) {
		if (first.equals(second)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean changepwd_inputIsSuccess() {
		String yuanPwd = mChangepwdYuan.getText().toString();
		String xinPwd = mChangepwdXin.getText().toString();
		String againPwd = mChangepwdDing.getText().toString();
		if ("".equals(yuanPwd)) {
			Toast.makeText(Person_changepwd.this, "原密码不能为空!",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(xinPwd)) {
			Toast.makeText(Person_changepwd.this, "新密码不能为空!",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(againPwd)) {
			Toast.makeText(Person_changepwd.this, "确定密码不能为空!",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (!pwdIsSame(xinPwd, againPwd)) {
			Toast.makeText(this, "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
			mChangepwdXin.setText("");
			mChangepwdDing.setText("");
			return false;
		}
		return true;
	}

	public class AsyncChangepwdTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.change_pwd(
						params[0], params[1], params[2]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("commonACK");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("101010")) {
				Toast.makeText(Person_changepwd.this, "修改密码成功！",
						Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent();
				intent.setClass(Person_changepwd.this, Person_login.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else if (result.equals("101011")) {
				Toast.makeText(Person_changepwd.this, "原密码输入错误！",
						Toast.LENGTH_SHORT).show();
				mChangepwdYuan.setText("");
				mChangepwdXin.setText("");
				mChangepwdDing.setText("");
			} else {
				Toast.makeText(Person_changepwd.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}
	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("正在修改密码...");
		}
		mProgress.show();
	}

	private void stopProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}
}
