package cn.xidianyaoyao.app.data;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.xidianyaoyao.app.R;

public class AdapterMorego extends BaseAdapter {

	private List<DataRankRestau> mMoregoList;
	private Context mContext;
	ItemHolder holder = null;

	public AdapterMorego(Context context, List<DataRankRestau> ld) {
		mContext = context;
		mMoregoList = ld;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMoregoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mMoregoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DataRankRestau data = mMoregoList.get(position);
		if (convertView == null) {
			holder = new ItemHolder();
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.item_gorestau_listview, null);
			holder.mMoregoName = (TextView) convertView
					.findViewById(R.id.go_restau_item_name);
			holder.mMoregoAddr = (TextView) convertView
					.findViewById(R.id.go_restau_item_addr);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mMoregoName.setText(data.getRestau_name());
		holder.mMoregoAddr.setText(data.getRestau_addr());
		return convertView;
	}

	public final class ItemHolder {
		public TextView mMoregoName;
		public TextView mMoregoAddr;
	}

}
