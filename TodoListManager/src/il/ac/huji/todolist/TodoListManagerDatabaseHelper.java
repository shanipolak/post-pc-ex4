package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoListManagerDatabaseHelper extends SQLiteOpenHelper {

	public TodoListManagerDatabaseHelper(Context context) {
		super(context, "todo_db", null, 1);
	}


	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table todo ( _id integer primary key autoincrement,"
				+ " title text, due INTEGER );");
	}


	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Nothing to do.
	}

}
