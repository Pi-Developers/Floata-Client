package pidevelopers.floata;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class Setting extends Activity {

	RadioGroup radioGroup1;
	RadioButton def, pen, bird, idea;
	String r11,r151;

	RadioButton r1, r2, r3, r4;
	SeekBar seekBar1 = null;
	int progressChanged = 0;
	static SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_setting);
	
		
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

		r1 = (RadioButton) findViewById(R.id.r1);
		r2 = (RadioButton) findViewById(R.id.r2);
		r3 = (RadioButton) findViewById(R.id.r3);
		r4 = (RadioButton) findViewById(R.id.r4);
 		loadon();
 		
	if(r11.equals(null)){
			r1.setChecked(true);

	}
		else if (r11.equals("1")) {
 			r1.setChecked(true);
 		} else if (r11.equals("2")) {
 			r2.setChecked(true);
 	} else if (r11.equals("3")) {
 			r3.setChecked(true);
 		}
 		else if (r11.equals("")) {
  			r1.setChecked(true);
    	}
	
	
		seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		
		loadseek();
		
		if(r151.equals(null) || r151.equals("") ){
			
		}else{
			
			seekBar1.setProgress(Integer.parseInt(r151));
		}

		seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@SuppressWarnings("deprecation")
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				progressChanged = progress;
			
				
				if (Chathead.twitter != null) {

					Chathead.twitter.setAlpha(progress);

				} else {

					Toast.makeText(getApplicationContext(),
							"Open floata first", Toast.LENGTH_SHORT).show();

				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				
			
				Toast.makeText(Setting.this,
						"Transperancy : " + progressChanged, Toast.LENGTH_SHORT)
						.show();
				save("tran" , String.valueOf(progressChanged) );
			}
		});

	}

	public void onRadioButtonClicked(View view) {

		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.r1:
			if (checked)
				if (Chathead.twitter != null) {
					Chathead.twitter.setImageResource(R.drawable.not);

 					save("image", "1");

				} else {
					Toast.makeText(getApplicationContext(),
							"Open floata first", Toast.LENGTH_SHORT).show();
				}

			break;
		case R.id.r2:
			if (checked)
				if (Chathead.twitter != null) {
					Chathead.twitter.setImageResource(R.drawable.twitter);
 					save("image", "2");

				} else {
					Toast.makeText(getApplicationContext(),
							"Open floata first", Toast.LENGTH_SHORT).show();

				}

			break;
		case R.id.r3:
			if (checked)
				if (Chathead.twitter != null) {
					Chathead.twitter.setImageResource(R.drawable.ideabubble);
 				save("image", "3");

				} else {

					Toast.makeText(getApplicationContext(),
							"Open floata first", Toast.LENGTH_SHORT).show();
				}

			break;

		}
	}

	public void save(String key, String value) {
		ServiceL.spfs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = MainActivity.spf.edit();
		edit.putString(key, value);
		edit.commit();

	}

  public void loadon() {
 
	  Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
 		r11 = Chathead.spf.getString("image", "");
 
 	}
  public void loadseek() {
	  
	  Chathead.spf = PreferenceManager.getDefaultSharedPreferences(this);
 		r151 = Chathead.spf.getString("tran", "");
 
 	}
}