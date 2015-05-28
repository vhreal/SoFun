package cn.xidianyaoyao.app.logic;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import cn.xidianyaoyao.app.utils.HttpUtils;
import cn.xidianyaoyao.app.utils.TUploadFile;

public class XidianYaoyaoApplication extends Application {
	private static XidianYaoyaoApplication mInstance = null;
	private List<Activity> activityList = new LinkedList<Activity>();
	public static HttpUtils mHttpUtils;
	public static TUploadFile mTUploadFile;
	public static float phoneScale;

	// 单例模式中获取唯一的XidianYaoyaoApplication实例
	public static XidianYaoyaoApplication getInstance() {
		if (null == mInstance)
			mInstance = new XidianYaoyaoApplication();
		return mInstance;
	}

	public void onCreate() {
		super.onCreate();
		phoneScale = getResources().getDisplayMetrics().density;
		mHttpUtils = HttpUtils.getInstance();
		mTUploadFile = TUploadFile.getInstance();
	}

	// 将activity添加到容器里面
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
