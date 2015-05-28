package cn.xidianyaoyao.app.data;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.net.Uri;
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

public class AdapterDishCollected extends BaseAdapter {

	private List<DataDishCollected> mDishCollectedList;
	private Context mContext;
	private File cache;
	ItemHolder holder = null;

	public AdapterDishCollected(Context context, List<DataDishCollected> ld,
			File cache) {
		mContext = context;
		mDishCollectedList = ld;
		this.cache = cache;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDishCollectedList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDishCollectedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DataDishCollected data = mDishCollectedList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_dish_collected_listview, null);
			holder.mDishColletTime = (TextView) convertView
					.findViewById(R.id.collect_time);
			holder.mDishColletImage = (ImageView) convertView
					.findViewById(R.id.collect_image);
			holder.mDishColletDishName = (TextView) convertView
					.findViewById(R.id.collect_dishname);
			holder.mDishColletRestauName = (TextView) convertView
					.findViewById(R.id.collect_restauname);
			holder.mDishColletTaste = (TextView) convertView
					.findViewById(R.id.collect_taste);
			holder.mDishColletNutrition = (TextView) convertView
					.findViewById(R.id.collect_nutrition);
			holder.mDishColletScore = (TextView) convertView
					.findViewById(R.id.collect_score);
			holder.mDishColletPrice = (TextView) convertView
					.findViewById(R.id.collect_price);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mDishColletTime.setText(data.getTime());
		asyncImageLoad(holder.mDishColletImage, HttpUtils.IP
				+ "resources/images/dishes/" + data.getDish_image());
		holder.mDishColletDishName.setText(data.getDish_name());
		holder.mDishColletRestauName.setText("饭馆：" + data.getRestau_name());
		holder.mDishColletTaste.setText("口味：" + data.getDish_taste());
		holder.mDishColletNutrition.setText("营养：" + data.getDish_nutrition());
		holder.mDishColletScore.setText("评分：" + data.getDish_score());
		holder.mDishColletPrice.setText("单价：" + data.getDish_price());
		return convertView;
	}

	private void asyncImageLoad(ImageView imageView, String path) {
		AsyncDishImageTask mDishImageTask = new AsyncDishImageTask(imageView);
		mDishImageTask.execute(path);
	}

	private class AsyncDishImageTask extends AsyncTask<String, Integer, Uri> {
		private ImageView imageView;

		public AsyncDishImageTask(ImageView imageView) {
			this.imageView = imageView;
		}

		protected Uri doInBackground(String... params) {// 子线程中执行的
			try {
				return XidianYaoyaoApplication.mHttpUtils.getCacheImage(
						params[0], cache);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Uri result) {// 运行在主线程
			if (result != null && imageView != null) {
				imageView.setImageURI(result);
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
//			startDefault();
		}
	}

//	private void startDefault() {
//		holder.mDishColletImage.setImageResource(R.drawable.image_dish_default);
//	}

	public final class ItemHolder {
		public TextView mDishColletTime;
		public ImageView mDishColletImage;
		public TextView mDishColletDishName;
		public TextView mDishColletRestauName;
		public TextView mDishColletTaste;
		public TextView mDishColletNutrition;
		public TextView mDishColletScore;
		public TextView mDishColletPrice;
	}

}
