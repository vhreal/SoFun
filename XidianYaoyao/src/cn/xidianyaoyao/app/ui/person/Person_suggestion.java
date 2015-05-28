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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class Person_suggestion extends Activity {

	private ImageView mSuggestionClose;
	private ImageView mSuggestionSend;
	private EditText mSuggestionSummary;

	private AsyncSuggestionTask mTask;
	private ProgressDialog mProgress;

	String UserSuggestion;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_suggestion);
		XidianYaoyaoApplication.getInstance().addActivity(this);

		mSuggestionClose = (ImageView) findViewById(R.id.suggestion_back);
		mSuggestionSend = (ImageView) findViewById(R.id.suggsetion_send);
		mSuggestionSummary = (EditText) findViewById(R.id.suggsetion_summary);

		mSuggestionClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mSuggestionSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (suggestion_inputIsSuccess()) {
					UserSuggestion = mSuggestionSummary.getText().toString();
					if (mTask != null
							&& mTask.getStatus() == AsyncSuggestionTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncSuggestionTask();
					mTask.execute(UserSuggestion);
				} else {
					mSuggestionSummary.setText("");
				}
			}
		});
	}

	private boolean suggestion_inputIsSuccess() {

		String suggestion = mSuggestionSummary.getText().toString().trim();
		if ("".equals(suggestion)) {
			Toast.makeText(Person_suggestion.this, "反馈意见不能为空!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private final class AsyncSuggestionTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 子线程中执行的
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.suggestion(params[0]);
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

		protected void onPostExecute(String result) {// 运行在主线程
			stopProgressDialog();
			if (result.equals("101000")) {
				Toast.makeText(Person_suggestion.this, "发送意见成功",
						Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			} else {
				Toast.makeText(Person_suggestion.this, "网络错误，请重试！",
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
			mProgress.setMessage("正在反馈意见中...");
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
