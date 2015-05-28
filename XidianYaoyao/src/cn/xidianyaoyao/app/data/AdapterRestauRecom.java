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

public class AdapterRestauRecom extends BaseAdapter {

	private List<DataRestauRecom> mRestauRecomList;
	private Context mContext;
	private File cache;
	ItemHolder holder = null;

	public AdapterRestauRecom(Context context, List<DataRestauRecom> ld,
			File cache) {
		mContext = context;
		mRestauRecomList = ld;
		this.cache = cache;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRestauRecomList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mRestauRecomList.get(position);
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
		DataRestauRecom data = mRestauRecomList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_restaurecom_listview, null);
			holder.mRestauRecomImage = (ImageView) convertView
					.findViewById(R.id.dish_recom_image);
			holder.mRestauRecomName = (TextView) convertView
					.findViewById(R.id.dish_recom_name);
			holder.mRestauRecomPrice = (TextView) convertView
					.findViewById(R.id.dish_recom_price);
			holder.mRestauRecomScore = (TextView) convertView
					.findViewById(R.id.dish_recom_score);
			holder.mRestauRecomTaste = (TextView) convertView
					.findViewById(R.id.dish_recom_taste);
			holder.mRestauRecomNutrition = (TextView) convertView
					.findViewById(R.id.dish_recom_nutrition);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mRestauRecomName.setText(data.getDish_name());
		holder.mRestauRecomPrice.setText("单价：" + data.getDish_price());
		holder.mRestauRecomScore.setText("评分：" + data.getDish_score());
		holder.mRestauRecomTaste.setText("口味：" + data.getDish_taste());
		holder.mRestauRecomNutrition.setText("营养：" + data.getDish_nutrition());
		asyncImageLoad(holder.mRestauRecomImage, HttpUtils.IP
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
			// startDefault();
		}
	}

	// private void startDefault() {
	// holder.mRestauRecomImage
	// .setImageResource(R.drawable.image_dish_default);
	// }

	public final class ItemHolder {
		public ImageView mRestauRecomImage;
		public TextView mRestauRecomName;
		public TextView mRestauRecomPrice;
		public TextView mRestauRecomScore;
		public TextView mRestauRecomTaste;
		public TextView mRestauRecomNutrition;
	}
}
