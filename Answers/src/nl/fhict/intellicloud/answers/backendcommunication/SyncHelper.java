package nl.fhict.intellicloud.answers.backendcommunication;

import android.util.Log;

public class SyncHelper {
	public static Long getUnixMillisecondsFromJsonDate(String jsonDate)
	{
		if (jsonDate != null)
		{
			Log.d("DateHelper", jsonDate);
			String[] intermediateString = jsonDate.split("\\(");
			String[] isolatedDate = intermediateString[1].split("\\+");
			//Log.d("DateHelper", isolatedDate[0]);
			Long dateMilliSeconds = Long.parseLong(isolatedDate[0]);
			Long dateTimeZone = Long.parseLong(isolatedDate[1].substring(0, 2));
			Long milliSecondsDifference = dateTimeZone * 360000;
			return dateMilliSeconds - milliSecondsDifference;
		}
		return 0L;
	}
	public static int getIdFromURI(String uri)
	{
		String[] uriparts = uri.split("/");
		for (int i = 0; i < uriparts.length; i++)
		{
			if(uriparts[i].matches("-?\\d+"))//Regex that checks if the string is a number
			{
				int result = Integer.parseInt(uriparts[i]);
				return result;
			}

		}
		return -1;
	}

}
