package cn.xidianyaoyao.app.ui.restaurant;

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
import cn.xidianyaoyao.app.data.AdapterRestauRecom;
import cn.xidianyaoyao.app.data.DataRestauRecom;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.HistorySQLiteHelper;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Fragment_Restau_Recom extends Fragment {

	private View mRecomView;
	private Bundle bundle;

	private int setLocation;// 每次刷新的最前一条可见item
	private int topOffset;// 每次刷新的最前一条可见item距离上面的偏移量

	private LoadingProgressDialog mProgressLoad = null;
	private View mSeeMoreView; // 加载更多数据的Footer
	private View mLoadingView; // 加载更多数据的Footer
	private LinearLayout mFooterSeeMore;// Footer里的布局

	private File mRestauRecomImageCache;
	private ListView mRestauRecomlistView;

	private AdapterRestauRecom adapter;
	private int mRequestTimes = 0;// 请求次数
	private int mLimit = 5;// 每页显示的item的条数

	private boolean mToBottom = false;
	private List<DataRestauRecom> data;

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

		mRecomView = (View) inflater.inflate(R.layout.fragment_restau_recom,
				container, false);

		initView();
		setLister();

		bundle = this.getArguments();
		new AsyncRestauRecomTask().execute(bundle.getString("RestauId"),
				String.valueOf(mRequestTimes), String.valueOf(mLimit));

		return mRecomView;
	}

	private void initView() {
		mRestauRecomlistView = (ListView) mRecomView
				.findViewById(R.id.restauRecom_listView);

		mSeeMoreView = getActivity().getLayoutInflater().inflate(
				R.layout.widget_footer_seemore, null);
		mLoadingView = getActivity().getLayoutInflater().inflate(
				R.layout.widget_footer_loading, null);
		mFooterSeeMore = (LinearLayout) mSeeMoreView
				.findViewById(R.id.footer_seeMore);

		data = new ArrayList<DataRestauRecom>();
		mRestauRecomlistView.setOnScrollListener(new ScrollListener());

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// 判断有无SD卡
			mRestauRecomImageCache = new File(
					Environment.getExternalStorageDirectory(),// SD卡路径
					"xidianyaoyao_cache/dishImageCache");
			if (!mRestauRecomImageCache.exists())
				mRestauRecomImageCache.mkdirs();
		}
	}

	private void setLister() {
		mFooterSeeMore
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View v) {
						mRestauRecomlistView.removeFooterView(mSeeMoreView);
						mRequestTimes++;
						new AsyncRestauRecomTask2().execute(
								bundle.getString("RestauId"),
								String.valueOf(mRequestTimes),
								String.valueOf(mLimit));
					}
				});

		// 设置列表事件监听
		mRestauRecomlistView.setOnItemClickListener(new OnItemClickListener() {
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
				setLocation = mRestauRecomlistView.getFirstVisiblePosition();
				topOffset = mRestauRecomlistView.getChildAt(0).getTop();
			}
		}
	}

	public class AsyncRestauRecomTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.restauRecom(
						params[0], params[1], params[2]);

				JSONObject resultCode = new JSONObject(result);
				String recomInfo = resultCode.getString("list");
				if (!recomInfo.equals("null")) {// 有数据
					code = "1111";
					JSONArray resultRecoms = new JSONArray(recomInfo);
					for (int i = 0; i < resultRecoms.length(); i++) {
						JSONObject object = resultRecoms.getJSONObject(i);
						DataRestauRecom dr = new DataRestauRecom(
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
					code = "2222";// 数据获取完毕
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
				adapter = new AdapterRestauRecom(getActivity(), data,
						mRestauRecomImageCache);
				mRestauRecomlistView.addFooterView(mSeeMoreView);
				mRestauRecomlistView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mRestauRecomlistView
						.setSelectionFromTop(setLocation, topOffset);
			} else if (result.equals("2222")) {
				Toast.makeText(getActivity(), "数据获取完毕！", Toast.LENGTH_SHORT)
						.show();
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

	public class AsyncRestauRecomTask2 extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.restauRecom(
						params[0], params[1], params[2]);

				JSONObject resultCode = new JSONObject(result);
				String recomInfo = resultCode.getString("list");
				if (!recomInfo.equals("null")) {// 有数据
					code = "1111";
					JSONArray resultRecoms = new JSONArray(recomInfo);
					for (int i = 0; i < resultRecoms.length(); i++) {
						JSONObject object = resultRecoms.getJSONObject(i);
						DataRestauRecom dr = new DataRestauRecom(
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
					code = "2222";// 数据获取完毕
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
			mRestauRecomlistView.removeFooterView(mLoadingView);
			if (result.equals("1111")) {
				// adapter = new AdapterRestauRecom(getActivity(),
				// data,mRestauRecomImageCache);
				mRestauRecomlistView.addFooterView(mSeeMoreView);
				// mRestauRecomlistView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mRestauRecomlistView
						.setSelectionFromTop(setLocation, topOffset);
			} else if (result.equals("2222")) {
				Toast.makeText(getActivity(), "数据获取完毕！", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "网络错误，请重试！", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			mRestauRecomlistView.addFooterView(mLoadingView);
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
