package pidevelopers.floata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Broadcast extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
	  
	  

	  
	    Intent i = new Intent();
	    i.setClass(context, Floating.class);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);
    
    
  }
} 
