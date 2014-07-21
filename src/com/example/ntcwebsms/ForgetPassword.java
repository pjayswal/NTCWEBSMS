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

public class ForgetPassword extends Activity implements OnClickListener {
	EditText Instruction,UserId,Passwd;
	Button Submit,ValidationCode;
	Dialog d;
	TextView dialogText;
	static String dialogString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		
		Instruction=(EditText) findViewById(R.id.editText1);
		UserId=(EditText) findViewById(R.id.editText2);
		Passwd=(EditText) findViewById(R.id.editText3);
		Submit=(Button) findViewById(R.id.button11);
		ValidationCode=(Button) findViewById(R.id.button12);
		
		Instruction.setText(R.string.resetinst);
		Instruction.setFocusable(false);
		Instruction.setEnabled(false);
		
		Submit.setOnClickListener(this);
		ValidationCode.setOnClickListener(this);
		
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(R.layout.dialog);
		dialogText=(TextView)d.findViewById(R.id.textView1);
		Button dialogOk=(Button)d.findViewById(R.id.button1);
		dialogOk.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				d.dismiss();			
			}
		});
		
	}
	public void onClick(View v) {
		if(v.getId()==R.id.button11){
			NtcWebSms.forgetScript="javascript:(function() { " +
				"document.getElementById('username').value='"+UserId.getText().toString()+"';"+
				"document.getElementById('userpuk').value='"+Passwd.getText().toString()+ "';" +
				"document.getElementById('submt').click();"+
						"})()";
		}
		else{
			NtcWebSms.forgetScript="http://websms.ntc.net.np/login_forget.jsp";
		}
		Intent intent=new Intent(ForgetPassword.this,NtcWebSms.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
	}
	@Override
	protected void onResume() {
		if(dialogString!=null){
			dialogText.setText(dialogString);
			d.show();
			dialogString=null;
		}
		super.onResume();
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
				final Dialog d=new Dialog(ForgetPassword.this);
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
    
