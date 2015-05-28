package cn.xidianyaoyao.app.ui.widget;

import cn.xidianyaoyao.app.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 页面加载动画类
 * 
 * @author WangTanyun
 *
 */

public class LoadingProgressDialog extends Dialog {
	private Context context = null;
	private static LoadingProgressDialog customProgressDialog = null;

	public LoadingProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LoadingProgressDialog createDialog(Context context) {
		customProgressDialog = new LoadingProgressDialog(context,
				R.style.myProgressDialog);
		customProgressDialog.setContentView(R.layout.widget_loadingprogress);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}
		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.loading_iv);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	/**
	 * 
	 * setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LoadingProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.progress_tv);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}
}