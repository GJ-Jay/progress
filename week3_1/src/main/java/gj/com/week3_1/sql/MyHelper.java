package gj.com.week3_1.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*private String news_id;
        private String news_title;
        private String news_summary;
        private String pic_url;*/
        //创建数据库
        db.execSQL("create table jsontable(id integer primary key autoincrement," +
                "news_title varchar(50)," +
                "news_summary varchar(50)," +
                "pic_url varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
