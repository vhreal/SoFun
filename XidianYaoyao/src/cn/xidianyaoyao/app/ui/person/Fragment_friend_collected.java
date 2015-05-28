package cn.xidianyaoyao.app.ui.person;

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
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterDishCollected;
import cn.xidianyaoyao.app.data.DataDishCollected;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.HistorySQLiteHelper;
import cn.xidianyaoyao.app.ui.restaurant.RestauDish_info;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Fragment_friend_collected extends Fragment {

	private View mCollectedView;
	private Bundle bundle;

	private LoadingProgressDialog mProgressLoad = null;
	private File mCollectDishImageCache;

	private ListView mDishCollectlistView;
	private LinearLayout mCollectEmpty;

	private AdapterDishCollected adapter;
	private boolean mToBottom = false;
	private List<DataDishCollected> data;

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

		mCollectedView = (View) inflater.inflate(
				R.layout.fragment_friend_collected, container, false);

		initView();
		setLister();

		bundle = this.getArguments();
		// 异步第一页
		new AsyncDishCollectTask().execute(bundle.getString("FriendName"));

		return mCollectedView;
	}

	private void initView() {
		mDishCollectlistView = (ListView) mCollectedView
				.findViewById(R.id.dish_collected_listView);
		mCollectEmpty = (LinearLayout) mCollectedView
				.findViewById(R.id.dish_empty_collect);

		data = new ArrayList<DataDishCollected>();
		mDishCollectlistView.setOnScrollListener(new ScrollListener());

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// 判断有无SD卡
			mCollectDishImageCache = new File(
					Environment.getExternalStorageDirectory(),// SD卡路径
					"xidianyaoyao_cache/dishImageCache");
			if (!mCollectDishImageCache.exists())
				mCollectDishImageCache.mkdirs();
		}
	}

	private void setLister() {
		// 设置列表事件监听
		mDishCollectlistView.setOnItemClickListener(new OnItemClickListener() {
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
				if (!params.get("cusName").equals("")) {
					HistorySQLiteHelper helper = new HistorySQLiteHelper(getActivity(),
							params.get("cusName"));
					db = helper.getWritableDatabase();// 获取可读写的数据库
					helper.InsertData(db, params.get("cusName"),
							data.get(position).getDish_id(), data.get(position)
									.getDish_image(), data.get(position)
									.getDish_name(), data.get(position)
									.getDish_score(), data.get(position)
									.getDish_price(), data.get(position)
									.getDish_taste(), data.get(position)
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
			}
		});
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

	public class AsyncDishCollectTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.dishCollectShow(params[0]);

				JSONObject resultCode = new JSONObject(result);
				int totalItems = Integer.parseInt(resultCode
						.getString("totalProperty"));// 总条数
				if (totalItems == 0) {
					code = "0000";// 本来就无数据
				} else {
					code = "1111";
					String collectInfo = resultCode.getString("collections");

					JSONArray resultCollects = new JSONArray(collectInfo);
					for (int i = 0; i < resultCollects.length(); i++) {
						JSONObject object = resultCollects.getJSONObject(i);
						DataDishCollected dr = new DataDishCollected(
								object.getString("collecttime"),
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
				adapter = new AdapterDishCollected(getActivity(), data,
						mCollectDishImageCache);
				mDishCollectlistView.setAdapter(adapter);
			} else if (result.equals("0000")) {
				mDishCollectlistView.setVisibility(View.GONE);
				mCollectEmpty.setVisibility(View.VISIBLE);
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
