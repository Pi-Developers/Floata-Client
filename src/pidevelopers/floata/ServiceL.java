package pidevelopers.floata;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class ServiceL extends Service {
	static SharedPreferences spfs;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onCreate() {
		super.onCreate();
		
	}	

}
