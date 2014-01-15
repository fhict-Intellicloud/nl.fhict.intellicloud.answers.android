package nl.fhict.intellicloud.answers.backendcommunication;

import android.util.Log;

public class DateHelper {
	public static Long getUnixMillisecondsFromJsonDate(String jsonDate)
	{
		if (jsonDate != null)
		{
			//Log.d("DateHelper", jsonDate);
			String[] intermediateString = jsonDate.split("\\(");
			String[] isolatedDate = intermediateString[1].split("\\+");
			//Log.d("DateHelper", isolatedDate[0]);
			return Long.parseLong(isolatedDate[0]);
		}
		return 0L;
	}

}
