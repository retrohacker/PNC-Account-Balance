package o.fcour.se.pncbal;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class PNCSERVICE extends BroadcastReceiver {
	
	enum CMD {
		BAL
	}

	private final static String phoneNumber = "762265";
	private static String accountName;
	public static Context widget;
	public static final String sms_received = "android.provider.Telephony.SMS_RECEIVED";
	private static String balance = "0";
	private static Application app;
	
	public static void execute(CMD command) {
		switch(command) {
		case BAL:
			balance();
			break;
		}
	}
	
	public static void setApp(Application a) {
		app = a;
	}
	
	public static void SetAccountName(String name) {
		accountName = name;
	}
	
	public static boolean AccountSet() {
		return accountName!=null;
	}
	
	private static void balance() {
		Log.d("PNC","Sending BAL SMS");
		if(AccountSet())
			SmsManager.getDefault().sendTextMessage(phoneNumber, null, "BAL "+accountName, null, null);
	}
	
	public static String getBalance() {
		Log.d("PNCSERVICE","Returning Balance: "+balance);
		return balance;
	}

	public void onReceive(Context c, Intent intent) {
		if(!intent.getAction().equalsIgnoreCase(sms_received))
			return;
	
		Bundle b = intent.getExtras();
		if(b==null)
			return;
		Object[]  pdus = (Object[]) b.get("pdus");
		SmsMessage msg;
		String content = "";
		for(int i=0;i<pdus.length;i++) {
			msg = SmsMessage.createFromPdu((byte[])pdus[i]);
			content = msg.getDisplayMessageBody();
			Log.d("PNC","TEST: "+content);
			if(parseType(content).equalsIgnoreCase("BAL")) {
				PNCSERVICE.balance = parseBalance(content);
			}
			Log.d("PNC","New Balance: "+balance);
		}
		Log.d("PNCSERVICE","Updating Widgets");
		Intent update = new Intent("o.fcour.se.pncbal.PNCWidget");
		update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app,PNCWidget.class));
		update.putExtra("widget_ids", ids);
		app.sendBroadcast(update);
	}
	
	public static String parseType(String pncmessage) {
		if(pncmessage.contains("BAL")) return "BAL";
		return "INVALID";
	}
	
	public static String parseBalance(String pncmessage) {
		String[] s = pncmessage.split("\\$");
		if(s.length<2) return "PARSE ERROR";
		else
		return s[1];
	}
}
