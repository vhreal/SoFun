package cn.xidianyaoyao.app.ui.widget;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

public class Dialog_ClearCache extends Dialog {

	private Context mContext;
	private Builder mDelete;

	public Dialog_ClearCache(Context context) {
		super(context);
		mContext = context;
		mDelete = new AlertDialog.Builder(mContext);
		DialogSettings();
	}

	private void DialogSettings() {
		mDelete.setTitle("确认清除缓存?");
		mDelete.setMessage("保留图片缓存可以节省手机流量");
		mDelete.setIcon(android.R.drawable.ic_dialog_info);
		mDelete.setPositiveButton("清除", mClearCacheListener);
		mDelete.setNegativeButton("取消", mClearCacheListener);
	}

	public void show() {
		mDelete.show();
	}

	OnClickListener mClearCacheListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {

			switch (which) {
			case -1:// 清除
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {// 判断SD卡是否挂载正常
					File xidianyaoyao_cache = new File(
							Environment.getExternalStorageDirectory(),
							"xidianyaoyao_cache");
					DeleteFile(xidianyaoyao_cache);
				} else {
					Toast.makeText(mContext, "SD挂载不正常！", Toast.LENGTH_SHORT)
							.show();
				}
			case -2:// 消失
				dialog.cancel();
				break;
			}
		}
	};

	// 递归删除缓存中的所有东西
	public void DeleteFile(File file) {
		if (!file.exists()) {
			Toast.makeText(mContext, "图片缓存不存在！", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (file.isFile())// 是一个文件
			{
				file.delete();
				Toast.makeText(mContext, "清除图片缓存成功！", Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (file.isDirectory())// 是一个目录
			{
				File childFiles[] = file.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					file.delete();
					Toast.makeText(mContext, "清除图片缓存成功！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				for (File f : childFiles) {
					DeleteFile(f);
				}
				file.delete();
				Toast.makeText(mContext, "清除图片缓存成功！", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
