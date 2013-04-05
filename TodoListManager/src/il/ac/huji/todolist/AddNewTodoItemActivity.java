package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);

		
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				EditText editTask = (EditText) findViewById(R.id.edtNewItem);
				String task = editTask.getText().toString();
				
				DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
				int day = datePicker.getDayOfMonth();
				int month = datePicker.getMonth();
				int year = datePicker.getYear();

				Calendar cal = Calendar.getInstance();
			    cal.set(Calendar.YEAR, year);
			    cal.set(Calendar.MONTH, month); 
			    cal.set(Calendar.DAY_OF_MONTH, day);
			    Date date = cal.getTime();
			
			    //set results in intent
			    Intent resultIntent = new Intent();
			    resultIntent.putExtra("title", task);
			    resultIntent.putExtra("dueDate", date);
			    
			    setResult(RESULT_OK, resultIntent);
				finish();
			}
			
		});
		
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				setResult(RESULT_CANCELED);
				finish();
			}
		});
				
	}


}
