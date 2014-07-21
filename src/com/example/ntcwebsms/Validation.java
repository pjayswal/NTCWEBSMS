package com.example.ntcwebsms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Validation extends Activity {
	EditText UserId,ValidCode,newPasswd,ConfPass;
	Button LogIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validation);
		
		UserId=(EditText) findViewById(R.id.editText1);
		ValidCode=(EditText) findViewById(R.id.editText2);
		newPasswd=(EditText) findViewById(R.id.editText3);
		ConfPass=(EditText) findViewById(R.id.editText4);
		
		LogIn=(Button) findViewById(R.id.button1);
		LogIn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(newPasswd.getText()!=ConfPass.getText()){
					final Dialog d=new Dialog(Validation.this);
					d.requestWindowFeature(Window.FEATURE_NO_TITLE);
					d.setContentView(R.layout.dialog);
					TextView dialogText=(TextView)d.findViewById(R.id.textView2);
					dialogText.setText("Password Field and Confirm Password Field doesn't match, Please Retype!! ");
					Button dialogOk=(Button)d.findViewById(R.id.button1);
					dialogOk.setOnClickListener(new OnClickListener() {		
						public void onClick(View v) {
							d.dismiss();			
						}
					});
					d.show();
				}
				else{
					NtcWebSms.validScript="javascript:(function() { " +
							"document.getElementById('username').value='"+UserId.getText().toString()+"';"+
							"document.getElementById('validationcode').value='"+ValidCode.getText().toString()+ "';" +
							"document.getElementById('newpassword').value='"+newPasswd.getText().toString()+ "';" +
							"document.getElementById('confnewpassword').value='"+ConfPass.getText().toString()+ "';" +
							"document.getElementById('submit').click();"+
									"})()";
					Intent intent=new Intent(Validation.this,NtcWebSms.class);
					setResult(1);
					finish();
				}
				
			}
		});
	
	}
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.ntc_web_sms, menu);
	        return true;
	    }
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			if(item.getItemId()==R.id.item1)
			{
				final Dialog d=new Dialog(Validation.this);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.dialog);
				TextView dialogText=(TextView)d.findViewById(R.id.textView2);
				dialogText.setText("This Application Uses the Websms Service \n"+"" +
						"of Nepal Doorsanchar Company Limited (Nepal Telecom)\n"+
						"\nDeveloped by: Pramod Jayswal \n" +
						"pramod.jayswal@gmail.com \n" +
						"https://www.facebook.com/pramod.jayswal");
				Button dialogOk=(Button)d.findViewById(R.id.button1);
				dialogOk.setOnClickListener(new OnClickListener() {		
					public void onClick(View v) {
						d.dismiss();			
					}
				});
				d.show();
			}
			else if(item.getItemId()==R.id.item2)
			{
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		    	sharingIntent.setType("text/plain");
		    	String shareBody = "Send Free sms to any Nepal Telecom number.Get this app from Market.It's Free.";
		    	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Get this cool Android App");
		    	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		    	startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
			return super.onOptionsItemSelected(item);
		}
	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
			if((keyCode==KeyEvent.KEYCODE_BACK)){
				System.out.println("Tag Registraion popped!!");
				Intent intent=new Intent();
				NtcWebSms.requestCode=3;
				setResult(3,intent);
				finish();
				return true;
			}	    
			return super.onKeyDown(keyCode, event);
		}

}
