package cn.xidianyaoyao.app.data;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.xidianyaoyao.app.R;

public class AdapterSearchRestau extends BaseAdapter {

	private List<DataSearch> mRestauList;
	private Context mContext;

	public AdapterSearchRestau(Context context,
			List<DataSearch> restauList) {
		mContext = context;
		mRestauList = restauList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRestauList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mRestauList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ItemHolder holder = null;
		DataSearch data = mRestauList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_search_restau_listview, null);
			holder.mNumber = (TextView) convertView
					.findViewById(R.id.restaurant_item_number);
			holder.mRestauAddr = (TextView) convertView
					.findViewById(R.id.restaurant_item_addr);
			holder.mHonorImage = (ImageView) convertView
					.findViewById(R.id.restaurant_item_honor);
			holder.mRestauName = (TextView) convertView
					.findViewById(R.id.restaurant_item_name);
			holder.mDishScore = (TextView) convertView
					.findViewById(R.id.restaurant_item_score);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mNumber.setText(data.getRank_number() + ".");
		holder.mRestauName.setText(data.getRestau_name());
		holder.mDishScore.setText(data.getRestau_score());
		holder.mRestauAddr.setText(data.getRestau_addr());
		if (position == 0) {
			holder.mHonorImage.setBackgroundResource(R.drawable.item_first);
		} else {
			holder.mHonorImage.setBackgroundResource(R.drawable.item_praise);
		}
		return convertView;
	}

	public final class ItemHolder {
		public TextView mNumber;
		public TextView mRestauAddr;
		public ImageView mHonorImage;
		public TextView mRestauName;
		public TextView mDishScore;
	}
}
