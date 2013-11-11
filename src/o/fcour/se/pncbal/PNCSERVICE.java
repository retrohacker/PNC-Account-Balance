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
	public static final String sms_received = "android.provider.Telephony.SMS_RECEIVED";
	private static String balance = "0";
	private static Application app;
	private static String parseErrorMsg = "PARSE ERROR";
	
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
		Log.d("PNCSERVICE","RECEIVED INTENT");

		//Ensure we are supposed to be reacting to this intent
		if(!intent.getAction().equalsIgnoreCase(sms_received))
			return;

		Bundle b = intent.getExtras();
		if(b==null)
			return;

		//Parse messages
		Object[]  pdus = (Object[]) b.get("pdus");
		for(Object pdu : pdus) {
			SmsMessage msg = SmsMessage.createFromPdu((byte[])pdu);

			if(!msg.getOriginatingAddress().equalsIgnoreCase(phoneNumber))
				continue;

			String content = msg.getDisplayMessageBody();
			Log.d("PNCSERVICE","Received msg from PNC: "+content);

			switch(responseType(content)) {
			case BAL:
				String tempBalance = parseBalance(content);
				if(!tempBalance.contentEquals(parseErrorMsg)) {
					PNCSERVICE.balance = tempBalance;
					Log.d("PNCSERVICE","New Balance: "+balance);
				} else {
					Log.w("PNCSERVICE","Received corrupt message from PNC");
				}
				break;
			default:
				Log.w("PNCSERVICE", "Unimplemented msg received from PNC");
				break;
			}

		}

		//Update all widgets
		Log.d("PNCSERVICE","Updating Widgets");
		Intent update = new Intent("o.fcour.se.pncbal.PNCWidget");
		update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app,PNCWidget.class));
		update.putExtra("widget_ids", ids);
		app.sendBroadcast(update);
	}

	public static CMD responseType(String pncmessage) {
		if(pncmessage.contains("BAL")) return CMD.BAL;
		return null;
	}

	public static String parseBalance(String pncmessage) {
		String[] s = pncmessage.split("\\$");
		if(s.length<2) return parseErrorMsg;
		else
		return s[1];
	}
}