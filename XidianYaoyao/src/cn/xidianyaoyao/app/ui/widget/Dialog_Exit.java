package cn.xidianyaoyao.app.ui.widget;

import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialog_Exit extends Dialog{
	
	private Context mContext;
	private Builder mExit;
	
	public Dialog_Exit(Context context) {
		super(context);
		mContext = context;
		mExit = new AlertDialog.Builder(mContext);
		DialogSettings();
	}
	
	private void DialogSettings() {
		mExit.setMessage("确认退出SoFun吗?");
		mExit.setPositiveButton("退出", mExitListener);
		mExit.setNegativeButton("取消", mExitListener);
	}
	
	public void show(){
		mExit.show();
	}
	
	OnClickListener mExitListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch(which){
			case -1://确定
				XidianYaoyaoApplication.getInstance().exit();
				break;
			case -2://取消
				dialog.cancel();
				break;
			}
		}
	};
}
