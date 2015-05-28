package cn.xidianyaoyao.app.ui.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterPersonRecom;
import cn.xidianyaoyao.app.data.DataPersonRecom;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.HistorySQLiteHelper;
import cn.xidianyaoyao.app.ui.person.Person_login;
import cn.xidianyaoyao.app.ui.restaurant.RestauDish_info;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Fragment_MainRecommend extends Fragment {

	private View mRecommendView;
	private RelativeLayout mLoginYes;
	private RelativeLayout mLoginNo;
	private ScrollView mTitleInfo;
	private RadioGroup mRecomGroup;
	private RadioButton mRecomDish;
	private RadioButton mRecomTitle;

	private LoadingProgressDialog mProgressLoad = null;
	private File mPersonRecomCache;

	private LinearLayout mPersonRecomEmpty;
	private ListView mPersonRecomlistView;
	private AdapterPersonRecom adapter;
	private List<DataPersonRecom> data;
	private boolean mToBottom = false;

	private PreferencesService preferencesService;
	private Map<String, String> params;
	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;
		mRecommendView = (View) inflater.inflate(
				R.layout.fragment_main_recommend3, container, false);

		initView();
		setListener();

		preferencesService = new PreferencesService(getActivity());
		params = preferencesService.cusInfo_getPreferences();
		if (params.get("cusName").equals("")) {
			mLoginYes.setVisibility(View.GONE);
			mLoginNo.setVisibility(View.VISIBLE);
		} else if (!params.get("cusName").equals("")) {
			mLoginNo.setVisibility(View.GONE);
			mLoginYes.setVisibility(View.VISIBLE);
			new AsyncPersonRecomTask().execute(params.get("cusName"));
		}
		return mRecommendView;
	}

	private void initView() {
		mRecomGroup = (RadioGroup) mRecommendView
				.findViewById(R.id.recom_group);
		mRecomDish = (RadioButton) mRecommendView.findViewById(R.id.recom_dish);
		mRecomTitle = (RadioButton) mRecommendView
				.findViewById(R.id.recom_title);
		mTitleInfo = (ScrollView) mRecommendView.findViewById(R.id.title_recom);
		mLoginYes = (RelativeLayout) mRecommendView
				.findViewById(R.id.layout_login_yes);
		mLoginNo = (RelativeLayout) mRecommendView
				.findViewById(R.id.layout_login_no);
		mPersonRecomEmpty = (LinearLayout) mRecommendView
				.findViewById(R.id.dish_empty_recom);
		mPersonRecomlistView = (ListView) mRecommendView
				.findViewById(R.id.recomDish_listView);

		data = new ArrayList<DataPersonRecom>();
		mPersonRecomlistView.setOnScrollListener(new ScrollListener());

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// 判断有无SD卡
			mPersonRecomCache = new File(
					Environment.getExternalStorageDirectory(),// SD卡路径
					"xidianyaoyao_cache/dishImageCache");
			if (!mPersonRecomCache.exists())
				mPersonRecomCache.mkdirs();
		}
	}

	private void setListener() {
		mRecomGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.recom_dish:
					mTitleInfo.setVisibility(View.GONE);
					new AsyncPersonRecomTask().execute(params.get("cusName"));
					break;
				case R.id.recom_title:
					mPersonRecomlistView.setVisibility(View.GONE);
					mPersonRecomEmpty.setVisibility(View.GONE);
					mTitleInfo.setVisibility(View.VISIBLE);
					break;
				}
			}
		});
		mLoginNo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),
						Person_login.class), 1);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		mPersonRecomlistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("DishId", data.get(position).getDish_id());
				intent.putExtra("DishImage", data.get(position).getDish_image());
				intent.putExtra("DishName", data.get(position).getDish_name());
				intent.putExtra("DishScore", data.get(position).getDish_score());
				intent.putExtra("DishPrice", data.get(position).getDish_price());
				intent.putExtra("DishTaste", data.get(position).getDish_taste());
				intent.putExtra("DishNutrition", data.get(position)
						.getDish_nutrition());
				intent.putExtra("RestauId", data.get(position).getRestau_id());
				intent.putExtra("RestauName", data.get(position)
						.getRestau_name());
				intent.putExtra("RestauScore", data.get(position)
						.getRestau_score());
				intent.putExtra("RestauAddr", data.get(position)
						.getRestau_addr());
				intent.putExtra("RestauCall", data.get(position)
						.getRestau_call());
				intent.putExtra("RestauDescr", data.get(position)
						.getRestau_descr());
				intent.putExtra("RestauLat", data.get(position).getRestau_lat());
				intent.putExtra("RestauLon", data.get(position).getRestau_lon());
				intent.setClass(getActivity(), RestauDish_info.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				HistorySQLiteHelper helper = new HistorySQLiteHelper(
						getActivity(), params.get("cusName"));
				db = helper.getWritableDatabase();// 获取可读写的数据库
				helper.InsertData(db, params.get("cusName"), data.get(position)
						.getDish_id(), data.get(position).getDish_image(), data
						.get(position).getDish_name(), data.get(position)
						.getDish_score(), data.get(position).getDish_price(),
						data.get(position).getDish_taste(), data.get(position)
								.getDish_nutrition(), data.get(position)
								.getRestau_id(), data.get(position)
								.getRestau_name(), data.get(position)
								.getRestau_score(), data.get(position)
								.getRestau_addr(), data.get(position)
								.getRestau_call(), data.get(position)
								.getRestau_descr(), data.get(position)
								.getRestau_lat(), data.get(position)
								.getRestau_lon());
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:// 登录成功
			mLoginNo.setVisibility(View.GONE);
			mLoginYes.setVisibility(View.VISIBLE);
			new AsyncPersonRecomTask().execute(params.get("cusName"));
			break;
		default:
			break;
		}
	}

	private final class ScrollListener implements OnScrollListener {

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int lastVisibleItemIndex = firstVisibleItem + visibleItemCount - 1;// 获取最后一个可见Item的索引(0-based)
			if (totalItemCount - 1 == lastVisibleItemIndex) {
				mToBottom = true;
			} else {
				mToBottom = false;
			}
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE && mToBottom) {
			}
		}
	}

	public class AsyncPersonRecomTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.personRecom(params[0]);

				JSONObject resultCode = new JSONObject(result);
				String recomsInfo = resultCode.getString("list");
				if (!recomsInfo.equals("null")) {// 有数据
					code = "1111";
					JSONArray recoms = new JSONArray(recomsInfo);
					for (int i = 0; i < recoms.length(); i++) {
						JSONObject object = recoms.getJSONObject(i);
						DataPersonRecom dr = new DataPersonRecom(
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
					code = "0000";// 没有推荐
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
			stopProgressLoad();
			if (result.equals("1111")) {
				mPersonRecomEmpty.setVisibility(View.GONE);
				mPersonRecomlistView.setVisibility(View.VISIBLE);
				adapter = new AdapterPersonRecom(getActivity(), data,
						mPersonRecomCache);
				mPersonRecomlistView.setAdapter(adapter);
			} else if (result.equals("0000")) {
				mPersonRecomlistView.setVisibility(View.GONE);
				mPersonRecomEmpty.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "网络错误，请重试！", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressLoad();
		}
	}

	private void startProgressLoad() {
		if (mProgressLoad == null) {
			mProgressLoad = LoadingProgressDialog.createDialog(getActivity());
			mProgressLoad.setMessage("努力加载中...");
		}
		mProgressLoad.show();
	}

	private void stopProgressLoad() {
		if (mProgressLoad != null) {
			mProgressLoad.dismiss();
			mProgressLoad = null;
		}
	}
}
