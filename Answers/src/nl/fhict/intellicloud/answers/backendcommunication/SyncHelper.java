package nl.fhict.intellicloud.answers.backendcommunication;

import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;

import org.json.JSONObject;

import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SyncHelper {
	public static Long getUnixMillisecondsFromJsonDate(String jsonDate)
	{
		if (jsonDate != null && jsonDate.length() > 0 && !jsonDate.equals("null"))
		{
			String[] intermediateString = jsonDate.split("\\(");
			String[] isolatedDate = intermediateString[1].split("\\+");
			Long dateMilliSeconds = Long.parseLong(isolatedDate[0]);
			Long dateTimeZone = Long.parseLong(isolatedDate[1].substring(0, 2));
			Long milliSecondsDifference = dateTimeZone * 3600000;
			return dateMilliSeconds + milliSecondsDifference;
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
	public static Boolean isServerObjectNewer(JSONObject serverObject, Cursor localObjectCursor)
	{
		int timeStampColumn = localObjectCursor.getColumnIndex(QuestionsEntry.COLUMN_TIMESTAMP);
		String serverTimeStampString = serverObject.optString("LastChangedTime", null);
		
		if (serverTimeStampString != null)
		{
			long localTimeStamp = localObjectCursor.getLong(timeStampColumn);
			long serverTimeStamp = getUnixMillisecondsFromJsonDate(serverObject.optString("LastChangedTime"));
			if (serverTimeStamp > localTimeStamp)
			{
				return true;
			}
			
		}
		return false;
	}

}
