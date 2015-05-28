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

public class AdapterFriend extends BaseAdapter {

	private List<DataFriend> mFriendList;
	private Context mContext;
	private ItemHolder holder = null;

	public AdapterFriend(Context context, List<DataFriend> FriendList) {
		mContext = context;
		mFriendList = FriendList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFriendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DataFriend data = mFriendList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_person_friend_listview, null);
			holder.mFriendImage = (ImageView) convertView
					.findViewById(R.id.friend_photo);
			holder.mFriendName = (TextView) convertView
					.findViewById(R.id.friend_name);
			holder.mFriendGender = (TextView) convertView
					.findViewById(R.id.friend_gender);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		asyncImageLoad(holder.mFriendImage, HttpUtils.IP + "resources/upload/"
				+ data.getFriend_image());
		holder.mFriendName.setText(data.getFriend_name());
		if (data.getFriend_gender().equals("0")) {
			holder.mFriendGender.setText("女");
		} else if (data.getFriend_gender().equals("1")) {
			holder.mFriendGender.setText("男");
		}
		return convertView;
	}

	public final class ItemHolder {
		public ImageView mFriendImage;
		public TextView mFriendName;
		public TextView mFriendGender;
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

				// BitmapFactory.Options opts = new BitmapFactory.Options();
				// opts.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(imageFile, opts);
				// opts.inSampleSize = BitmapTools.computeSampleSize(opts, -1,
				// 128 * 128);
				// opts.inJustDecodeBounds = false;
				// try {
				// Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
				// imageView.setImageBitmap(bmp);
				// } catch (OutOfMemoryError err) {
				// }

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
			// startDefault();
		}
	}

	// private void startDefault() {
	// holder.mFriendImage.setImageResource(R.drawable.image_head_default);
	// }
}
