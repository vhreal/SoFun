package cn.xidianyaoyao.app.ui.restaurant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.person.Person_login;
import cn.xidianyaoyao.app.ui.widget.BitmapTools;
import cn.xidianyaoyao.app.ui.widget.ImageTools;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;
import cn.xidianyaoyao.app.utils.HttpUtils;

public class Fragment_Restau_Photo extends Fragment {

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

	private Bundle bundle;
	private LoadingProgressDialog mProgress;
	private ProgressDialog mProgress2;

	private int mRequestTimes = 0;// 请求次数从1开始计数
	private String sTotalNum;
	private String sPhotoPath;
	private String sPhotoSendUserName;
	private String sPhotoSendTime;

	private TextView mCurrentNum;
	private TextView mTotalNum;
	private TextView mSendPersonName;
	private TextView mSendTime;
	private ImageView mPhotoImage;
	private LinearLayout mCamera;
	private LinearLayout mPhotoPre;
	private LinearLayout mPhotoLas;
	private LinearLayout mEmptyPhoto;
	private ScrollView mScrollView;

	private View mPhotoView;
	private PreferencesService preferencesService;
	private Map<String, String> params;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;
		mPhotoView = (View) inflater.inflate(R.layout.fragment_restau_photo,
				container, false);

		initView();
		setLister();

