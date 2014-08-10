package pidevelopers.floata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Close extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	if(Chathead.twitter != null ){
		
		
		try{
			stopService(new Intent (Close.this , Chathead.class));
			finish();
		}catch(Exception e){
			
		}
		
	}
		
		
		
		
	}

}
