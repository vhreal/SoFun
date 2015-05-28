package cn.xidianyaoyao.app.ui.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import cn.xidianyaoyao.app.data.AdapterMorego;
import cn.xidianyaoyao.app.data.DataRankRestau;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.MoregoSQLiteHelper;
import cn.xidianyaoyao.app.ui.restaurant.Restau_info;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Person_morego extends Activity {

	private ImageView mMoregoBack;// 返回
	private ListView mMoregolistView;
	private LinearLayout mMoregoEmpty;

	private AdapterMorego adapter;
	private List<DataRankRestau> data;
	private boolean mToBottom = false;

	private PreferencesService preferencesService;
	private Map<String, String> params;
	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_morego);

		initView();
		setLister();

		preferencesService = new PreferencesService(this);
		params = preferencesService.cusInfo_getPreferences();

		MoregoSQLiteHelper helper = new MoregoSQLiteHelper(this,
				params.get("cusName"));
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery(
				"select * from " + "sqllcus" + params.get("cusName"), null);
		while (c.moveToNext()) {
			String res_id = c.getString(c.getColumnIndex("res_id"));
			String res_name = c.getString(c.getColumnIndex("res_name"));
			String res_score = c.getString(c.getColumnIndex("res_score"));
			String res_addr = c.getString(c.getColumnIndex("res_addr"));
			String res_call = c.getString(c.getColumnIndex("res_call"));
			String res_descr = c.getString(c.getColumnIndex("res_descr"));
			String res_lat = c.getString(c.getColumnIndex("res_lat"));
			String res_lon = c.getString(c.getColumnIndex("res_lon"));

			DataRankRestau dr = new DataRankRestau(res_id, res_name, res_score,
					res_addr, res_call, res_descr, res_lat, res_lon);
			data.add(dr);
		}
		adapter = new AdapterMorego(this, data);
		mMoregolistView.setAdapter(adapter);

		// 显示数据
		if (data.isEmpty()) {
			mMoregolistView.setVisibility(View.GONE);
			mMoregoEmpty.setVisibility(View.VISIBLE);
		} else {
			mMoregoEmpty.setVisibility(View.GONE);
			mMoregolistView.setVisibility(View.VISIBLE);
		}

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mMoregoBack = (ImageView) findViewById(R.id.morego_back);
		mMoregolistView = (ListView) this.findViewById(R.id.morego_listView);
		mMoregoEmpty = (LinearLayout) findViewById(R.id.morego_empty);

		data = new ArrayList<DataRankRestau>();
		mMoregolistView.setOnScrollListener(new ScrollListener());
	}

	private void setLister() {
		mMoregoBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		// 设置列表事件监听
		mMoregolistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();

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
				intent.setClass(Person_morego.this, Restau_info.class);
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