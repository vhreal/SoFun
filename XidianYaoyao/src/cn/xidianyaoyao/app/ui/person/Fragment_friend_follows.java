package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import cn.xidianyaoyao.app.data.AdapterFriend;
import cn.xidianyaoyao.app.data.DataFriend;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;

public class Fragment_friend_follows extends Fragment {

	private View mFollowsView;
	private Bundle bundle;

	private LoadingProgressDialog mProgressLoad = null;

	private ListView mFollowslistView;
	private LinearLayout mFollowsEmpty;
	private AdapterFriend adapter;
	private List<DataFriend> data;
	private boolean mToBottom = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;

		mFollowsView = (View) inflater.inflate(
				R.layout.fragment_person_follows, container, false);

		initView();
		setLister();

		bundle = this.getArguments();
		new AsyncFollowsTask().execute(bundle.getString("FriendName"));

		return mFollowsView;
	}

	private void initView() {
		mFollowslistView = (ListView) mFollowsView
				.findViewById(R.id.follows_listView);
		mFollowsEmpty = (LinearLayout) mFollowsView
				.findViewById(R.id.follows_empty);

		data = new ArrayList<DataFriend>();
		mFollowslistView.setOnScrollListener(new ScrollListener());
	}

	private void setLister() {
		// 设置列表事件监听
		mFollowslistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("FriendName", data.get(position)
						.getFriend_name());
				intent.putExtra("FriendImage", data.get(position)
						.getFriend_image());
				intent.putExtra("FriendGender", data.get(position)
						.getFriend_gender());
				intent.setClass(getActivity(), Person_friendSee.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
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

	public class AsyncFollowsTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.followsShow(params[0]);

				JSONObject resultCode = new JSONObject(result);
				String followsInfo = resultCode.getString("list");
				if (!followsInfo.equals("null")) {// 有数据
					code = "1111";
					JSONArray follows = new JSONArray(followsInfo);
					for (int i = 0; i < follows.length(); i++) {
						JSONObject object = follows.getJSONObject(i);
						DataFriend dr = new DataFriend(
								object.getString("headimage"),
								object.getString("followed_id"),
								object.getString("gender"));
						data.add(dr);
					}
				} else {
					code = "0000";// 没有关注
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
				adapter = new AdapterFriend(getActivity(), data);
				mFollowslistView.setAdapter(adapter);
			} else if (result.equals("0000")) {
				mFollowslistView.setVisibility(View.GONE);
				mFollowsEmpty.setVisibility(View.VISIBLE);
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
