package o.fcour.se.pncbal;

import o.fcour.se.pncbal.PNCSERVICE.CMD;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Refresh extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("PNC Widget","Refresh onCreate()");
		PNCSERVICE.setApp(getApplication());
		PNCSERVICE.execute(CMD.BAL);
		finish();
	}
}
