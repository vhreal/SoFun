package cn.xidianyaoyao.app.ui.restaurant;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * 百度地图显示饭馆位置
 * 
 * @author WangTanyun
 * 
 */

public class Restau_Map extends Activity {

	private ImageView mMapBack;
	private TextView mMapTitleName;

	private double mLat;// 位置纬度
	private double mLon;// 位置经度

	private BMapManager mBMapManager = null;
	// private static final String BAIDU_MAP_KEY = "ckGucNo7a2AKMdCFfEaFDYOC";
	// // 百度地图官网申请的密钥
	private static final String BAIDU_MAP_KEY = "pGvTt9pmC9Yn7Rm6kO6GygGD"; // 百度地图官网申请的密钥

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 * 用MapController完成地图控制
	 */
	private MapController mMapController = null;
	/**
	 * MKMapViewListener 用于处理地图事件回调
	 */
	private MKMapViewListener mMapListener = null;

	public void onCreate(Bundle savedInstanceState) {
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */

		super.onCreate(savedInstanceState);
		initEngineManager();
		setContentView(R.layout.restau_map);

		initView();
		setLister();
		initViewMap();
		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	/**
	 * 进行验证key
	 * 
	 * @param pContext
	 */
	private void initEngineManager() {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(getApplicationContext());
			mBMapManager.init(BAIDU_MAP_KEY, new MKGeneralListener() {

				@Override
				public void onGetPermissionState(int iError) {
					if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
						// 授权Key错误：
						Toast.makeText(Restau_Map.this,
								"请输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError,
								Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void onGetNetworkState(int iError) {
					if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
						Toast.makeText(Restau_Map.this, "您的网络出错啦！",
								Toast.LENGTH_LONG).show();
					} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
						Toast.makeText(Restau_Map.this, "输入正确的检索条件！",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	class OverlayTest extends ItemizedOverlay<OverlayItem> {
		public OverlayTest(Drawable marker, MapView mapView) {
			super(marker, mapView);
		}
	}

	private void initView() {
		mMapBack = (ImageView) findViewById(R.id.restauMap_back);
		mMapTitleName = (TextView) findViewById(R.id.restauMap_titleName);
		mMapTitleName.setText(getIntent().getStringExtra("RestauName"));
	}

	private void setLister() {
		mMapBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
	}

	private void initViewMap() {
		/**
		 * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
		 */
		mMapView = (MapView) findViewById(R.id.bmapView);
		/**
		 * 设置显示放大缩小的控制按钮
		 */
		mMapView.setBuiltInZoomControls(true);
		/**
		 * 设置地图显示模式setTraffic交通setSatellite卫星
		 */
		// mMapView.setTraffic(true);
		// mMapView.setSatellite(true);// 设置为卫星模式
		/**
		 * 获取地图控制器
		 */
		mMapController = mMapView.getController();
		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(18);

		/**
		 * 设置指示位置的图标
		 */
		Drawable marker = getResources().getDrawable(
				R.drawable.shape_location_marker);// 指示位置的图片
		OverlayTest ov = new OverlayTest(marker, mMapView);

		mLat = Double.parseDouble(getIntent().getStringExtra("RestauLat"));// 位置纬度赋值
		mLon = Double.parseDouble(getIntent().getStringExtra("RestauLon"));// 位置经度赋值

		GeoPoint geoPoint = new GeoPoint((int) (mLat * 1e6), (int) (mLon * 1e6));
		mMapController.setCenter(geoPoint);
		OverlayItem item = new OverlayItem(geoPoint, null, null);
		item.setMarker(marker);
		ov.addItem(item);
		mMapView.getOverlays().add(ov);

		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
				 * mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(Restau_Map.this, title, Toast.LENGTH_SHORT)
							.show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}

			/**
			 * 在此处理地图载完成事件
			 */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(Restau_Map.this, "地图加载完成", Toast.LENGTH_SHORT)
						.show();

			}
		};
		mMapView.regMapViewListener(mBMapManager, mMapListener);
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapView.onPause();
		if (mBMapManager != null) {
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.onResume();
		if (mBMapManager != null) {
			mBMapManager.start();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.destroy();
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
}
