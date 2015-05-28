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

public class AdapterPersonRecom extends BaseAdapter {

	private List<DataPersonRecom> mPersonRecomList;
	private Context mContext;
	private File cache;
	ItemHolder holder = null;

	public AdapterPersonRecom(Context context, List<DataPersonRecom> ld,
			File cache) {
		mContext = context;
		mPersonRecomList = ld;
		this.cache = cache;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPersonRecomList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mPersonRecomList.get(position);
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
		DataPersonRecom data = mPersonRecomList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_dish_shake_listview, null);
			holder.mPersonRecomImage = (ImageView) convertView
					.findViewById(R.id.dish_shaked_image);
			holder.mPersonRecomName = (TextView) convertView
					.findViewById(R.id.dish_shaked_name);
			holder.mPersonRecomPrice = (TextView) convertView
					.findViewById(R.id.dish_shaked_price);
			holder.mPersonRecomScore = (TextView) convertView
					.findViewById(R.id.dish_shaked_score);
			holder.mPersonRecomTaste = (TextView) convertView
					.findViewById(R.id.dish_shaked_taste);
			holder.mPersonRecomNutrition = (TextView) convertView
					.findViewById(R.id.dish_shaked_nutrition);
			holder.mPersonRecomRestauName = (TextView) convertView
					.findViewById(R.id.dish_shaked_restau_name);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mPersonRecomName.setText(data.getDish_name());
		holder.mPersonRecomPrice.setText("单价：" + data.getDish_price());
		holder.mPersonRecomScore.setText("评分：" + data.getDish_score());
		holder.mPersonRecomTaste.setText("口味：" + data.getDish_taste());
		holder.mPersonRecomNutrition.setText("营养：" + data.getDish_nutrition());
		holder.mPersonRecomRestauName.setText("饭馆：" + data.getRestau_name());
		asyncImageLoad(holder.mPersonRecomImage, HttpUtils.IP
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
//		holder.mPersonRecomImage
//				.setImageResource(R.drawable.image_dish_default);
//	}

	public final class ItemHolder {
		public ImageView mPersonRecomImage;
		public TextView mPersonRecomName;
		public TextView mPersonRecomPrice;
		public TextView mPersonRecomScore;
		public TextView mPersonRecomTaste;
		public TextView mPersonRecomNutrition;
		public TextView mPersonRecomRestauName;
	}
}
