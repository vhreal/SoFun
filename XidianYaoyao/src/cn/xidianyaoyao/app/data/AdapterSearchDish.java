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

public class AdapterSearchDish extends BaseAdapter {

	private List<DataSearch> mDishSearchList;
	private Context mContext;
	private File cache;
	ItemHolder holder = null;

	public AdapterSearchDish(Context context, List<DataSearch> ld, File cache) {
		mContext = context;
		mDishSearchList = ld;
		this.cache = cache;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDishSearchList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDishSearchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// ItemHolder holder = null;
		DataSearch data = mDishSearchList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_search_dish_listview, null);
			holder.mDishSearchImage = (ImageView) convertView
					.findViewById(R.id.dish_search_image);
			holder.mDishSearchHonor = (ImageView) convertView
					.findViewById(R.id.dish_search_honor);
			holder.mDishSearchNumber = (TextView) convertView
					.findViewById(R.id.dish_search_number);
			holder.mDishSearchName = (TextView) convertView
					.findViewById(R.id.dish_search_name);
			holder.mDishSearchPrice = (TextView) convertView
					.findViewById(R.id.dish_search_price);
			holder.mDishSearchScore = (TextView) convertView
					.findViewById(R.id.dish_search_score);
			holder.mDishSearchTaste = (TextView) convertView
					.findViewById(R.id.dish_search_taste);
			holder.mDishSearchNutrition = (TextView) convertView
					.findViewById(R.id.dish_search_nutrition);
			holder.mDishSearchRestauName = (TextView) convertView
					.findViewById(R.id.dish_search_restau_name);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mDishSearchNumber.setText(data.getRank_number() + ".");
		holder.mDishSearchName.setText(data.getDish_name());
		holder.mDishSearchPrice.setText("单价：" + data.getDish_price());
		holder.mDishSearchScore.setText("评分：" + data.getDish_score());
		holder.mDishSearchTaste.setText("口味：" + data.getDish_taste());
		holder.mDishSearchNutrition.setText("营养：" + data.getDish_nutrition());
		holder.mDishSearchRestauName.setText("饭馆：" + data.getRestau_name());
		asyncImageLoad(holder.mDishSearchImage, HttpUtils.IP
				+ "resources/images/dishes/" + data.getDish_image());
		if (position == 0) {
			holder.mDishSearchHonor
					.setBackgroundResource(R.drawable.item_first);
		} else {
			holder.mDishSearchHonor
					.setBackgroundResource(R.drawable.item_praise);
		}
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
			// startDefault();
		}
	}

	// private void startDefault() {
	// holder.mDishSearchImage.setImageResource(R.drawable.image_dish_default);
	// }

	public final class ItemHolder {
		public ImageView mDishSearchImage;
		public ImageView mDishSearchHonor;
		public TextView mDishSearchNumber;
		public TextView mDishSearchName;
		public TextView mDishSearchPrice;
		public TextView mDishSearchScore;
		public TextView mDishSearchTaste;
		public TextView mDishSearchNutrition;
		public TextView mDishSearchRestauName;
	}
}
