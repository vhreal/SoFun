package cn.xidianyaoyao.app.ui.shake;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.DataAllDB;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Shake_group_dialog extends Activity {

	private AutoCompleteTextView mSearchEdit;
	private RelativeLayout mGroupRestau;
	private RelativeLayout mGroupPrice;
	private RelativeLayout mGroupNumber;
	private TextView mPriceShow;
	private TextView mNumberShow;
	private String[] mDBallName;

	private PreferencesService preferencesService;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shake_group_dialog);

		mGroupRestau = (RelativeLayout) findViewById(R.id.shake_group_restau);
		mGroupPrice = (RelativeLayout) findViewById(R.id.shake_group_price);
		mGroupNumber = (RelativeLayout) findViewById(R.id.shake_group_number);
		mPriceShow = (TextView) findViewById(R.id.priceShow);
		mNumberShow = (TextView) findViewById(R.id.numberShow);

		preferencesService = new PreferencesService(this);

		if (preferencesService.shakeGroupPrice_getPreferences().equals("")) {
			preferencesService.save_shakeGroupPriceNum(0);
			preferencesService.save_shakeGroupPrice("不限");// 设定默认值为第一个
		}
		if (preferencesService.shakeNumber_getPreferences().equals("")) {
			preferencesService.save_shakeNumberNum(0);
			preferencesService.save_shakeNumber("2");// 设定默认值为第一个
		}

		mPriceShow.setText(preferencesService.shakeRestau_getPreferences());
		mPriceShow.setText(preferencesService.shakeGroupPrice_getPreferences());
		mNumberShow.setText(preferencesService.shakeNumber_getPreferences());

		mGroupPrice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckPriceDialog();
			}
		});
		mGroupNumber.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckNumberDialog();
			}
		});

		mDBallName = DataAllDB.getRestau();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.widget_search_down, mDBallName);
		mSearchEdit = (AutoCompleteTextView) findViewById(R.id.restauSearch);
		mSearchEdit.setAdapter(adapter);
		mSearchEdit.setText(preferencesService.shakeRestau_getPreferences());
		
		mSearchEdit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable s) {
				if (s.length() != 0) {
					preferencesService.save_shakeRestau(mSearchEdit.getText().toString().trim());
				} 
			}
		});

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	public void CheckPriceDialog() {
		final String[] arrayPrice = new String[] { "不限", "<100", "100-200",
				"200-300", "300-400", "400-500", ">500" };

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("选择消费的上限(元)")
				.setSingleChoiceItems(arrayPrice,
						preferencesService.shakeGroupPriceNum_getPreferences(),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								preferencesService
										.save_shakeGroupPriceNum(which);
								preferencesService
										.save_shakeGroupPrice(arrayPrice[which]);
								mPriceShow.setText(preferencesService
										.shakeGroupPrice_getPreferences());
							}
						})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		alertDialog.show();
	}

	public void CheckNumberDialog() {
		final String[] arrayNumber = new String[] { "2", "3", "4", "5", "6",
				"7", "8", "9", "10" };

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("选择消费的人数")
				.setSingleChoiceItems(arrayNumber,
						preferencesService.shakeNumberNum_getPreferences(),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								preferencesService.save_shakeNumberNum(which);
								preferencesService
										.save_shakeNumber(arrayNumber[which]);
								mNumberShow.setText(preferencesService
										.shakeNumber_getPreferences());
							}
						})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		alertDialog.show();
	}

	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
