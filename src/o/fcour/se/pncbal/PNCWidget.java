package o.fcour.se.pncbal;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class PNCWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context c,Intent intent) {
		Log.d("PNCWidget", "Received Intent");
		if(intent.hasExtra("widget_ids")) {
			this.onUpdate(c, AppWidgetManager.getInstance(c), intent.getExtras().getIntArray("widget_ids"));
		}
		super.onReceive(c, intent);
	}

	public void onUpdate(Context c, AppWidgetManager awm, int[] widgetIds) {
		for(int id : widgetIds) {
			Log.d("PNC WIDGET", "Updating: "+id);
			RemoteViews view = new RemoteViews(c.getPackageName(),R.layout.activity_widget);
			view.setTextViewText(R.id.textView1, "$"+PNCSERVICE.getBalance());

			Intent intent = new Intent(c,Refresh.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, 0);
			view.setOnClickPendingIntent(R.id.button1, pendingIntent);

			awm.updateAppWidget(id, view);
		}

		super.onUpdate(c, awm, widgetIds);
	}
}
