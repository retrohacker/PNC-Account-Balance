package o.fcour.se.pncbal;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;

public class Config extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		Button sendsms = (Button) this.findViewById(R.id.sendsms);
		sendsms.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.sendsms:
			setupWidget();
			break;
		}	
	}
	
	private void setupWidget() {
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

		Intent update = new Intent("o.fcour.se.pncbal.PNCWidget");
		update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this,PNCWidget.class));
		update.putExtra("widget_ids", ids);
		this.sendBroadcast(update);
		
		finish();
	}
}
