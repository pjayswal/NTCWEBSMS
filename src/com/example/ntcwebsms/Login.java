package com.example.ntcwebsms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener  {
	EditText loginId,passwd;
	Button signIn,forget,signUp;
	TextView errMsg;
	CheckBox RemPass;
	static String username,password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ntc_web_sms);
		loginId=(EditText) findViewById(R.id.editText1);
        passwd=(EditText) findViewById(R.id.editText2);
        signIn=(Button) findViewById(R.id.button13);
        forget=(Button) findViewById(R.id.button2);
        signUp=(Button) findViewById(R.id.button3);
        errMsg=(TextView) findViewById(R.id.textView5);
        RemPass=(CheckBox) findViewById(R.id.checkBox1);
        signIn.setOnClickListener(this);
        forget.setOnClickListener(this);
        signUp.setOnClickListener(this);
        
        SharedPreferences credential=getSharedPreferences("cred",0);
        loginId.setText(credential.getString("userId",""));
        passwd.setText(credential.getString("passwd",""));
        System.out.println("Tag "+"Login Page statrted!!"+getIntent().getExtras().getString("error"));
      
        errMsg.setText(getIntent().getExtras().getString("error"));
		
	}
	public void onClick(View v) {
		if(v.getId()==R.id.button13){//Sign In 
			username=loginId.getText().toString();
			password=passwd.getText().toString();
			if(RemPass.isChecked()){
				 SharedPreferences credential=getSharedPreferences("cred",0);
				 SharedPreferences.Editor editor=credential.edit();
				 editor.putString("userId",username);
				 editor.putString("passwd",password);
				 editor.commit();
			}
			NtcWebSms.loginScript="javascript:(function() { " +
			"document.getElementById('username').value='"+username+"';"+
			"document.getElementById('password').value='"+password+ "';" +
			"document.getElementById('submit').click();"+
					"})()";
			NtcWebSms.UserName="9849087988";
			Intent intent=new Intent(Login.this,NtcWebSms.class);
			Toast.makeText(Login.this,"Signing In!!!", 3000).show();
			setResult(1);
			finish();
		}
		else if(v.getId()==R.id.button2){//forget Password
			NtcWebSms.loginScript="http://websms.ntc.net.np/passRequest.jsp";
			Intent intent=new Intent(Login.this,NtcWebSms.class);
			setResult(1);
			finish();
		}
		else if(v.getId()==R.id.button3){//Registration
			NtcWebSms.loginScript="http://websms.ntc.net.np/registrationIndex.jsp";
			Intent intent=new Intent(Login.this,NtcWebSms.class);
			setResult(1);
			finish();
		}
		
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode==KeyEvent.KEYCODE_BACK)){
			System.out.println("Tag "+"Login Page Back Pressed!!");
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
	        	.setTitle(R.string.quit)
	        	.setMessage(R.string.realy_quit)
	        	.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int which) {
	            	Intent intent=new Intent(getApplicationContext(),NtcWebSms.class);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            	intent.putExtra("EXIT", true);
	    			startActivity(intent);
	    			android.os.Process.killProcess(android.os.Process.myPid());
	            }
	            
	        }).setNegativeButton(R.string.no, null).show();
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
				final Dialog d=new Dialog(Login.this);
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
		    	String shareBody = "Send Free sms to any Nepal Telecom number.Get this free app from Market.";
		    	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Get this cool Android App");
		    	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		    	startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
			return super.onOptionsItemSelected(item);
		}

}
