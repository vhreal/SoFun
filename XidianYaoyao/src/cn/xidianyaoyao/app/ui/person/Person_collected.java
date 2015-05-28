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

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
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

public class Person_collected extends Activity {

	private int deleteId;
	private ImageView mDishCollectedBack;// 返回

	private LoadingProgressDialog mProgressLoad = null;
	private ProgressDialog mProgressDelete = null;
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
		setContentView(R.layout.person_collected);

		initView();
		setLister();

		preferencesService = new PreferencesService(Person_collected.this);
		params = preferencesService.cusInfo_getPreferences();
		// 异步第一页
		new AsyncDishCollectTask().execute(params.get("cusName"));

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mDishCollectedBack = (ImageView) findViewById(R.id.dish_collected_back);
		mDishCollectlistView = (ListView) this
				.findViewById(R.id.dish_collected_listView);
		mCollectEmpty = (LinearLayout) findViewById(R.id.dish_empty_collect);

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
		mDishCollectedBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

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
				intent.setClass(Person_collected.this, RestauDish_info.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

				preferencesService = new PreferencesService(
						Person_collected.this);
				params = preferencesService.cusInfo_getPreferences();
				HistorySQLiteHelper helper = new HistorySQLiteHelper(
						Person_collected.this, params.get("cusName"));
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

		// 设置列表长按事件监听
		mDishCollectlistView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						deleteId = position;
						Builder dialogBuidler = new Builder(
								Person_collected.this);
						dialogBuidler.setMessage(data.get(position)
								.getRestau_name()
								+ "："
								+ data.get(position).getDish_name());
						dialogBuidler.setPositiveButton("删除收藏",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										preferencesService = new PreferencesService(
												Person_collected.this);
										params = preferencesService
												.cusInfo_getPreferences();
										new AsyncDeleteTask().execute(
												params.get("cusName"),
												data.get(deleteId).getDish_id(),
												data.get(deleteId)
														.getRestau_id());
									}
								});
						dialogBuidler.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						dialogBuidler.create().show();
						return true;
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
				adapter = new AdapterDishCollected(Person_collected.this, data,
						mCollectDishImageCache);
				mDishCollectlistView.setAdapter(adapter);
			} else if (result.equals("0000")) {
				mDishCollectlistView.setVisibility(View.GONE);
				mCollectEmpty.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(Person_collected.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressLoad();
		}
	}

	private void startProgressLoad() {
		if (mProgressLoad == null) {
			mProgressLoad = LoadingProgressDialog.createDialog(this);
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

	/**************************************/
	public class AsyncDeleteTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// 后台线程将要完成的任务
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.personNotCollect(params[0], params[1], params[2]);
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
			if (result.equals("100011")) {
				Toast.makeText(Person_collected.this, "删除收藏成功",
						Toast.LENGTH_SHORT).show();
				data.remove(deleteId);// 没有这句话的删除完listview中还会有
				adapter.notifyDataSetChanged();
				if (data.isEmpty()) {
					mDishCollectlistView.setVisibility(View.GONE);
					mCollectEmpty.setVisibility(View.VISIBLE);
				}
			} else {
				Toast.makeText(Person_collected.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {// 该方法将在执行后台耗时操作前被调用，初始化，现实进度条等
			super.onPreExecute();
			startProgressDelete();
		}
	}

	private void startProgressDelete() {
		if (mProgressDelete == null) {
			mProgressDelete = new ProgressDialog(this, R.style.myProgressDialog);
			mProgressDelete.setMessage("正在删除收藏...");
		}
		mProgressDelete.show();
	}

	private void stopProgressDelete() {
		if (mProgressDelete != null) {
			mProgressDelete.dismiss();
			mProgressDelete = null;
		}
	}
}