		bundle = this.getArguments();
		new AsyncRestauPhotoTask().execute(bundle.getString("RestauId"),
				String.valueOf(mRequestTimes));
		return mPhotoView;
	}

	private void initView() {
		mCurrentNum = (TextView) mPhotoView
				.findViewById(R.id.restau_photo_currentNum);
		mTotalNum = (TextView) mPhotoView
				.findViewById(R.id.restau_photo_totalNum);
		mPhotoImage = (ImageView) mPhotoView
				.findViewById(R.id.restau_photo_image);
		mSendPersonName = (TextView) mPhotoView
				.findViewById(R.id.restau_photo_sendperson);
		mSendTime = (TextView) mPhotoView
				.findViewById(R.id.restau_photo_sendtime);
		mCamera = (LinearLayout) mPhotoView.findViewById(R.id.restau_photo_btn);
		mPhotoPre = (LinearLayout) mPhotoView
				.findViewById(R.id.restau_photo_pre);
		mPhotoLas = (LinearLayout) mPhotoView
				.findViewById(R.id.restau_photo_las);
		mScrollView = (ScrollView) mPhotoView.findViewById(R.id.scrollview);
		mEmptyPhoto = (LinearLayout) mPhotoView.findViewById(R.id.photo_empty);
	}

	private void setLister() {
		mPhotoPre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mRequestTimes--;
				if (mRequestTimes < 0) {
					mRequestTimes = 0;
				} else {
					new AsyncRestauPhotoTask().execute(
							bundle.getString("RestauId"),
							String.valueOf(mRequestTimes));
				}
			}
		});

		mPhotoLas.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mRequestTimes++;
				if ((mRequestTimes + 1) > Integer.valueOf(sTotalNum)) {// 超过总数，就设为总数
					mRequestTimes = Integer.valueOf(sTotalNum) - 1;
				} else {
					new AsyncRestauPhotoTask().execute(
							bundle.getString("RestauId"),
							String.valueOf(mRequestTimes));
				}
			}
		});
		mCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				if (params.get("cusName").equals("")) {
					Toast.makeText(getActivity(), "亲，你还没登录呢!",
							Toast.LENGTH_SHORT).show();
					startActivityForResult(new Intent(getActivity(),
							Person_login.class), 1);
					getActivity().overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
				} else if (!params.get("cusName").equals("")) {
					// 创建拍照上传dialog,解决多张照片上传
					CheckPhotoDialog();
				}
			}
		});

		mEmptyPhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				if (params.get("cusName").equals("")) {
					Toast.makeText(getActivity(), "亲，你还没登录呢!",
							Toast.LENGTH_SHORT).show();
					startActivityForResult(new Intent(getActivity(),
							Person_login.class), 1);
					getActivity().overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
				} else if (!params.get("cusName").equals("")) {
					// 创建拍照上传dialog,解决多张照片上传
					CheckPhotoDialog();
				}
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			CheckPhotoDialog();
			return;
		} else if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				break;

			case CHOOSE_PICTURE:
				break;

			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				} else {
					uri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "restau.png"));
				}
				cropImage(uri, 400, 400, CROP_PICTURE);
				break;

			case CROP_PICTURE:
				Bitmap photo = null;
				Uri photoUri = data.getData();
				if (photoUri != null) {
					photo = BitmapFactory.decodeFile(photoUri.getPath());
				}
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photo = (Bitmap) extra.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					}
				}
				ImageTools.savePhotoToSDCard(photo, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						"myrestau");

				File temp = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/myrestau.png");
				if (!temp.exists()) {
					Toast.makeText(getActivity(), "图片保存不成功", Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					preferencesService = new PreferencesService(getActivity());
					params = preferencesService.cusInfo_getPreferences();
					new AsyncLoadTask().execute(params.get("cusName"),
							bundle.getString("RestauId"));
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);

		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	public void CheckPhotoDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择设置头像方式");
		builder.setItems(new String[] { "手机拍照", "手机相册" },
				new DialogInterface.OnClickListener() {
					// 类型码
					int REQUEST_CODE;

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							Uri imageUri = null;
							Intent openCameraIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							REQUEST_CODE = CROP;

							imageUri = Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									"restau.png"));
							// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									imageUri);
							startActivityForResult(openCameraIntent,
									REQUEST_CODE);
							break;

						case CHOOSE_PICTURE:
							Intent openAlbumIntent = new Intent(
									Intent.ACTION_GET_CONTENT);
							REQUEST_CODE = CROP;
							openAlbumIntent
									.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							startActivityForResult(openAlbumIntent,
									REQUEST_CODE);
							break;

						default:
							break;
						}
					}
				});
		builder.create().show();
	}

	public class AsyncLoadTask extends AsyncTask<String, Integer, String> {
		// 上传照片
		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mTUploadFile.TUploadFromLocal(
						params[0], params[1]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("commonACK");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopLoadProgressDialog();
			if (result.equals("000001")) {
				Toast.makeText(getActivity(), "上传照片成功", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startUpProgressDialog();
		}
	}

	private void startUpProgressDialog() {
		if (mProgress2 == null) {
			mProgress2 = new ProgressDialog(getActivity(),
					R.style.myProgressDialog);
			mProgress2.setMessage("照片上传中...");
		}
		mProgress2.show();
	}

	/*************************************************/
	public class AsyncRestauPhotoTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.photoShow(
						params[0], params[1]);
				JSONObject resultCode = new JSONObject(result);
				sTotalNum = resultCode.getString("totalProperty");
				int totalItems = Integer.parseInt(resultCode
						.getString("totalProperty"));// 总的照片数
				if (totalItems == 0) {
					code = "0000";// 本来就没有照片
				} else {
					String photoString = resultCode.getString("restImages");
					if (!photoString.equals("null")) {// 有照片
						code = "1111";
						JSONObject photoInfos = new JSONObject(photoString);
						sPhotoPath = HttpUtils.IP + "resources/upload/"
								+ photoInfos.getString("imageUrl");
						sPhotoSendUserName = photoInfos
								.getString("uploadPerson");
						sPhotoSendTime = " 上传于"
								+ photoInfos.getString("uploadTime");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopSeeProgressDialog();
			if (result.equals("1111")) {
				mEmptyPhoto.setVisibility(View.GONE);
				mScrollView.setVisibility(View.VISIBLE);
				mCurrentNum.setText(String.valueOf(mRequestTimes + 1));
				mTotalNum.setText("/" + sTotalNum);
				mSendPersonName.setText(sPhotoSendUserName);
				mSendTime.setText(sPhotoSendTime);
				asyncImageLoad(mPhotoImage, sPhotoPath);
			} else if (result.equals("0000")) {
				mScrollView.setVisibility(View.GONE);
				mEmptyPhoto.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "网络错误，请重试！", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startSeeProgressDialog();
		}
	}

	private void asyncImageLoad(ImageView imageView, String path) {
		AsyncTuDownTask mTuDownTask = new AsyncTuDownTask(imageView);
		mTuDownTask.execute(path);
	}

	private class AsyncTuDownTask extends AsyncTask<String, Integer, byte[]> {
		private ImageView imageView;

		public AsyncTuDownTask(ImageView imageView) {
			this.imageView = imageView;
		}

		protected byte[] doInBackground(String... params) {// 子线程中执行的
			try {
				return XidianYaoyaoApplication.mHttpUtils
						.getNoCacheImage(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(byte[] result) {// 运行在主线程
			if (result != null && imageView != null) {

				// BitmapFactory.Options opts = new BitmapFactory.Options();
				// opts.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(imageFile, opts);
				// opts.inSampleSize = BitmapTools.computeSampleSize(opts, -1,
				// 128 * 128);
				// opts.inJustDecodeBounds = false;
				// try {
				// Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
				// imageView.setImageBitmap(bmp);
				// } catch (OutOfMemoryError err) {
				// }

				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeByteArray(result, 0,
							result.length);
					BitmapTools.toRoundCorner(bitmap, 4);// 圆角头像数值越大圆角越大
					imageView.setImageBitmap(bitmap);// 显示图片
				} catch (OutOfMemoryError e) {// 图片太大了就显示系统默认的头像
				}
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	private void startSeeProgressDialog() {
		if (mProgress == null) {
			mProgress = new LoadingProgressDialog(getActivity(),
					R.style.myProgressDialog);
		}
		mProgress.show();
	}

	private void stopSeeProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}

	private void stopLoadProgressDialog() {
		if (mProgress2 != null) {
			mProgress2.dismiss();
			mProgress2 = null;
		}
	}
}
