package cn.xidianyaoyao.app.ui.restaurant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterRankDish;
import cn.xidianyaoyao.app.data.DataRankDish;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.HistorySQLiteHelper;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

/**
 * 
 * 返回前50名饭菜的排序
 * 
 * @author WangTanyun
 * 
 */
public class SearchDish_rank extends Activity {

	private ImageView mSearchDishBack;
	private ImageView mSearchDishRefresh;

	private int setDishLocation;// 每次刷新的最前一条可见item
	private int topDishOffset;// 每次刷新的最前一条可见item距离上面的偏移量

	private View mSeeMoreView; // 加载更多数据的Footer
	private View mLoadingView; // 加载更多数据的Footer
	private LinearLayout mFooterSeeMore;// Footer里的布局

	private File mRankDishImageCache;
	private LoadingProgressDialog mProgress = null;

	private ListView mDishRanklistView;
	private AdapterRankDish mDishAdapter;

	private int mDishRequestTimes = 0;// 请求次数
	private int mDishLimit = 5;// 每页显示的item的条数

	private boolean mToBottom = false;
	private int mRankDishNumber = 0;// 按序号排名

	private List<DataRankDish> dataDish;

	private PreferencesService preferencesService;
	private Map<String, String> params;
	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_dish_rank);

		initView();
		setListener();

		new AsyncDishRankTask().execute(String.valueOf(mDishRequestTimes),
				String.valueOf(mDishLimit));

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {

		mSearchDishBack = (ImageView) findViewById(R.id.search_dish_rank_back);
		mSearchDishRefresh = (ImageView) findViewById(R.id.search_dish_rank_refresh);
		mDishRanklistView = (ListView) this
				.findViewById(R.id.rankDish_listView);

		mSeeMoreView = getLayoutInflater().inflate(
				R.layout.widget_footer_seemore, null);
		mLoadingView = getLayoutInflater().inflate(
				R.layout.widget_footer_loading, null);
		mFooterSeeMore = (LinearLayout) mSeeMoreView
				.findViewById(R.id.footer_seeMore);

		dataDish = new ArrayList<DataRankDish>();
		mDishRanklistView.setOnScrollListener(new ScrollListener());

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// 判断有无SD卡
			mRankDishImageCache = new File(
					Environment.getExternalStorageDirectory(),// SD卡路径
					"xidianyaoyao_cache/dishImageCache");
			if (!mRankDishImageCache.exists())
				mRankDishImageCache.mkdirs();
		}
	}

	private void setListener() {
		mSearchDishBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		mSearchDishRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dataDish.clear();
				mDishRanklistView.removeFooterView(mSeeMoreView);
				mDishRequestTimes = 0;
				mRankDishNumber = 0;
				new AsyncDishRankTask().execute(
						String.valueOf(mDishRequestTimes),
						String.valueOf(mDishLimit));
			}
		});

		mDishRanklistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				preferencesService = new PreferencesService(
						SearchDish_rank.this);
				params = preferencesService.cusInfo_getPreferences();

				Intent intent = new Intent();
				intent.putExtra("DishId", dataDish.get(position).getDish_id());
				intent.putExtra("DishImage", dataDish.get(position)
						.getDish_image());
				intent.putExtra("DishName", dataDish.get(position)
						.getDish_name());
				intent.putExtra("DishScore", dataDish.get(position)
						.getDish_score());
				intent.putExtra("DishPrice", dataDish.get(position)
						.getDish_price());
				intent.putExtra("DishTaste", dataDish.get(position)
						.getDish_taste());
				intent.putExtra("DishNutrition", dataDish.get(position)
						.getDish_nutrition());
				intent.putExtra("RestauId", dataDish.get(position)
						.getRestau_id());
				intent.putExtra("RestauName", dataDish.get(position)
						.getRestau_name());
				intent.putExtra("RestauScore", dataDish.get(position)
						.getRestau_score());
				intent.putExtra("RestauAddr", dataDish.get(position)
						.getRestau_addr());
				intent.putExtra("RestauCall", dataDish.get(position)
						.getRestau_call());
				intent.putExtra("RestauDescr", dataDish.get(position)
						.getRestau_descr());
				intent.putExtra("RestauLat", dataDish.get(position)
						.getRestau_lat());
				intent.putExtra("RestauLon", dataDish.get(position)
						.getRestau_lon());
				intent.setClass(SearchDish_rank.this, RestauDish_info.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

				preferencesService = new PreferencesService(
						SearchDish_rank.this);
				params = preferencesService.cusInfo_getPreferences();
				if (!params.get("cusName").equals("")) {
					HistorySQLiteHelper helper = new HistorySQLiteHelper(
							SearchDish_rank.this, params.get("cusName"));
					db = helper.getWritableDatabase();// 获取可读写的数据库
					helper.InsertData(db, params.get("cusName"),
							dataDish.get(position).getDish_id(),
							dataDish.get(position).getDish_image(), dataDish
									.get(position).getDish_name(), dataDish
									.get(position).getDish_score(), dataDish
									.get(position).getDish_price(), dataDish
									.get(position).getDish_taste(), dataDish
									.get(position).getDish_nutrition(),
							dataDish.get(position).getRestau_id(), dataDish
									.get(position).getRestau_name(), dataDish
									.get(position).getRestau_score(), dataDish
									.get(position).getRestau_addr(), dataDish
									.get(position).getRestau_call(), dataDish
									.get(position).getRestau_descr(), dataDish
									.get(position).getRestau_lat(), dataDish
									.get(position).getRestau_lon());
				}
			}
		});

		mFooterSeeMore
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View v) {
						mDishRanklistView.removeFooterView(mSeeMoreView);
						mDishRequestTimes++;
						new AsyncDishRankTask2().execute(
								String.valueOf(mDishRequestTimes),
								String.valueOf(mDishLimit));
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
			if (scrollState == SCROLL_STATE_IDLE && mToBottom) {// SCROLL_STATE_IDLE滑动后静止
				setDishLocation = mDishRanklistView.getFirstVisiblePosition();
				topDishOffset = mDishRanklistView.getChildAt(0).getTop();
			}
		}
	}

	/*************************************************/
	public class AsyncDishRankTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.dishRank(
						params[0], params[1]);
				JSONObject dish_object = new JSONObject(result);
				String dishString = dish_object.getString("list");
				if (!dishString.equals("null")) {
					JSONArray resultCode = new JSONArray(dishString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataRankDish dr = new DataRankDish(
								String.valueOf(++mRankDishNumber), // 排名号，从1开始
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
						dataDish.add(dr);
						code = "0000";// 有数据
					}
				} else {
					code = "1111";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("0000")) {
				mDishAdapter = new AdapterRankDish(SearchDish_rank.this,
						dataDish, mRankDishImageCache);
				mDishRanklistView.addFooterView(mSeeMoreView);
				mDishRanklistView.setAdapter(mDishAdapter);
				mDishAdapter.notifyDataSetChanged();
				mDishRanklistView.setSelectionFromTop(setDishLocation,
						topDishOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(SearchDish_rank.this, "获取数据完毕！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(SearchDish_rank.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}
	}

	public class AsyncDishRankTask2 extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.dishRank(
						params[0], params[1]);
				JSONObject dish_object = new JSONObject(result);
				String dishString = dish_object.getString("list");
				if (!dishString.equals("null")) {
					JSONArray resultCode = new JSONArray(dishString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataRankDish dr = new DataRankDish(
								String.valueOf(++mRankDishNumber), // 排名号，从1开始
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
						dataDish.add(dr);
						code = "0000";// 有数据
					}
				} else {
					code = "1111";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			mDishRanklistView.removeFooterView(mLoadingView);
			if (result.equals("0000")) {
				// mDishAdapter = new
				// AdapterRankDish(SearchDish_rank.this,dataDish,
				// mRankDishImageCache);
				mDishRanklistView.addFooterView(mSeeMoreView);
				// mDishRanklistView.setAdapter(mDishAdapter);
				mDishAdapter.notifyDataSetChanged();
				mDishRanklistView.setSelectionFromTop(setDishLocation,
						topDishOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(SearchDish_rank.this, "获取数据完毕！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(SearchDish_rank.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			mDishRanklistView.addFooterView(mLoadingView);
		}
	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = LoadingProgressDialog
					.createDialog(SearchDish_rank.this);
			mProgress.setMessage("努力加载中...");
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
