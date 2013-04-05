package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

public class Task implements ITodoItem{

	private String _name;
	private int _day;
	private int _month;
	private int _year;
	private Date _date;
	
	public Task(String name, Date date)
	{
		_name = name;
		_date = date;
		
		//get day, month, year from date
		if(date != null)
		{

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			_day = cal.get(Calendar.DAY_OF_MONTH);
			_month = cal.get(Calendar.MONTH) + 1;
			_year = cal.get(Calendar.YEAR);
		}
		else
		{
			_day = -1;
			_month =-1;
			_year = -1;
		}
	}
	
	public int getday()
	{
		return _day;
	}
	
	public int getMonth()
	{
		return _month;
	}
	
	public int getYear()
	{
		return _year;
	}
	
	public String getStrDueDate()
	{
		if(_date == null)
		{
			return "No due date";
		}
		return String.format("%02d", _day) + "/" + String.format("%02d", _month) +"/" + String.valueOf(_year);
	}

	public String getTitle() {
		
		return _name;
	}

	public Date getDueDate() {
		return _date;
	}
	
}
