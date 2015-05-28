package cn.xidianyaoyao.app.utils;

import java.io.File;

import android.os.Environment;

public class TUploadFile {

	private static TUploadFile mTUploadFile = null;

	private TUploadFile() {
	}

	public synchronized static TUploadFile getInstance() {
		if (mTUploadFile == null)
			mTUploadFile = new TUploadFile();
		return mTUploadFile;
	}

	public String TUploadFromLocal(String cusId, String resId) {
		String result = "";
		result = UploadUtils.uploadFile(new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/myrestau.png"),
				"http://nutshellsofun.duapp.com/"
						+ "upload_uploadReataurantImage?cusId=" + cusId
						+ "&resId=" + resId);
		return result;
	}

	public String TUploadHeadImage(String cusId) {
		String result = "";
		result = UploadUtils.uploadFile(new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/myphoto.png"),
				"http://nutshellsofun.duapp.com/"
						+ "upload_uploadHeadImage?cusId=" + cusId);
		return result;
	}
}
