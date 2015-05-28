package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.AnimationTabHost;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;
import cn.xidianyaoyao.app.utils.HttpUtils;

public class Person_friendSee extends FragmentActivity implements
		OnCheckedChangeListener {

	private ProgressDialog mProgressAdd = null;
	private ProgressDialog mProgressDelete = null;

	private static final String TAB_COLLECT = "tab_collect";
	private static final String TAB_FOLLOWS = "tab_follows";
	private static final String TAB_FANS = "tab_fans";

	private ImageView mFriendSeeBack;// 返回
	private ImageView mAddBtn;
	private ImageView mAddedBtn;
	private ImageView mFriendImage;
	private TextView mFriendName;
	private TextView mFriendGender;

	private AnimationTabHost mTabHost;
	private RadioGroup mRadioGroup;
	private RadioButton mDefaultBtn;

	private FragmentManager mFrgManager;
	private FragmentTransaction mFrgTran;
	private Fragment mTabFragment;

	private PreferencesService preferencesService;
	private Map<String, String> params;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_friendsee);

		initView();
		setLister();

		initTabHost();
		initRadioGroup();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:// 登录成功
				// 判断它是否收藏过
			preferencesService = new PreferencesService(Person_friendSee.this);
			params = preferencesService.cusInfo_getPreferences();
			if (params.get("cusName").equals(
					getIntent().getStringExtra("FriendName"))) {
				Toast.makeText(Person_friendSee.this, "亲，是你自己哦！",
						Toast.LENGTH_SHORT).show();
			} else {
				new AsyncIsAddTask2().execute(params.get("cusName"),
						getIntent().getStringExtra("FriendName"));
			}
			break;
		default:
			break;
		}
	}

	private void initView() {
		mFriendSeeBack = (ImageView) findViewById(R.id.friendsee_back);
		mFriendImage = (ImageView) findViewById(R.id.friendsee_photo);
		mFriendName = (TextView) findViewById(R.id.friendsee_name);
		mFriendGender = (TextView) findViewById(R.id.friendsee_gender);

		asyncImageLoad(mFriendImage, HttpUtils.IP + "resources/upload/"
				+ getIntent().getStringExtra("FriendImage"));
		mFriendName.setText(getIntent().getStringExtra("FriendName"));
		if (getIntent().getStringExtra("FriendGender").equals("0")) {
			mFriendGender.setText("女");
		} else if (getIntent().getStringExtra("FriendGender").equals("1")) {
			mFriendGender.setText("男");
		}

		mAddBtn = (ImageView) findViewById(R.id.friendsee_add);
		mAddedBtn = (ImageView) findViewById(R.id.friendsee_added);
		mRadioGroup = (RadioGroup) findViewById(R.id.friendsee_group);

		preferencesService = new PreferencesService(this);
		params = preferencesService.cusInfo_getPreferences();
		if (params.get("cusName").equals("")) {
			mAddedBtn.setVisibility(View.GONE);
			mAddBtn.setVisibility(View.VISIBLE);
		} else if (!params.get("cusName").equals("")) {
			if (params.get("cusName").equals(
					getIntent().getStringExtra("FriendName"))) {
				Toast.makeText(Person_friendSee.this, "亲，是你自己哦！",
						Toast.LENGTH_SHORT).show();
			} else {
				new AsyncIsAddTask().execute(params.get("cusName"), getIntent()
						.getStringExtra("FriendName"));
			}
		}
	}

	private void setLister() {
		mFriendSeeBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		mAddBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preferencesService = new PreferencesService(
						Person_friendSee.this);
				params = preferencesService.cusInfo_getPreferences();
				if (params.get("cusName").equals("")) {
					Toast.makeText(Person_friendSee.this, "亲，你还没登录呢!",
							Toast.LENGTH_SHORT).show();
					startActivityForResult(new Intent(Person_friendSee.this,
							Person_login.class), 1);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else if (!params.get("cusName").equals("")) {
					if (params.get("cusName").equals(
							getIntent().getStringExtra("FriendName"))) {
						Toast.makeText(Person_friendSee.this, "亲，自己不能关注自己哦！",
								Toast.LENGTH_SHORT).show();
					} else {
						new AsyncAddTask().execute(params.get("cusName"),
								getIntent().getStringExtra("FriendName"));
					}
				}
			}
		});
		mAddedBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AsyncDeleteTask().execute(params.get("cusName"),
						getIntent().getStringExtra("FriendName"));
			}
		});
	}

	private void initTabHost() {
		mTabHost = (AnimationTabHost) findViewById(R.id.anim_tabhost);
		mTabHost.setup();

		TabSpec ts1 = mTabHost.newTabSpec(TAB_COLLECT)
				.setIndicator(TAB_COLLECT);
		ts1.setContent(new Intent(this, Fragment_friend_collected.class));
		mTabHost.addTab(ts1);

		TabSpec ts2 = mTabHost.newTabSpec(TAB_FOLLOWS)
				.setIndicator(TAB_FOLLOWS);
		ts2.setContent(new Intent(this, Fragment_friend_follows.class));
		mTabHost.addTab(ts2);

		TabSpec ts3 = mTabHost.newTabSpec(TAB_FANS).setIndicator(TAB_FANS);
		ts3.setContent(new Intent(this, Fragment_friend_fans.class));
		mTabHost.addTab(ts3);

		mTabHost.setCurrentTab(0);// 设置默认标签
	}

	private void initRadioGroup() {
		mRadioGroup = (RadioGroup) findViewById(R.id.friendsee_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mDefaultBtn = (RadioButton) findViewById(R.id.friendsee_collect);
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
		case R.id.friendsee_collect:
			mTabFragment = new Fragment_friend_collected();
			Bundle name_bundle = new Bundle();
			name_bundle.putString("FriendName",
					getIntent().getStringExtra("FriendName"));
			mTabFragment.setArguments(name_bundle);
			mTabHost.setCurrentTab(0);
			break;
		case R.id.friendsee_follows:
			mTabFragment = new Fragment_friend_follows();
			Bundle name1_bundle = new Bundle();
			name1_bundle.putString("FriendName",
					getIntent().getStringExtra("FriendName"));
			mTabFragment.setArguments(name1_bundle);
			mTabHost.setCurrentTab(1);
			break;
		case R.id.friendsee_fans:
			mTabFragment = new Fragment_friend_fans();
			Bundle name2_bundle = new Bundle();
			name2_bundle.putString("FriendName",
					getIntent().getStringExtra("FriendName"));
			mTabFragment.setArguments(name2_bundle);
			mTabHost.setCurrentTab(2);
			break;
		}
		mFrgTran.replace(android.R.id.tabcontent, mTabFragment).commit();
	}

	private void asyncImageLoad(ImageView imageView, String path) {
		AsyncHeadImageTask mHeadImageTask = new AsyncHeadImageTask(imageView);
		mHeadImageTask.execute(path);
	}

	private class AsyncHeadImageTask extends AsyncTask<String, Integer, byte[]> {
		private ImageView imageView;

		public AsyncHeadImageTask(ImageView imageView) {
			this.imageView = imageView;
		}

		protected byte[] doInBackground(String... params) {// 子线程中执行的
			try {
				return XidianYaoyaoApplication.mHttpUtils
						.getNoCacheImage(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(byte[] result) {// 运行在主线程
			if (result != null && imageView != null) {

				// BitmapFactory.Options opts = new BitmapFactory.Options();
				// opts.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(imageFile, opts);
				// opts.inSampleSize = BitmapTools.computeSampleSize(opts, -1,
				// 128 * 128);
				// opts.inJustDecodeBounds = false;
				// try {
				// Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
				// imageView.setImageBitmap(bmp);
				// } catch (OutOfMemoryError err) {
				// }

				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeByteArray(result, 0,
							result.length);
					imageView.setImageBitmap(bitmap);// 显示图片
				} catch (OutOfMemoryError e) {// 图片太大了就显示系统默认的头像
				}
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	/**************************************/
	public class AsyncDeleteTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.followsDelete(params[0], params[1]);
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
			stopProgressDelete();
			if (result.equals("110101")) {
				mAddedBtn.setVisibility(View.GONE);
				mAddBtn.setVisibility(View.VISIBLE);
				Toast.makeText(Person_friendSee.this, "取消关注成功",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Person_friendSee.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {// 该方法将在执行后台耗时操作前被调用，初始化，现实进度条等
			super.onPreExecute();
			startProgressDelete();
		}
	}

	/**************************************/
	public class AsyncAddTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.followsAdd(
						params[0], params[1]);
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
			stopProgressAdd();
			if (result.equals("110100")) {
				mAddBtn.setVisibility(View.GONE);
				mAddedBtn.setVisibility(View.VISIBLE);
				Toast.makeText(Person_friendSee.this, "添加关注成功",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Person_friendSee.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {// 该方法将在执行后台耗时操作前被调用，初始化，现实进度条等
			super.onPreExecute();
			startProgressAdd();
		}
	}

	/**************************************/
	public class AsyncIsAddTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.followsIsOrNot(params[0], params[1]);
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
			if (result.equals("110010")) {// 已关注
				Toast.makeText(Person_friendSee.this, "已经关注",
						Toast.LENGTH_SHORT).show();
				mAddBtn.setVisibility(View.GONE);
				mAddedBtn.setVisibility(View.VISIBLE);
			} else if (result.equals("110011")) {
				Toast.makeText(Person_friendSee.this, "还未关注！",
						Toast.LENGTH_SHORT).show();
				mAddedBtn.setVisibility(View.GONE);
				mAddBtn.setVisibility(View.VISIBLE);
			}
		}
	}

	/**************************************/
	public class AsyncIsAddTask2 extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.followsIsOrNot(params[0], params[1]);
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
			if (result.equals("110010")) {// 已关注
				Toast.makeText(Person_friendSee.this, "已经关注",
						Toast.LENGTH_SHORT).show();
				mAddBtn.setVisibility(View.GONE);
				mAddedBtn.setVisibility(View.VISIBLE);
			} else if (result.equals("110011")) {//未关注
				mAddedBtn.setVisibility(View.GONE);
				mAddBtn.setVisibility(View.VISIBLE);
				new AsyncAddTask().execute(params.get("cusName"), getIntent()
						.getStringExtra("FriendName"));
			}
		}
	}

	private void startProgressDelete() {
		if (mProgressDelete == null) {
			mProgressDelete = new ProgressDialog(this, R.style.myProgressDialog);
			mProgressDelete.setMessage("正在取消关注...");
		}
		mProgressDelete.show();
	}

	private void startProgressAdd() {
		if (mProgressAdd == null) {
			mProgressAdd = new ProgressDialog(this, R.style.myProgressDialog);
			mProgressAdd.setMessage("正在添加关注...");
		}
		mProgressAdd.show();
	}

	private void stopProgressDelete() {
		if (mProgressDelete != null) {
			mProgressDelete.dismiss();
			mProgressDelete = null;
		}
	}

	private void stopProgressAdd() {
		if (mProgressAdd != null) {
			mProgressAdd.dismiss();
			mProgressAdd = null;
		}
	}
}
