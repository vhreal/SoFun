package cn.xidianyaoyao.app.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 本地数据库，存储浏览历史
 * 
 * @author TangWenda
 * 
 */
public class HistorySQLiteHelper extends SQLiteOpenHelper {
	// 调用父类构造器
	private String user_name = null;
	private static final int VERSION = 1;

	public HistorySQLiteHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version, String user_name) {
		super(context, name, factory, version);
		this.user_name = user_name;
	}

	public HistorySQLiteHelper(Context context, String user_name) {
		this(context, "sql" + user_name, null, VERSION, "sqlcus" + user_name);// 第二参数是数据库名
	}

	/**
	 * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行. 重写onCreate方法，调用execSQL方法创建表
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ user_name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,cus_id VARCHAR,time VARCHAR,dish_id VARCHAR,dish_image VARCHAR,dish_name VARCHAR,dish_score VARCHAR,dish_price VARCHAR,dish_taste VARCHAR,dish_nutrition VARCHAR,res_id VARCHAR,res_name VARCHAR,res_score VARCHAR,res_addr VARCHAR,res_call VARCHAR,res_descr VARCHAR,res_lat VARCHAR,res_lon VARCHAR)");
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	// 插入数据，最多只能存10条，大于10条时，依次删除最前的条
	public void InsertData(SQLiteDatabase db, String name, String dish_id,
			String dish_image, String dish_name, String dish_score,
			String dish_price, String dish_taste, String dish_nutrition,
			String res_id, String res_name, String res_score, String res_addr,
			String res_call, String res_descr, String res_lat, String res_lon) {
		Cursor c = db.rawQuery("SELECT * FROM " + user_name, null);
		if (c.getCount() < 10) {
			db.execSQL("INSERT INTO " + user_name
					+ " VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { "sqlcus" + name, getDateTime(), dish_id,
							dish_image, dish_name, dish_score, dish_price,
							dish_taste, dish_nutrition, res_id, res_name,
							res_score, res_addr, res_call, res_descr, res_lat,
							res_lon });
		} else {
			db.execSQL("delete from " + user_name
					+ " where _id=(select _id from " + user_name
					+ " ORDER BY time limit 1)");
			db.execSQL("INSERT INTO " + user_name
					+ " VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { "sqlcus" + name, getDateTime(), dish_id,
							dish_image, dish_name, dish_score, dish_price,
							dish_taste, dish_nutrition, res_id, res_name,
							res_score, res_addr, res_call, res_descr, res_lat,
							res_lon });
		}
	}

	// 清空数据
	public void DeleteData(SQLiteDatabase db) {
		db.execSQL("delete from " + user_name);
	}

	// 当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
