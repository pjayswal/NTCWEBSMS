package com.example.ntcwebsms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	EditText instruction,userNumber,userPassword;
	Button register;
	TextView terms;
	CheckBox Agree;
	static String dialogmessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("Tag Registration Page Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		instruction=(EditText) findViewById(R.id.editText1);
		userNumber=(EditText) findViewById(R.id.editText2);
		userPassword=(EditText) findViewById(R.id.editText3);
		register=(Button) findViewById(R.id.button1);
		terms=(TextView) findViewById(R.id.textView4);
		instruction.setText(R.string.instruction);
		Agree=(CheckBox) findViewById(R.id.checkBox1);
		instruction.setFocusable(false);
		instruction.setEnabled(false);
		
		terms.setOnTouchListener(new OnTouchListener() {	
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://websms.ntc.net.np/terms.jsp"));
		        startActivity(intent);
				return false;
			}
		});  
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(userNumber.length()==0)
					Toast.makeText(Register.this, "$$$Please,Enter Valid Username$$$", Toast.LENGTH_SHORT).show();
				else if(userPassword.length()==0)
					Toast.makeText(Register.this, "Please enter PUK/SIM Serial Number/R-UIM Number/FWP Number/Password", Toast.LENGTH_SHORT).show();
				else if(!Agree.isChecked()){
					Toast.makeText(Register.this, "Sorry, you must Agree Terms and Conditions.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					NtcWebSms.registerScript="javascript:(function() { " +
							"document.getElementById('ntc_user').value='"+userNumber.getText().toString()+"';"+
							"document.getElementById('ntc_pass').value='"+userPassword.getText().toString()+ "';" +
							"document.getElementById('submit').click();"+
									"})()";
					Intent intent=new Intent();
					NtcWebSms.requestCode=3;
					setResult(3,intent);
					finish();
				}
			}
		});
		
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
				final Dialog d=new Dialog(Register.this);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.dialog);
				TextView dialogText=(TextView)d.findViewById(R.id.textView2);
				dialogText.setText("Developed by: Pramod Jayswal \n" +
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

}
