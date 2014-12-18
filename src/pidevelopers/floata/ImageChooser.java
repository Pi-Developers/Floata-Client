package pidevelopers.floata;

import wei.mark.standout.StandOutWindow;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;


public class ImageChooser extends Activity {
	String picturePath;
	Intent i;
	  SharedPreferences spf;
	    Editor edit;
	
	private static int RESULT_LOAD_IMAGE = 1;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		spf = PreferenceManager.getDefaultSharedPreferences(this);

	i = new Intent(
			Intent.ACTION_PICK,
			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

	 this.startActivityForResult(i , RESULT_LOAD_IMAGE);
	 
	 

	
	}
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			
			save("img" , picturePath);
			
			
			StandOutWindow
			.show(ImageChooser.this, FloatingActivity.class, StandOutWindow.DEFAULT_ID);
			
			finish();

			

		}
		
	}
	
	
	
	
	
	public void save(String key, String value) {
		edit = spf.edit();
		edit.putString(key, value);
		edit.commit();

	}
	
}