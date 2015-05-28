package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
import cn.xidianyaoyao.app.ui.widget.SendMail;

public class Person_find_pwd extends Activity {

	private ImageView mFindpwdBack;// 从找回密码界面返回
	private EditText mFindpwdUser;
	private Button mFindPwdSumbit;

	private AsyncFindpwdTask mTask;
	private ProgressDialog mProgress;

	private String FUserStr;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_find_pwd);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mFindpwdBack = (ImageView) findViewById(R.id.find_pwd_back);
		mFindpwdUser = (EditText) findViewById(R.id.find_pwd_user);
		mFindPwdSumbit = (Button) findViewById(R.id.find_pwd_sumbit);
	}

	private void setLister() {
		mFindpwdBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mFindPwdSumbit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (findpwd_inputIsSuccess()) {
					FUserStr = mFindpwdUser.getText().toString();
					if (mTask != null
							&& mTask.getStatus() == AsyncFindpwdTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncFindpwdTask();
					mTask.execute(FUserStr);
				} else {
					mFindpwdUser.setText("");
				}
			}
		});
	}

	// 判断用户输入是否正确
	private boolean findpwd_inputIsSuccess() {
		String username = mFindpwdUser.getText().toString().trim();
		if ("".equals(username)) {
			Toast.makeText(Person_find_pwd.this, "账号不能为空!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncFindpwdTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.find_pwd(params[0]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("commonACK");
				if (!resultCode.getString("customer").equals("null")) {
					JSONObject cusInfo = new JSONObject(
							resultCode.getString("customer"));
					String pwd = cusInfo.getString("passwd");
					String email = cusInfo.getString("email");
					SendMail.sendEmail(email, "SoFun找回密码", "您的SoFun账号的密码： " + pwd);
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
			if (result.equals("101001")) {
				Toast.makeText(Person_find_pwd.this, "找回密码成功，\n已发送到你的注册邮箱！",
						Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			} else if (result.equals("100111")) {
				Toast.makeText(Person_find_pwd.this, "账号不存在！",
						Toast.LENGTH_SHORT).show();
				mFindpwdUser.setText("");
			} else {
				Toast.makeText(Person_find_pwd.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {// 该方法将在执行后台耗时操作前被调用，初始化，现实进度条等
			super.onPreExecute();
			startProgressDialog();
		}
	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("正在找回密码...");
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
