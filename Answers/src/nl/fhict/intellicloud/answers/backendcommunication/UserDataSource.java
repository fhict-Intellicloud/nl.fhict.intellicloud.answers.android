package nl.fhict.intellicloud.answers.backendcommunication;

import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;

import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.AnswersEntry;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.UsersEntry;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;

public class UserDataSource implements IUserService {
	private SQLiteDatabase database;
	private LocalStorageSQLiteHelper dbHelper;
	
	
	
	public UserDataSource(Context context) {
		dbHelper = new LocalStorageSQLiteHelper(context);
		//
	}
	
	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	private void close() {
		dbHelper.close();
	}
	public User GetUser(int id)
	{
		open();
		User user = GetUser(id, database);
		close();
		return user;
	}
	public static User GetUser(int id, SQLiteDatabase database)
	{
		User user = null;
		Cursor cursor = database.query(UsersEntry.TABLE_NAME, UsersEntry.ALL_COLUMNS, UsersEntry.COLUMN_BACKEND_ID + " == " + id , null, null, null, null);
		if (cursor.moveToFirst())
		{
			user = new User(cursor.getInt(1), 
					cursor.getString(2), 
					cursor.getString(3), 
					cursor.getString(4), 
					UserType.valueOf(cursor.getString(5)));
		}
		cursor.close();
		
		return user;
	}

}
