package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;
import java.util.Map;

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
import android.widget.RatingBar;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Person_evaluate extends Activity {

	private ImageView mEvaluateClose;
	private ImageView mEvaluateSend;
	private EditText mEvaluateSummary;
	private RatingBar mScoreRatingBar;

	private ProgressDialog mProgress;
	private AsyncEvaluateTask mTask;

	private String sUserScore;
	private String sUserEcaluate;

	private PreferencesService preferencesService;
	private Map<String, String> params;
//	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_evaluate);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mEvaluateClose = (ImageView) findViewById(R.id.send_evaluate_back);
		mEvaluateSend = (ImageView) findViewById(R.id.send_evaluate_send);
		mEvaluateSummary = (EditText) findViewById(R.id.send_evaluate_summary);

		mScoreRatingBar = (RatingBar) findViewById(R.id.score_ratingbar);
	}

	private void setLister() {
		mEvaluateClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mEvaluateSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (evaluate_inputIsSuccess()) {
					preferencesService = new PreferencesService(
							Person_evaluate.this);
					params = preferencesService.cusInfo_getPreferences();
					sUserScore = String.valueOf(mScoreRatingBar.getRating());
					sUserEcaluate = mEvaluateSummary.getText().toString();
					if (mTask != null
							&& mTask.getStatus() == AsyncEvaluateTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncEvaluateTask();
					mTask.execute(params.get("cusName"), getIntent()
							.getStringExtra("DishId"), getIntent()
							.getStringExtra("RestauId"), sUserScore,
							sUserEcaluate);

//					preferencesService = new PreferencesService(
//							Person_evaluate.this);
//					params = preferencesService.cusInfo_getPreferences();
//					MoregoSQLiteHelper helper = new MoregoSQLiteHelper(
//							Person_evaluate.this, params.get("cusName"));
//					db = helper.getWritableDatabase();// 获取可读写的数据库
//					helper.InsertData(db, params.get("cusName"), getIntent()
//							.getStringExtra("RestauId"), getIntent()
//							.getStringExtra("RestauName"), getIntent()
//							.getStringExtra("RestauScore"), getIntent()
//							.getStringExtra("RestauAddr"), getIntent()
//							.getStringExtra("RestauCall"), getIntent()
//							.getStringExtra("RestauDescr"), getIntent()
//							.getStringExtra("RestauLat"), getIntent()
//							.getStringExtra("RestauLon"));

				} else {
					mEvaluateSummary.setText("");
				}
			}
		});

		mScoreRatingBar
				.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						Toast.makeText(Person_evaluate.this,
								"score:" + String.valueOf(rating),
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	// 判断用户输入是否正确
	private boolean evaluate_inputIsSuccess() {

		// 获取用户输入的信息
		String evaluate = mEvaluateSummary.getText().toString().trim();
		if ("".equals(evaluate)) {
			Toast.makeText(Person_evaluate.this, "添加评语不能为空!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public class AsyncEvaluateTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.addEvaluate(
						params[0], params[1], params[2], params[3], params[4]);
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
			if (result.equals("110000")) {
				Toast.makeText(Person_evaluate.this, "添加评论成功",
						Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			} else if (result.equals("110001")) {
				Toast.makeText(Person_evaluate.this, "添加评论失败，服务器故障！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Person_evaluate.this, "网络错误，请重试！",
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
			mProgress.setMessage("正在添加评论...");
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