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

public class AdapterRankDish extends BaseAdapter {

	private List<DataRankDish> mRankDishList;
	private Context mContext;
	private ItemHolder holder = null;
	private File cache;

	public AdapterRankDish(Context context, List<DataRankDish> rankDishList,
			File cache) {
		mContext = context;
		mRankDishList = rankDishList;
		this.cache = cache;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRankDishList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mRankDishList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DataRankDish data = mRankDishList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_rankdish_listview, null);
			holder.mNumber = (TextView) convertView
					.findViewById(R.id.rankdish_dish_number);
			holder.mHonorImage = (ImageView) convertView
					.findViewById(R.id.rank_dish_item_honor);
			holder.mDishImage = (ImageView) convertView
					.findViewById(R.id.rankdish_dish_image);
			holder.mDishName = (TextView) convertView
					.findViewById(R.id.rankdish_dish_name);
			holder.mRestauName = (TextView) convertView
					.findViewById(R.id.rankdish_restau_name);
			holder.mDishPrice = (TextView) convertView
					.findViewById(R.id.rankdish_price);
			holder.mDishScore = (TextView) convertView
					.findViewById(R.id.rankdish_score);

			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		asyncImageLoad(holder.mDishImage, HttpUtils.IP
				+ "resources/images/dishes/" + data.getDish_image());
		holder.mNumber.setText(data.getRank_number() + ".");
		holder.mDishName.setText(data.getDish_name());
		holder.mRestauName.setText("饭馆：" + data.getRestau_name());
		holder.mDishPrice.setText("单价：" + data.getDish_price());
		holder.mDishScore.setText("评分：" + data.getDish_score());
		if (position == 0) {
			holder.mHonorImage.setBackgroundResource(R.drawable.item_first);
		} else {
			holder.mHonorImage.setBackgroundResource(R.drawable.item_praise);
		}
		return convertView;
	}

	private void asyncImageLoad(ImageView imageView, String path) {
		AsyncDishImageTask mDishImageTask = new AsyncDishImageTask(imageView);
		mDishImageTask.execute(path);
	}

	private final class AsyncDishImageTask extends
			AsyncTask<String, Integer, Uri> {
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
//		holder.mDishImage.setImageResource(R.drawable.image_dish_default);
//	}

	public final class ItemHolder {
		public TextView mNumber;
		public ImageView mHonorImage;
		public ImageView mDishImage;
		public TextView mDishName;
		public TextView mRestauName;
		public TextView mDishPrice;
		public TextView mDishScore;
	}
}
