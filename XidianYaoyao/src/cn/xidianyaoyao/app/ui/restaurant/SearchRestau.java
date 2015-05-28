package cn.xidianyaoyao.app.ui.restaurant;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterSearchRestau;
import cn.xidianyaoyao.app.data.DataSearch;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

/**
 * 
 * 返回前20名饭馆的排序
 * 
 * @author WangTanyun
 *
 */
public class SearchRestau extends Activity {

	private ImageView mRestauBack;// 返回
	private TextView mTitleName;

	private ListView mRestaulistView;
	private AdapterSearchRestau adapter;

	private List<DataSearch> data;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_restau);

		initView();
		setLister();
		
		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		/**
		 * listview动画
		 */
		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // 控制渐变透明的动画效果
		animation.setDuration(250); // 动画时间毫秒数
		set.addAnimation(animation); // 加入动画集合
		LayoutAnimationController controller = new LayoutAnimationController(set, 1);
		
		mRestauBack = (ImageView) findViewById(R.id.restaurant_back);
		mTitleName = (TextView) findViewById(R.id.search_restauName);

		mRestaulistView = (ListView) findViewById(R.id.restaurant_listView);
		mRestaulistView.setLayoutAnimation(controller); // ListView 设置动画效果

		data = (List<DataSearch>) getIntent().getSerializableExtra("data");
		adapter = new AdapterSearchRestau(this, data);
		mRestaulistView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		mTitleName.setText(data.get(0).getRestau_name());
	}

	private void setLister() {
		mRestauBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		// 设置列表事件监听
		mRestaulistView.setOnItemClickListener(new OnItemClickListener() {
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
				intent.setClass(SearchRestau.this, Restau_info.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}
}