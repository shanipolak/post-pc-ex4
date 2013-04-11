package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class TodoListManagerActivity extends Activity {

	private SQLiteDatabase db;
	static final String CALL = "Call ";
	private TodoDAL  _tododal;
	private TodoListCursorAdapter _adapter;
	Cursor cursor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		_tododal = new TodoDAL(this);
		
		ListView listtasks =  (ListView)findViewById(R.id.lstTodoItems);
		registerForContextMenu(listtasks);

		//set adapter to sqlite db
		db = _tododal.getDB();
		
		cursor = db.query("todo", new String[] { "_id", "title", "due" }, null, null, null, null, null);
		String[] from = { "title", "due" };
		int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };

		_adapter = new TodoListCursorAdapter(this, R.layout.row, cursor, from, to);
	
		listtasks.setAdapter(_adapter);
	}
	

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menuItemAdd:

			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, 1337);		
		}
		return true;
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.contextmenu, menu);
		
		//set context menu title
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		cursor =_adapter.getCursor();
		cursor.moveToPosition(info.position);
		String selectedItem = cursor.getString(1);

		menu.setHeaderTitle(selectedItem);
		
		//call option
		MenuItem item = (MenuItem) menu.findItem(R.id.menuItemCall);
		if(selectedItem.startsWith(CALL))
		{
			item.setTitle(selectedItem);
		}
		else
		{
			menu.removeItem(R.id.menuItemCall);
		}

	}


	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int itemPosition = info.position;
		cursor.moveToPosition(itemPosition);

	
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				
				_tododal.delete((ITodoItem)new Task(cursor.getString(1), new Date(cursor.getLong(2))));
				cursor.requery();
				
			break;
		
			case R.id.menuItemCall:
				String phoneNum = parseCallTask(cursor.getString(1));
				Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
				startActivity(dial); 
		}
		return true;
	}

	//returns the phone number from a call task
	private String parseCallTask(String task) {
		
		return task.substring(CALL.length());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1337 && resultCode == RESULT_OK) {
			String taskName = data.getStringExtra("title");
			Date dueDate = (Date)data.getSerializableExtra("dueDate");
			_tododal.insert((ITodoItem)new Task(taskName, dueDate));
			cursor.requery();
			
		}
	}

}