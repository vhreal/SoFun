package cn.xidianyaoyao.app.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.DataDishShake;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.main.Fragment_MainRank.AsyncDishRankTask;
import cn.xidianyaoyao.app.ui.main.Fragment_MainRank.AsyncRestauRankTask;
import cn.xidianyaoyao.app.ui.shake.Shake_group_dialog;
import cn.xidianyaoyao.app.ui.shake.Shake_select_dialog;
import cn.xidianyaoyao.app.ui.shake.shaked_dish;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;
import cn.xidianyaoyao.app.ui.widget.ShakeListener;
import cn.xidianyaoyao.app.ui.widget.ShakeListener.OnShakeListener;

public class Fragment_MainShake extends Fragment {

	private ShakeListener mShakeListener = null;
	private Vibrator mVibrator = null;

	private ProgressDialog mProgress;
	private List<DataDishShake> data;

	private View mShakeView;

	private ImageView mShakeSet;
	private ImageView mShakeTu;
	private RadioGroup mShake;
	private RadioButton mShakeSelect;
	private RadioButton mShakeGroup;

	private PreferencesService preferencesService;
	private String sSelectShakePrice;
	private String sSelectShakeScore;
	private String sGroupShakePrice;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mVibrator = (Vibrator) getActivity().getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);
		mShakeListener = new ShakeListener(getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;

		mShakeView = (View) inflater.inflate(R.layout.fragment_main_shake,
				container, false);

		initView();
		setListener();
		yaoyao();

		return mShakeView;
	}

	private void initView() {
		mShakeSet = (ImageView) mShakeView.findViewById(R.id.main_shake_set);

		mShake = (RadioGroup) mShakeView.findViewById(R.id.shake_group);
		mShakeSelect = (RadioButton) mShakeView.findViewById(R.id.shake_select);
		mShakeGroup = (RadioButton) mShakeView
				.findViewById(R.id.shake_groupmeal);

		mShakeTu = (ImageView) mShakeView.findViewById(R.id.shake_tu);
		preferencesService = new PreferencesService(getActivity());
		startShakeAnimation(mShakeTu);
	}

	private void setListener() {
		mShakeSet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(),
						Main_set.class));
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		mShakeTu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				yaoyaoSet();
			}
		});

		mShake.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.shake_select:
					startActivity(new Intent().setClass(getActivity(),
							Shake_select_dialog.class));
					break;
				case R.id.shake_groupmeal:
					startActivity(new Intent().setClass(getActivity(),
							Shake_group_dialog.class));
					break;
				}
			}
		});
	}

	public void yaoyao() {
		mShakeListener.start();
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				yaoyaoSet();
			}
		});
	}

	public void yaoyaoSet() {
		mShakeListener.stop();
		if (preferencesService.voiceSet_getPreferences()
				&& preferencesService.vibrateSet_getPreferences()) {
			loadSound();
			startVibrato();
		} else if (preferencesService.voiceSet_getPreferences()
				&& !preferencesService.vibrateSet_getPreferences()) {
			loadSound();
		} else if (!preferencesService.voiceSet_getPreferences()
				&& preferencesService.vibrateSet_getPreferences()) {
			startVibrato();
		} else if (!preferencesService.voiceSet_getPreferences()
				&& !preferencesService.vibrateSet_getPreferences()) {
		}

		if (mShakeSelect.isChecked()) {
			if (preferencesService.shakePrice_getPreferences().equals("不限")) {
				sSelectShakePrice = "0";
			} else if (preferencesService.shakePrice_getPreferences().equals(
					"<10")) {
				sSelectShakePrice = "1";
			} else if (preferencesService.shakePrice_getPreferences().equals(
					"10-20")) {
				sSelectShakePrice = "2";
			} else if (preferencesService.shakePrice_getPreferences().equals(
					"20-30")) {
				sSelectShakePrice = "3";
			} else if (preferencesService.shakePrice_getPreferences().equals(
					"30-40")) {
				sSelectShakePrice = "4";
			} else if (preferencesService.shakePrice_getPreferences().equals(
					"40-50")) {
				sSelectShakePrice = "5";
			} else if (preferencesService.shakePrice_getPreferences().equals(
					">50")) {
				sSelectShakePrice = "6";
			}

			if (preferencesService.shakeScore_getPreferences().equals("不限")) {
				sSelectShakeScore = "0";
			} else {
				sSelectShakeScore = preferencesService
						.shakeScore_getPreferences();
			}

			new AsyncShakeSelectTask().execute(
					preferencesService.shakeItem_getPreferences(),
					sSelectShakePrice, sSelectShakeScore,
					preferencesService.shakeTaste_getPreferences(),
					preferencesService.shakeNutrition_getPreferences());

		} else if (mShakeGroup.isChecked()) {
			if (preferencesService.shakeGroupPrice_getPreferences()
					.equals("不限")) {
				sGroupShakePrice = "0";
			} else if (preferencesService.shakeGroupPrice_getPreferences()
					.equals("<100")) {
				sGroupShakePrice = "1";
			} else if (preferencesService.shakeGroupPrice_getPreferences()
					.equals("100-200")) {
				sGroupShakePrice = "2";
			} else if (preferencesService.shakeGroupPrice_getPreferences()
					.equals("200-300")) {
				sGroupShakePrice = "3";
			} else if (preferencesService.shakeGroupPrice_getPreferences()
					.equals("300-400")) {
				sGroupShakePrice = "4";
			} else if (preferencesService.shakeGroupPrice_getPreferences()
					.equals("400-500")) {
				sGroupShakePrice = "5";
			} else if (preferencesService.shakeGroupPrice_getPreferences()
					.equals(">500")) {
				sGroupShakePrice = "6";
			}

			if (preferencesService.shakeRestau_getPreferences().equals("")) {
				Toast.makeText(getActivity(), "没有输入消费的饭馆！", Toast.LENGTH_SHORT)
						.show();
			} else {
				new AsyncShakeGroupTask().execute(
						preferencesService.shakeRestau_getPreferences(),
						sGroupShakePrice,
						preferencesService.shakeNumber_getPreferences());
			}
		}
	}

	public class AsyncShakeSelectTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			data = new ArrayList<DataDishShake>();
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.shake_select(params[0], params[1], params[2],
								params[3], params[4]);
				JSONObject resultDishs = new JSONObject(result);
				String dishString = resultDishs.getString("dishes");
				if (!dishString.equals("null")) {
					code = "0000";
					JSONArray resultCode = new JSONArray(dishString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataDishShake dr = new DataDishShake(
								object.getString("dis_id"),
								object.getString("imageUrl"),
								object.getString("name"),
								object.getString("dishscore"),
								object.getString("price"),
								object.getString("taste"),
								object.getString("nutrition"),
								object.getString("res_id"),
								object.getString("resname"),
								object.getString("restscore"),
								object.getString("addr"),
								object.getString("telephone"),
								object.getString("restdescr"),
								object.getString("lat"),
								object.getString("lng"));
						data.add(dr);
					}
				} else {
					code = "1111";// 没有符合条件的数据
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("0000")) {
				Intent intent = new Intent();
				intent.putExtra("data", (ArrayList<DataDishShake>) data);
				intent.setClass(getActivity(), shaked_dish.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.roll_up,
						R.anim.roll);
			} else if (result.equals("1111")) {
				mShakeListener.start();
				Toast.makeText(getActivity(), "没有符合条件的数据！", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "网络错误，请重试！", Toast.LENGTH_SHORT)
						.show();
				mShakeListener.start();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}
	}

	public class AsyncShakeGroupTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			data = new ArrayList<DataDishShake>();
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.shake_group(
						params[0], params[1], params[2]);
				JSONObject resultDishs = new JSONObject(result);
				String dishString = resultDishs.getString("list");
				if (!dishString.equals("null")) {
					code = "0000";
					JSONArray resultCode = new JSONArray(dishString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataDishShake dr = new DataDishShake(
								object.getString("dis_id"),
								object.getString("imageUrl"),
								object.getString("name"),
								object.getString("dishscore"),
								object.getString("price"),
								object.getString("taste"),
								object.getString("nutrition"),
								object.getString("res_id"),
								object.getString("resname"),
								object.getString("restscore"),
								object.getString("addr"),
								object.getString("telephone"),
								object.getString("restdescr"),
								object.getString("lat"),
								object.getString("lng"));
						data.add(dr);
					}
				} else {
					code = "1111";// 没有符合条件的数据
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("0000")) {
				Intent intent = new Intent();
				intent.putExtra("data", (ArrayList<DataDishShake>) data);
				intent.setClass(getActivity(), shaked_dish.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.roll_up,
						R.anim.roll);
			} else if (result.equals("1111")) {
				mShakeListener.start();
				Toast.makeText(getActivity(), "没有符合条件的数据！", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "网络错误，请重试！", Toast.LENGTH_SHORT)
						.show();
				mShakeListener.start();
			}
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
			mProgress.setMessage("正在摇菜中...");
		}
		mProgress.show();
	}

	private void stopProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}

	/***********************************/

	private void loadSound() {// 定义摇动声音
		MediaPlayer mMediaPlayer = new MediaPlayer();
		mMediaPlayer.reset();
		try {
			AssetFileDescriptor fileDescriptor = getActivity().getAssets()
					.openFd("sound/wanpi.ogg");
			mMediaPlayer
					.setDataSource(fileDescriptor.getFileDescriptor(),
							fileDescriptor.getStartOffset(),
							fileDescriptor.getLength());
			mMediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();
	}

	public void startShakeAnimation(View v) { // 抖动动画
		int pivot = Animation.RELATIVE_TO_SELF;
		CycleInterpolator interpolator = new CycleInterpolator(3.0f);
		RotateAnimation animation = new RotateAnimation(0, 15, pivot, 0.5f,
				pivot, 0.5f);
		animation.setStartOffset(1000); // 抖动间隔时间1s
		animation.setDuration(1000); // 抖动时间1s
		animation.setRepeatCount(Animation.INFINITE);
		animation.setInterpolator(interpolator);
		v.startAnimation(animation);
	}

	public void startVibrato() { // 定义震动
		// 第一个｛｝里面是节奏数组，第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
	}

	public void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}

	public void onResume() {
		super.onResume();
		mShakeListener.start();
	}
}
