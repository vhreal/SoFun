package cn.xidianyaoyao.app.ui.restaurant;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class Fragment_Restau_Introduce extends Fragment {

	private View mIntroduceView;
	private Bundle bundle;

	private TextView mRestauName;
	private TextView mRestauScore;
	private RelativeLayout mRestaucallItem;
	private RelativeLayout mRestauaddrItem;
	private RelativeLayout mRestauerrorItem;
	private TextView mRestauAddr;
	private TextView mRestauCall;
	private TextView mRestauDescr;

	private ProgressDialog mProgress;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;

		mIntroduceView = (View) inflater.inflate(
				R.layout.fragment_restau_introduce, container, false);

		initView();
		setListener();

		return mIntroduceView;
	}

	private void initView() {
		mRestauName = (TextView) mIntroduceView.findViewById(R.id.restau_name);
		mRestauScore = (TextView) mIntroduceView
				.findViewById(R.id.restau_score);
		mRestaucallItem = (RelativeLayout) mIntroduceView
				.findViewById(R.id.restau_call_layout);
		mRestauaddrItem = (RelativeLayout) mIntroduceView
				.findViewById(R.id.restau_addr_layout);
		mRestauerrorItem = (RelativeLayout) mIntroduceView
				.findViewById(R.id.restau_error_layout);
		mRestauAddr = (TextView) mIntroduceView.findViewById(R.id.restau_addr);
		mRestauCall = (TextView) mIntroduceView.findViewById(R.id.restau_call);
		mRestauDescr = (TextView) mIntroduceView.findViewById(R.id.restau_desc);

		bundle = this.getArguments();
		mRestauName.setText(bundle.getString("RestauName"));
		mRestauScore.setText(bundle.getString("RestauScore"));
		mRestauAddr.setText(bundle.getString("RestauAddr"));
		mRestauCall.setText(bundle.getString("RestauCall"));
		mRestauDescr.setText(bundle.getString("RestauDescr"));
	}

	private void setListener() {
		mRestaucallItem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"
						+ bundle.getString("RestauCall")));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		mRestauaddrItem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), Restau_Map.class);
				intent.putExtra("RestauName", bundle.getString("RestauName"));
				intent.putExtra("RestauLat", bundle.getString("RestauLat"));
				intent.putExtra("RestauLon", bundle.getString("RestauLon"));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		mRestauerrorItem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String[] arrayWay = new String[] { "饭馆已关门", "饭馆电话或地址有误" };
				Dialog alertDialog = new AlertDialog.Builder(getActivity())
						.setTitle("饭馆纠错")
						.setItems(arrayWay,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										switch (which) {
										case 0:// 饭馆已关门
											new AsyncCloseTask().execute(bundle
													.getString("RestauName"));
											break;
										case 1:// 饭馆电话地址有误
											Intent intent = new Intent();
											intent.putExtra(
													"RestauName",
													bundle.getString("RestauName"));
											startActivity(intent.setClass(
													getActivity(),
													Restau_Error.class));
											getActivity()
													.overridePendingTransition(
															R.anim.roll_up,
															R.anim.roll);
											break;
										}
									}
								}).create();
				alertDialog.show();
			}
		});
	}

	public class AsyncCloseTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.restauClose(params[0]);
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
			// if (result.equals("110000")) {// 改动
			Toast.makeText(getActivity(), "提交成功，SoFun会尽快审核！", Toast.LENGTH_SHORT)
					.show();
			// } else {
			// Toast.makeText(getActivity(), "网络错误，请重试！", Toast.LENGTH_SHORT)
			// .show();
			// }
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}

	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(getActivity(),
					R.style.myProgressDialog);
			mProgress.setMessage("正在提交...");
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
