package cn.xidianyaoyao.app.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 本地数据库，存储常去饭馆
 * 
 * @author WangTanyun
 * 
 */
public class MoregoSQLiteHelper extends SQLiteOpenHelper {
	// 调用父类构造器
	private String user_name = null;
	private static final int VERSION = 1;

	public MoregoSQLiteHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version, String user_name) {
		super(context, name, factory, version);
		this.user_name = user_name;
	}

	public MoregoSQLiteHelper(Context context, String user_name) {
		this(context, "sql" + user_name, null, VERSION, "sqllcus" + user_name);// 第二参数是数据库名
	}

	/**
	 * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行. 重写onCreate方法，调用execSQL方法创建表
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ user_name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,cus_id VARCHAR,res_id VARCHAR,res_name VARCHAR,res_score VARCHAR,res_addr VARCHAR,res_call VARCHAR,res_descr VARCHAR,res_lat VARCHAR,res_lon VARCHAR,count INTEGER)");
	}

	// 插入数据
	public void InsertData(SQLiteDatabase db, String name, String res_id,
			String res_name, String res_score, String res_addr,
			String res_call, String res_descr, String res_lat, String res_lon) {
		Cursor c = db.rawQuery("SELECT * FROM " + user_name, null);
		db.execSQL("INSERT INTO " + user_name
				+ " VALUES (NULL,?,?,?,?,?,?,?,?,?)", new Object[] {
				"sqllcus" + name, res_id, res_name, res_score, res_addr,
				res_call, res_descr, res_lat, res_lon });
	}

	// 当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
