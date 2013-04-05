package il.ac.huji.todolist;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class TodoDAL {
	
	private SQLiteDatabase _db;
	
	public TodoDAL(Context context) {
		
		//db
		TodoListManagerDatabaseHelper helper = new TodoListManagerDatabaseHelper(context);
		_db = helper.getWritableDatabase();
		
		//parse
		Parse.initialize(context, context.getResources().getString(R.string.parseApplication), context.getResources().getString(R.string.clientKey));
		PushService.subscribe(context, "", TodoListManagerActivity.class);
		PushService.setDefaultPushCallback(context, TodoListManagerActivity.class);
		ParseUser.enableAutomaticUser();
		
	}
	
	public SQLiteDatabase getDB()
	{
		return _db;
	}
	
	public boolean insert(ITodoItem todoItem) {

		if(todoItem == null || todoItem.getTitle() == null)
		{
			return false;
		} 
		// sqlite
		ContentValues task = new ContentValues();
		task.put("title", todoItem.getTitle());
		task.put("due", todoItem.getDueDate().getTime());
		if(_db.insert("todo", null, task) == -1)
		{
			return false;
		}
	
		// parse
		ParseObject todoObg = new ParseObject("todo");
		todoObg.put("title", todoItem.getTitle());
		todoObg.put("due", todoItem.getDueDate().getTime());
		todoObg.saveInBackground();
		
		return true;
	}
	
	public boolean update(ITodoItem todoItem) { 
		
		if(todoItem == null || todoItem.getTitle() == null)
		{
			return false;
		}
		
		final ITodoItem finalItem = todoItem;
		
		//sqlite
		ContentValues task = new ContentValues();
		task.put("due", todoItem.getDueDate().getTime());
		if(_db.update("todo", task, "title=?", new String[] {todoItem.getTitle()}) == 0)
		{
			return false; //can't update an item that doesn't exist
		}
		
		// parse
		//get parse obj	
		ParseQuery query = new ParseQuery("todo");
		query.whereEqualTo("title", todoItem.getTitle());
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> todoList, ParseException e) {
				if (e == null) {
					for(int i=0; i< todoList.size(); i++)
					{
						ParseObject parseObj = todoList.get(i);
						parseObj.put("due", finalItem.getDueDate().getTime());
						parseObj.saveInBackground();
					}
				} 
			
			}
		});
		
		return true;
	}
	
	public boolean delete(ITodoItem todoItem) {
		
		if(todoItem == null || todoItem.getTitle() == null)
		{
			return false;
		}
		
		// sqlite
		if(_db.delete("todo", "title=? and due=?", new String[] {todoItem.getTitle(), String.valueOf(todoItem.getDueDate().getTime())}) == 0)
		{
			return false;//can't delete an item that doesn't exist
		}
	
		//get parse obj	
		ParseQuery query = new ParseQuery("todo");
		query.whereEqualTo("title", todoItem.getTitle());
		query.whereEqualTo("due", todoItem.getDueDate().getTime());
		query.findInBackground(new FindCallback() {
		    public void done(List<ParseObject> todoList, ParseException e) {
		        if (e == null) {
		            for(int i=0; i< todoList.size(); i++)
		            {
		            	(todoList.get(i)).deleteInBackground();
		            }
		        } 
		    }
		});
		return true;
	}
	
	public List<ITodoItem> all() {
		
		List<ITodoItem> allTodos = new ArrayList<ITodoItem>();
		Cursor cursor =_db.query("todo", new String[] {"title", "due" }, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			do {

							String title = cursor.getString(0);
							Date due = new Date(cursor.getLong(1));
							allTodos.add((ITodoItem)new Task(title, due));

			} while (cursor.moveToNext());
		}
		return allTodos;

	}
}