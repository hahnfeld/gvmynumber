package com.everysoft.gvmynumber;

import com.everysoft.gvmynumber.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class GVMyNumberBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String intentAction = intent.getAction();
		String gateway_prov_pkg = intent.getStringExtra("com.android.phone.extra.GATEWAY_PROVIDER_PACKAGE");
		String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

		// only handle calls from google voice
		if (
			intentAction.equals(Intent.ACTION_NEW_OUTGOING_CALL)
			&& gateway_prov_pkg != null
			&& gateway_prov_pkg.equals("com.google.android.apps.googlevoice")
		) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			String gv_number = Uri.encode(prefs.getString("gv_number", null));
			String pin = Uri.encode(prefs.getString("PIN", null));
						
			if (gv_number != null && !gv_number.equals("")) {				
				Intent newIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0000000")); // google voice ignores 7-digit calling
				newIntent.putExtra("com.android.phone.extra.GATEWAY_PROVIDER_PACKAGE", "com.everysoft.gvmynumber");

				String clean_number = number;
				if (clean_number.startsWith("+1")) {
					clean_number = clean_number.substring(2);
				}
				
				/* dial out using our own google voice number, and append the original dialed number to the end as the fragment */
				String newUri = "tel:" + Uri.encode(gv_number + " " + prefs.getString("pause", ",") + (pin != null ? pin + ",," : "") + "2," + clean_number + "#") + "#" + Uri.encode(number);
				newIntent.putExtra("com.android.phone.extra.GATEWAY_URI", newUri);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(newIntent);
				setResultData(null);
			}
			else {
            	Toast confirmMsg = Toast.makeText(context, R.string.need_settings, Toast.LENGTH_LONG);
            	confirmMsg.show();
			}
		}
		else if (
			intentAction.equals(Intent.ACTION_NEW_OUTGOING_CALL)
			&& gateway_prov_pkg != null
			&& gateway_prov_pkg.equals("com.everysoft.gvmynumber")
			&& number.equals("0000000")
		) {
			String gateway_uri = intent.getStringExtra("com.android.phone.extra.GATEWAY_URI");
			setResultData(Uri.parse(gateway_uri).getFragment());
		}
		// else just let the number pass through
	}
}
