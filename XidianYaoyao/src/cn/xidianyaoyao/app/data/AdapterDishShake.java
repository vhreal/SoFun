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

public class AdapterDishShake extends BaseAdapter {

	private List<DataDishShake> mDishShakedList;
	private Context mContext;
	private File cache;
	ItemHolder holder = null;

	public AdapterDishShake(Context context, List<DataDishShake> ld, File cache) {
		mContext = context;
		mDishShakedList = ld;
		this.cache = cache;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDishShakedList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDishShakedList.get(position);
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
		DataDishShake data = mDishShakedList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_dish_shake_listview, null);
			holder.mDishShakedImage = (ImageView) convertView
					.findViewById(R.id.dish_shaked_image);
			holder.mDishShakedName = (TextView) convertView
					.findViewById(R.id.dish_shaked_name);
			holder.mDishShakedPrice = (TextView) convertView
					.findViewById(R.id.dish_shaked_price);
			holder.mDishShakedScore = (TextView) convertView
					.findViewById(R.id.dish_shaked_score);
			holder.mDishShakedTaste = (TextView) convertView
					.findViewById(R.id.dish_shaked_taste);
			holder.mDishShakedNutrition = (TextView) convertView
					.findViewById(R.id.dish_shaked_nutrition);
			holder.mDishShakedRestauName = (TextView) convertView
					.findViewById(R.id.dish_shaked_restau_name);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mDishShakedName.setText(data.getDish_name());
		holder.mDishShakedPrice.setText("单价：" + data.getDish_price());
		holder.mDishShakedScore.setText("评分：" + data.getDish_score());
		holder.mDishShakedTaste.setText("口味：" + data.getDish_taste());
		holder.mDishShakedNutrition.setText("营养：" + data.getDish_nutrition());
		holder.mDishShakedRestauName.setText("饭馆：" + data.getRestau_name());
		asyncImageLoad(holder.mDishShakedImage, HttpUtils.IP
				+ "resources/images/dishes/" + data.getDish_image());
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
//		holder.mDishShakedImage.setImageResource(R.drawable.image_dish_default);
//	}

	public final class ItemHolder {
		public ImageView mDishShakedImage;
		public TextView mDishShakedName;
		public TextView mDishShakedPrice;
		public TextView mDishShakedScore;
		public TextView mDishShakedTaste;
		public TextView mDishShakedNutrition;
		public TextView mDishShakedRestauName;
	}
}
