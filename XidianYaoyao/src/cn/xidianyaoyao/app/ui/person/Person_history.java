package cn.xidianyaoyao.app.ui.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterHistory;
import cn.xidianyaoyao.app.data.DataDishCollected;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.HistorySQLiteHelper;
import cn.xidianyaoyao.app.ui.restaurant.RestauDish_info;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Person_history extends Activity {

	private ImageView mhistoryBack;// 返回
	private ImageView mhistoryClear;// 清空
	private ListView mHistorylistView;
	private LinearLayout mHistoryEmpty;

	private AdapterHistory adapter;
	private List<DataDishCollected> data;
	private boolean mToBottom = false;

	private PreferencesService preferencesService;
	private Map<String, String> params;
	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_history);

		initView();
		setLister();

		preferencesService = new PreferencesService(this);
		params = preferencesService.cusInfo_getPreferences();

		HistorySQLiteHelper helper = new HistorySQLiteHelper(this, params.get("cusName"));
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery(
				"select * from " + "sqlcus" + params.get("cusName")
						+ " ORDER BY time", null);
		while (c.moveToNext()) {
			String time = c.getString(c.getColumnIndex("time"));
			String dish_id = c.getString(c.getColumnIndex("dish_id"));
			String dish_image = c.getString(c.getColumnIndex("dish_image"));
			String dish_name = c.getString(c.getColumnIndex("dish_name"));
			String dish_score = c.getString(c.getColumnIndex("dish_score"));
			String dish_price = c.getString(c.getColumnIndex("dish_price"));
			String dish_taste = c.getString(c.getColumnIndex("dish_taste"));
			String dish_nutrition = c.getString(c
					.getColumnIndex("dish_nutrition"));
			String res_id = c.getString(c.getColumnIndex("res_id"));
			String res_name = c.getString(c.getColumnIndex("res_name"));
			String res_score = c.getString(c.getColumnIndex("res_score"));
			String res_addr = c.getString(c.getColumnIndex("res_addr"));
			String res_call = c.getString(c.getColumnIndex("res_call"));
			String res_descr = c.getString(c.getColumnIndex("res_descr"));
			String res_lat = c.getString(c.getColumnIndex("res_lat"));
			String res_lon = c.getString(c.getColumnIndex("res_lon"));

			DataDishCollected dr = new DataDishCollected(time, dish_id,
					dish_image, dish_name, dish_score, dish_price, dish_taste,
					dish_nutrition, res_id, res_name, res_score, res_addr,
					res_call, res_descr, res_lat, res_lon);
			data.add(dr);
		}
		Collections.reverse(data);
		adapter = new AdapterHistory(this, data);
		mHistorylistView.setAdapter(adapter);

		// 显示数据
		if (data.isEmpty()) {
			mHistorylistView.setVisibility(View.GONE);
			mHistoryEmpty.setVisibility(View.VISIBLE);
		} else {
			mHistoryEmpty.setVisibility(View.GONE);
			mHistorylistView.setVisibility(View.VISIBLE);
		}

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mhistoryBack = (ImageView) findViewById(R.id.history_back);
		mhistoryClear = (ImageView) findViewById(R.id.history_clear);
		mHistorylistView = (ListView) this.findViewById(R.id.history_listView);
		mHistoryEmpty = (LinearLayout) findViewById(R.id.history_empty);

		data = new ArrayList<DataDishCollected>();
		mHistorylistView.setOnScrollListener(new ScrollListener());
	}

	private void setLister() {
		mhistoryBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		mhistoryClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Builder dialogBuidler = new Builder(Person_history.this);
				dialogBuidler.setMessage("确认清空最近浏览吗?");
				dialogBuidler.setPositiveButton("清空",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								preferencesService = new PreferencesService(
										Person_history.this);
								params = preferencesService
										.cusInfo_getPreferences();
								HistorySQLiteHelper helper = new HistorySQLiteHelper(
										Person_history.this, params
												.get("cusName"));
								db = helper.getWritableDatabase();// 获取可读写的数据库
								helper.DeleteData(db);
								data.clear();
								adapter.notifyDataSetChanged();
								mHistorylistView.setVisibility(View.GONE);
								mHistoryEmpty.setVisibility(View.VISIBLE);
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
			}
		});

		// 设置列表事件监听
		mHistorylistView.setOnItemClickListener(new OnItemClickListener() {
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
				intent.setClass(Person_history.this, RestauDish_info.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
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
}