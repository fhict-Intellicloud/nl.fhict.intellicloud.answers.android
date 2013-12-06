package nl.fhict.intellicloud.answers;

import java.text.SimpleDateFormat;
import nl.fhict.intellicloud.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlidingMenuListAdapter extends ArrayAdapter<String> {
	 private final Context context;
	 private final String[] values;

	  public SlidingMenuListAdapter(Context context, String[] values) {
	    super(context, R.layout.listview_item_sliding_menu, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listview_item_sliding_menu, parent, false);
	    
	    String drawerItem = values[position];
	    
	    ImageView questionStageImage = (ImageView) rowView.findViewById(R.id.ivSlidingItem);
	    questionStageImage.setImageResource(getImageId(position));
	    TextView firstLine = (TextView) rowView.findViewById(R.id.txtFirstLineSlidingItem);
	    firstLine.setText(drawerItem); 

	    return rowView;
	  }
	  
	  private int getImageId(int drawerItemPosition) {
		  switch(drawerItemPosition) {
			case 1:
				return R.drawable.search_icon_white;
			case 2:
				  return R.drawable.rejected_icon_white;
			case 3:
				  return R.drawable.hourglass_icon_white;
			case 4:
				  return R.drawable.glass_icon_white;
		  }
		  return -1;
	  }
}
