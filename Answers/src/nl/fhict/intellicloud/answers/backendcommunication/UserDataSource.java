package nl.fhict.intellicloud.answers.backendcommunication;

import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;

import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.UsersEntry;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

public class UserDataSource {
	public static User GetUser(int id, SQLiteDatabase database)
	{
		User user = null;
		Cursor cursor = database.query(UsersEntry.TABLE_NAME, UsersEntry.ALL_COLUMNS, UsersEntry.COLUMN_ID + " = " + id , null, null, null, null);
		if (cursor.moveToFirst())
		{
			user = new User(cursor.getInt(0), 
					cursor.getString(1), 
					cursor.getString(2), 
					cursor.getString(3), 
					UserType.valueOf(cursor.getString(4)));
		}
		cursor.close();
		
		return user;
	}

}
