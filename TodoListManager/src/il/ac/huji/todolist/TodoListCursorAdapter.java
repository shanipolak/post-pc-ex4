package il.ac.huji.todolist;

import java.sql.Date;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class TodoListCursorAdapter extends SimpleCursorAdapter{
	
	Context _context;
	//Cursor _cursor;
	
	public TodoListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
		 _context = context;
		//_cursor = c;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row, null);
		
		Cursor c = (Cursor)getItem(position);
		c.moveToPosition(position);

		TextView taskName = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView taskDueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		
		Date date;
		if(c.isNull(2))
		{
			date = null;
		}
		else
		{
			date = new Date(c.getLong(2));
		}
		Task task = new Task(c.getString(1), date);
		
		taskName.setText(task.getTitle());
		taskDueDate.setText(task.getStrDueDate());
		
		do
		{
			if(task.getDueDate() == null)
			{
				break;
			}

			// cur date
			Calendar calendar = Calendar.getInstance();
			int curDay = calendar.get(Calendar.DAY_OF_MONTH);
			int curMonth = calendar.get(Calendar.MONTH) + 1;// January is 0
			int curYear = calendar.get(Calendar.YEAR);


			if(curYear > task.getYear() || (curYear == task.getYear() && curMonth > task.getMonth()) ||
					(curYear == task.getYear() && curMonth == task.getMonth() && curDay >= task.getday()))
			{
				taskName.setTextColor(Color.RED);
				taskDueDate.setTextColor(Color.RED);
			}
			else{
				taskName.setTextColor(Color.BLACK);
				taskDueDate.setTextColor(Color.BLACK);
			}
		}
		while(false);
		
		return view;
	}
}
