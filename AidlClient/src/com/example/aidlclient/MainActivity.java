package com.example.aidlclient;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aidlserver.IRemote;

public class MainActivity extends Activity implements OnClickListener  {

	EditText mFirst,mSecond;
	Button mAdd,mSubtract,mClear;
	TextView mResultText;
	protected IRemote mService;
	ServiceConnection mServiceConnection;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFirst = (EditText) findViewById(R.id.firstValue);
		mSecond = (EditText) findViewById(R.id.secondValue);
	    mResultText = (TextView) findViewById(R.id.resultText);
		mAdd = (Button) findViewById(R.id.add);
		mAdd.setOnClickListener(this);
	
		initConnection();

	}
	void initConnection(){
	    mServiceConnection = new ServiceConnection() {

			

				@Override
				public void onServiceDisconnected(ComponentName name) {
					// TODO Auto-generated method stub
					mService = null;
					Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
					Log.d("IRemote", "Binding - Service disconnected");
				}

				@Override
				public void onServiceConnected(ComponentName name, IBinder service)
				{
					// TODO Auto-generated method stub
					mService = IRemote.Stub.asInterface((IBinder) service);
					Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
					Log.d("IRemote", "Binding is done - Service connected");
				}
			};
			if(mService == null)
			{
				Intent it = new Intent();
				it.setAction("com.remote.service.CALCULATOR");
				//binding to remote service
				bindService(it, mServiceConnection, Service.BIND_AUTO_CREATE);
			}
	}
	protected void onDestroy() {

		super.onDestroy();
		unbindService(mServiceConnection);
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.add:{
			int a = Integer.parseInt(mFirst.getText().toString());
			int b = Integer.parseInt(mSecond.getText().toString());

			try{
				mResultText.setText("Result -> Add ->"+mService.add(a,b));
				Log.d("IRemote", "Binding - Add operation");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		break;
	  }
	}
}
