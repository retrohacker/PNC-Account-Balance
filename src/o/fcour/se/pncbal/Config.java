package o.fcour.se.pncbal;

import o.fcour.se.pncbal.PNCSERVICE.CMD;
import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViews;

public class Config extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		setupWidget();
	}
	
	private void setupWidget() {

		//Initialize PNCSERVICE
		PNCSERVICE.setApp(getApplication());
		PNCSERVICE.SetAccountName("Chek");
		PNCSERVICE.execute(CMD.BAL);
		
		//Setup the Widget view
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null) {
			int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
			RemoteViews view = new RemoteViews(this.getPackageName(),R.layout.activity_widget);
			appWidgetManager.updateAppWidget(widgetId, view);
			Intent result = new Intent();
			result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			setResult(RESULT_OK,result);
		}

		finish();
	}
}
