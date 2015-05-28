package cn.xidianyaoyao.app.data;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.utils.HttpUtils;

public class AdapterDishEvaulate extends BaseAdapter {

	private List<DataDishEvaluate> mEvauList;
	private Context mContext;
	private ItemHolder holder = null;

	public AdapterDishEvaulate(Context context, List<DataDishEvaluate> ld) {
		mContext = context;
		mEvauList = ld;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mEvauList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mEvauList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DataDishEvaluate data = mEvauList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_dish_evaluate_listview, null);
			holder.mEvauHead = (ImageView) convertView
					.findViewById(R.id.evau_head);
			holder.mEvauName = (TextView) convertView
					.findViewById(R.id.evau_name);
			holder.mEvauTime = (TextView) convertView
					.findViewById(R.id.evau_id);
			holder.mEvauScore = (TextView) convertView
					.findViewById(R.id.evau_score);
			holder.mEvauSummary = (TextView) convertView
					.findViewById(R.id.evau_summary);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		asyncImageLoad(holder.mEvauHead, HttpUtils.IP +
				 "resources/upload/"+data.getHead());
		holder.mEvauName.setText(data.getName());
		holder.mEvauTime.setText(data.getTime());
		holder.mEvauScore.setText(data.getScore());
		holder.mEvauSummary.setText(data.getSummary());
		return convertView;
	}

	public final class ItemHolder {
		public ImageView mEvauHead;
		public TextView mEvauName;
		public TextView mEvauTime;
		public TextView mEvauScore;
		public TextView mEvauSummary;
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
//			startDefault();
		}
	}

//	private void startDefault() {
//		holder.mEvauHead.setImageResource(R.drawable.image_head_default);
//	}
}
