package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;

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
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Person_login extends Activity {

	private ImageView mLoginBack;
	private EditText mUser; // ’ ∫≈±‡º≠øÚ
	private EditText mPassword; // √‹¬Î±‡º≠øÚ
	private Button mLogin;// µ«¬º
	private TextView mRegist;// ◊¢≤·
	private TextView mForgetPwd;// Õ¸º«√‹¬Î

	private AsyncLoginTask mTask;
	private ProgressDialog mProgress;
	private String UserStr;
	private String PasswordStr;

	private String sUser;
	private String sGender;
	private String sEmail;
	private String sHead;

	private PreferencesService preferencesService;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_login);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mLoginBack = (ImageView) findViewById(R.id.login_back);
		mLogin = (Button) findViewById(R.id.login_btn);
		mRegist = (TextView) findViewById(R.id.login_regist);
		mForgetPwd = (TextView) findViewById(R.id.login_forgetPwd);

		mUser = (EditText) findViewById(R.id.login_user);
		mPassword = (EditText) findViewById(R.id.login_pwd);

		preferencesService = new PreferencesService(this);
	}

	private void setLister() {
		mLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (login_inputIsSuccess()) {
					UserStr = mUser.getText().toString();
					PasswordStr = mPassword.getText().toString();
					if (mTask != null
							&& mTask.getStatus() == AsyncLoginTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncLoginTask();
					mTask.execute(UserStr, PasswordStr);
				} else {
					mUser.setText("");
					mPassword.setText("");
				}
			}
		});

		mRegist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Person_login.this, Person_regist.class);
				startActivity(intent);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});

		mForgetPwd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Person_login.this, Person_find_pwd.class);
				startActivity(intent);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});

		mLoginBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
	}

	private boolean login_inputIsSuccess() {
		String username = mUser.getText().toString().trim();
		String password = mPassword.getText().toString().trim();
		if ("".equals(username)) {
			Toast.makeText(Person_login.this, "’À∫≈ ‰»Î≤ªƒ‹Œ™ø’!", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if ("".equals(password)) {
			Toast.makeText(Person_login.this, "√‹¬Î ‰»Î≤ªƒ‹Œ™ø’!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncLoginTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.login(
						params[0], params[1]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("commonACK");
				JSONObject cusInfo = new JSONObject(
						resultCode.getString("customer"));
				if (!cusInfo.equals("null")) {
					sUser = cusInfo.getString("cusId");
					sGender = cusInfo.getString("gender");
					sEmail = cusInfo.getString("email");
					sHead = cusInfo.getString("headimg");
				}
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
			if (result.equals("100100")) {
				Toast.makeText(Person_login.this, "µ«¬º≥…π¶£°", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				Person_login.this.setResult(1, intent);
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				preferencesService.save_cusInfo(sUser, sGender, sEmail, sHead);
			} else if (result.equals("100000")) {
				Toast.makeText(Person_login.this, "µ«¬º ß∞‹£¨’À∫≈ªÚ√‹¬Î ‰»Î¥ÌŒÛ£°",
						Toast.LENGTH_SHORT).show();
				mUser.setText("");
				mPassword.setText("");
			} else {
				Toast.makeText(Person_login.this, "Õ¯¬Á¥ÌŒÛ£¨«Î÷ÿ ‘£°",
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
			mProgress.setMessage("’˝‘⁄µ«¬º÷–...");
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
