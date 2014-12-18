package pidevelopers.floata;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

public class Close extends ActionBarActivity {
WindowManager windowManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		NotificationManager	notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		if(ChatHead.twitter != null ){
			
			
			try{
				stopService(new Intent (Close.this , ChatHead.class));
				notificationManager.cancel(0);
				finish();
			}catch(Exception e){
				
			}
			
		}
		
	
		
	}

}
