package cn.xidianyaoyao.app.ui.shake;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Shake_select_dialog extends Activity {

	private RelativeLayout mSelectItem;
	private RelativeLayout mSelectPrice;
	private RelativeLayout mSelectScore;
	private RelativeLayout mSelectTaste;
	private RelativeLayout mSelectNutrition;
	private TextView mItemShow;
	private TextView mPriceShow;
	private TextView mScoreShow;
	private TextView mTasteShow;
	private TextView mNutritionShow;

	private PreferencesService preferencesService;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shake_select_dialog);

		mSelectItem = (RelativeLayout) findViewById(R.id.shake_select_item);
		mSelectPrice = (RelativeLayout) findViewById(R.id.shake_select_price);
		mSelectScore = (RelativeLayout) findViewById(R.id.shake_select_score);
		mSelectTaste = (RelativeLayout) findViewById(R.id.shake_select_taste);
		mSelectNutrition = (RelativeLayout) findViewById(R.id.shake_select_nutrition);
		mItemShow = (TextView) findViewById(R.id.itemShow);
		mPriceShow = (TextView) findViewById(R.id.priceShow);
		mScoreShow = (TextView) findViewById(R.id.scoreShow);
		mTasteShow = (TextView) findViewById(R.id.tasteShow);
		mNutritionShow = (TextView) findViewById(R.id.nutritionShow);

		preferencesService = new PreferencesService(this);
		if (preferencesService.shakeItem_getPreferences().equals("")) {
			preferencesService.save_shakeItem("1");// 默认值为1
		}
		if (preferencesService.shakePrice_getPreferences().equals("")) {
			preferencesService.save_shakePriceNum(0);
			preferencesService.save_shakePrice("不限");// 设定默认值为第一个
		}
		if (preferencesService.shakeScore_getPreferences().equals("")) {
			preferencesService.save_shakeScoreNum(0);
			preferencesService.save_shakeScore("不限");// 设定默认值为第一个
		}
		if (preferencesService.shakeTaste_getPreferences().equals("")) {
			preferencesService.save_shakeTasteNum(0);
			preferencesService.save_shakeTaste("不限");// 设定默认值为第一个
		}
		if (preferencesService.shakeNutrition_getPreferences().equals("")) {
			preferencesService.save_shakeNutritionNum(0);
			preferencesService.save_shakeNutrition("不限");// 设定默认值为第一个
		}

		mItemShow.setText(preferencesService.shakeItem_getPreferences());
		mPriceShow.setText(preferencesService.shakePrice_getPreferences());
		mScoreShow.setText(preferencesService.shakeScore_getPreferences());
		mTasteShow.setText(preferencesService.shakeTaste_getPreferences());
		mNutritionShow.setText(preferencesService
				.shakeNutrition_getPreferences());

		mSelectItem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckItemDialog();
			}
		});
		mSelectPrice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckPriceDialog();
			}
		});
		mSelectScore.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckScoreDialog();
			}
		});
		mSelectTaste.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckTasteDialog();
			}
		});

		mSelectNutrition.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckNutritionDialog();
			}
		});

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	public void CheckItemDialog() {
		final String[] arrayNum = new String[] { "1", "2", "3", "4", "5" };
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("选择一次摇出几个菜")
				.setSingleChoiceItems(
						arrayNum,
						Integer.valueOf(preferencesService
								.shakeItem_getPreferences()) - 1,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								mItemShow.setText(arrayNum[which]);
								preferencesService
										.save_shakeItem(arrayNum[which]);
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		alertDialog.show();
	}

	public void CheckPriceDialog() {
		final String[] arrayPrice = new String[] { "不限", "<10", "10-20",
				"20-30", "30-40", "40-50", ">50" };

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("选择饭菜的单价(元)")
				.setSingleChoiceItems(arrayPrice,
						preferencesService.shakePriceNum_getPreferences(),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								preferencesService.save_shakePriceNum(which);
								preferencesService
										.save_shakePrice(arrayPrice[which]);
								mPriceShow.setText(preferencesService
										.shakePrice_getPreferences());
							}
						})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		alertDialog.show();
	}

	public void CheckScoreDialog() {
		final String[] arrayScore = new String[] { "不限", "1", "2", "3", "4",
				"5" };

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("选择饭菜的评分(不能低于)")
				.setSingleChoiceItems(arrayScore,
						preferencesService.shakeScoreNum_getPreferences(),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								preferencesService.save_shakeScoreNum(which);
								preferencesService
										.save_shakeScore(arrayScore[which]);
								mScoreShow.setText(preferencesService
										.shakeScore_getPreferences());
							}
						})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		alertDialog.show();
	}

	public void CheckTasteDialog() {
		final String[] arrayTaste = new String[] { "不限", "酸", "甜", "苦", "辣",
				"咸", "涩", "腻", "淡", "麻", "腥" };

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("选择饭菜的口味")
				.setSingleChoiceItems(arrayTaste,
						preferencesService.shakeTasteNum_getPreferences(),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								preferencesService.save_shakeTasteNum(which);
								preferencesService
										.save_shakeTaste(arrayTaste[which]);
								mTasteShow.setText(preferencesService
										.shakeTaste_getPreferences());
							}
						})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		alertDialog.show();
	}

	public void CheckNutritionDialog() {
		final String[] arrayNutrition = new String[] { "不限", "糖类", "脂类", "蛋白质",
				"无机盐", "维生素", "水" };

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("选择饭菜的营养")
				.setSingleChoiceItems(arrayNutrition,
						preferencesService.shakeNutritionNum_getPreferences(),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								preferencesService
										.save_shakeNutritionNum(which);
								preferencesService
										.save_shakeNutrition(arrayNutrition[which]);
								mNutritionShow.setText(preferencesService
										.shakeNutrition_getPreferences());
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
