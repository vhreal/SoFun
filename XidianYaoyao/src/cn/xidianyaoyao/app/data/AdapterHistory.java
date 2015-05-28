package cn.xidianyaoyao.app.data;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.xidianyaoyao.app.R;

public class AdapterHistory extends BaseAdapter {

	private List<DataDishCollected> mHistoryList;
	private Context mContext;
	ItemHolder holder = null;

	public AdapterHistory(Context context, List<DataDishCollected> ld) {
		mContext = context;
		mHistoryList = ld;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mHistoryList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mHistoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DataDishCollected data = mHistoryList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_history_listview, null);
			holder.mHistoryTime = (TextView) convertView
					.findViewById(R.id.history_time);
			holder.mHistoryText = (TextView) convertView
					.findViewById(R.id.history_text);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mHistoryTime.setText(data.getTime());
		holder.mHistoryText.setText(data.getDish_name() + "-"
				+ data.getRestau_name());
		return convertView;
	}

	public final class ItemHolder {
		public TextView mHistoryTime;
		public TextView mHistoryText;
	}

}
