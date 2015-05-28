package cn.xidianyaoyao.app.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class Main_about extends Activity {

	private ImageView mAboutBack;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_about);

		mAboutBack = (ImageView) findViewById(R.id.about_back);

		mAboutBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		XidianYaoyaoApplication.getInstance().addActivity(this);
	}
}