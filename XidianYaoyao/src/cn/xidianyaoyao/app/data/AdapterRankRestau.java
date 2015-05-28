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

public class AdapterRankRestau extends BaseAdapter {

	private List<DataRankRestau> mRankRestauList;
	private Context mContext;

	public AdapterRankRestau(Context context,
			List<DataRankRestau> rankRestauList) {
		mContext = context;
		mRankRestauList = rankRestauList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRankRestauList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mRankRestauList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ItemHolder holder = null;
		DataRankRestau data = mRankRestauList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_rankrestau_listview, null);
			holder.mNumber = (TextView) convertView
					.findViewById(R.id.rank_restau_number);
			holder.mRestauAddr = (TextView) convertView
					.findViewById(R.id.rank_restau_item_addr);
			holder.mHonorImage = (ImageView) convertView
					.findViewById(R.id.rank_restau_item_honor);
			holder.mRestauName = (TextView) convertView
					.findViewById(R.id.rank_restau_item_name);
			holder.mRestauScore = (TextView) convertView
					.findViewById(R.id.rank_restau_item_score);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mNumber.setText(data.getRank_number() + ".");
		holder.mRestauName.setText(data.getRestau_name());
		holder.mRestauScore.setText(data.getRestau_score());
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
		public ImageView mHonorImage;
		public TextView mRestauName;
		public TextView mRestauScore;
		public TextView mRestauAddr;
	}
}
