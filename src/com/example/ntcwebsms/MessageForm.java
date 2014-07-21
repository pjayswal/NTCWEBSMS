package com.example.ntcwebsms;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;

public class MessageForm extends Activity implements OnClickListener {
	EditText recipientNoInput;
	EditText messegeInput;
	TextView charLeft;
	Button Send,reset;
	ImageButton addContact;
	CheckBox sendTome;
	String messege,recipientNo;
	int flag,msgLen;
	int result=100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messege);
		
		recipientNoInput=(EditText) findViewById(R.id.editText1);
		messegeInput=(EditText) findViewById(R.id.editText2);
		
		charLeft=(TextView) findViewById(R.id.textView5);
		Send=(Button) findViewById(R.id.button1);
		reset=(Button) findViewById(R.id.button2);
		addContact=(ImageButton) findViewById(R.id.imageButton1);
		
		sendTome=(CheckBox) findViewById(R.id.checkBox1);
		
		charLeft.setText("142 /"+"142");
		
		Send.setOnClickListener(this);
		reset.setOnClickListener(this);
		addContact.setOnClickListener(this);
		//addContact.setBackgroundDrawable(android.R.drawable.ic_menu_add);
		flag=0;msgLen=0;
		messegeInput.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}	
			public void afterTextChanged(Editable s) {
				msgLen=messegeInput.getText().toString().length();
				if(msgLen==142){
					messegeInput.setTextColor(Color.RED);
					Toast.makeText(MessageForm.this,"Your message has reached the maximum allowed length of 142 characters !!", Toast.LENGTH_LONG).show();
					flag=1;
				}
				else if(flag==1){
					messegeInput.setTextColor(Color.BLACK);
					flag=0;
				}
				charLeft.setText(""+(142-msgLen)+" /"+"142");
			}
		});
	}
	public void onClick(View arg0) {
		 if(arg0.getId()==R.id.button1){
			 recipientNo=recipientNoInput.getText().toString();
			 if(sendTome.isChecked()){
				 if(recipientNo.length()!=0)
					 recipientNo+=";";
				 recipientNo+=NtcWebSms.UserName;
				 recipientNoInput.setText(recipientNo);
			}
			
			 messege=messegeInput.getText().toString();
			 messege=messege.replace('\n',' ');
			 NtcWebSms.messegeScript="javascript:(function(){" +
						"document.getElementsByName('dest_no')[0].value='"+recipientNo+"';"+  
						"document.getElementById('message').value=\""+messege+"\";"+
						"document.getElementById('submt').click();"+
								"})()";
			 Intent intent=new Intent(this,NtcWebSms.class);
			 setResult(1);
			 finish();
		 }
		 else if(arg0.getId()==R.id.button2){
			 messegeInput.setText("");
			 messege=null;
			 recipientNoInput.setText("");
			 recipientNo=null;
		 }
		 else
		 {
			 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			 startActivityForResult(intent, result);
		 }
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
				final Dialog d=new Dialog(MessageForm.this);
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
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	
			String id,name;

	    	super.onActivityResult(requestCode, resultCode, data);
	    	ContentResolver cr = getContentResolver();
	    	Cursor cursor=cr.query(data.getData(), null,null,null, null);
	    	while (cursor.moveToNext()) {
	    		 id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
	        	 name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	        	Toast.makeText(MessageForm.this, "Id:"+id+"Name: " + name, Toast.LENGTH_SHORT).show();
	        	if (Integer.parseInt(cursor.getString(
	                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
	        		
	        		Cursor pCur = cr.query(
	                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	                        null,
	                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
	                        new String[]{id}, null);
	        		
	        		final ArrayList<String> phonesList = new ArrayList<String>();
	        		while (pCur.moveToNext()) {
	        			String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                    phonesList.add(phoneNo);
	        		}
	        		pCur.close();
	        		if (phonesList.size() == 0) 
	                    Toast.makeText(this, "No Phone Number in Contact",Toast.LENGTH_SHORT).show();
	        		else {
	       			 final String[] phonesArr = new String[phonesList.size()];
	                    for (int i = 0; i < phonesList.size(); i++)
	                        phonesArr[i] = phonesList.get(i);
	                    AlertDialog.Builder dialog = new AlertDialog.Builder(MessageForm.this);
	                    dialog.setTitle("Choose Phone Number");
	                    ((Builder) dialog).setItems(phonesArr,
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(DialogInterface dialog,int which) {
	                                     String selectedPhone = phonesArr[which];
	                                     if(selectedPhone.length()>10)
	                                     selectedPhone=ProcessNumber(selectedPhone);
	                                     recipientNo=recipientNoInput.getText().toString();
	                                     Toast.makeText(MessageForm.this,recipientNo+ recipientNo.length() , Toast.LENGTH_SHORT).show();
	                                     if(recipientNo.length()!=0)
	                     	    			recipientNo+=";";
	                     	    		recipientNo+=selectedPhone;
	                     	    		recipientNoInput.setText(recipientNo);
	                                    Toast.makeText(MessageForm.this, " Phone No: " + selectedPhone, Toast.LENGTH_SHORT).show();
	                                }

									private String ProcessNumber(String num) {
										int len=num.length();
										len--;
										char S[]=new char[10];
										int i=9;
										while(i>=0){
											if( num.charAt(len)>=48&&num.charAt(len)<=57){
												S[i]=num.charAt(len);
												i--;
												len--;
											}
											else
												len--;
										}
										String s=new String(S);
										return s;
									}
	                            }).create();
	                    dialog.show();
	       		    }
	        	}
	    	}
	    }
	    @Override
	    protected void onResume() {
	    	System.out.println("Tag MesgForm Resumed");
	    	super.onResume();
	    }

}
