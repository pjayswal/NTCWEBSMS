package com.example.ntcwebsms;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NtcWebSms extends Activity {	
	WebView webView;
	TextView errorText;
	Button reTry;
	
	static int LOGIN_PAGE=1,MESSEGE_PAGE=2,REGISTRATION_PAGE=3,FORGETPASS_PAGE=4,VALIDATION_PAGE=5;
	static String loginScript,UserName,messegeScript,registerScript,forgetScript,validScript;
	ProgressDialog pdialog;
	String Url;
	
	static int activity,requestCode;
	boolean timeout=false;
	static final int requestCod=0;
	Intent intent;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        
        reTry=(Button) findViewById(R.id.button1);
        errorText=(TextView) findViewById(R.id.textView1);
        
        Url="http://websms.ntc.net.np/";
        webView = (WebView) findViewById(R.id.webView1);
        webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVisibility(0);
		webView.setBackgroundColor(0);
		checkAndstart(); 
		
		reTry.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				timeout=false;
				checkAndstart();
			}
		});
    }
	
	public Handler closeHandler = new Handler() {
		  public void handleMessage(Message msg) {
		    if (!pdialog.isShowing()){ pdialog.dismiss();
		    	errorText.setText("Server is not responding,Plz Try Later!!!");
		    	System.out.println("Tag"+"serverNotResponding");
		    	webView.stopLoading();
		    }
		  }
		};
	public void openDialog() {
		errorText.setText("Please Wait!!");
		pdialog =  ProgressDialog.show(this, "","Loading. Please wait...", false);
     	pdialog.setCancelable(true);  	 
		closeHandler.sendEmptyMessageDelayed(0, 30000);
	}
    @Override
    protected void onResume() {
    	super.onResume();
    	System.out.println("Tag "+"OnResume req"+requestCode+" "+activity);
    	if (getIntent().getBooleanExtra("EXIT", false)) {
    		android.os.Process.killProcess(android.os.Process.myPid());
    	}
    	if(!timeout){
    		openDialog();
    	  	
    		if(requestCode==3)
    		{
    			requestCode=0;
    			webView.loadUrl("http://websms.ntc.net.np/");
    		}
    		else if(activity==LOGIN_PAGE){
    			
    			webView.loadUrl(loginScript);
    		} 			
    		else if(activity==MESSEGE_PAGE){
    			System.out.println("Tag "+"OnResume execute script:"+messegeScript+activity);
    			//webView.loadUrl("http://websms.ntc.net.np/");
    			webView.loadUrl(messegeScript);
    		}
    		else if(activity==REGISTRATION_PAGE){
    			webView.loadUrl(registerScript);
    		}
    		else if(activity==FORGETPASS_PAGE){
    			webView.loadUrl(forgetScript);
    		}
    		else if(activity==VALIDATION_PAGE){
    		webView.loadUrl(validScript);
    		}
    	}
    }
   
	public class MyWebViewClient extends WebViewClient {	
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	if (url.equals("http://websms.ntc.net.np/")
	    			||url.equals("http://websms.ntc.net.np/index.jsp")
	    			||url.equals("http://websms.ntc.net.np/registrationIndex.jsp")
	    			||url.equals("http://websms.ntc.net.np/passRequest.jsp")
	    			||url.equals("http://websms.ntc.net.np/login.jsp")
	    			||url.equals("http://websms.ntc.net.np/login_forget.jsp")) {
	        	return false;
	        }
	    	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(intent);
	        return true;       
	    }
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    	super.onPageStarted(view, url, favicon);
	    	if(pdialog==null)
	    		openDialog();
	    	System.out.println("Tag "+"PageStarted:"+activity+url+" :");
	    }
		@Override
		public void onPageFinished(WebView view, String url) {
			System.out.println("Tag "+"PageFinished:"+url+" :"+activity);
				if(url.equals("http://websms.ntc.net.np/login.jsp")){
					webView.loadUrl("javascript:(function() { " + 
							"if($('#session-message').length!=0){"+
							"alert(document.getElementById('session-message').innerHTML);}"+
							"if($('#session-message').length==0){ alert(null);}"+
							 "})()");
					System.out.println("Tag "+"javaScript Loaded!!!");
				}
			if(url.equals("http://websms.ntc.net.np/")){
				//activity=MESSEGE_PAGE;
				//intent=new Intent(NtcWebSms.this,MessageForm.class);
				activity=LOGIN_PAGE;			
				intent=new Intent(NtcWebSms.this,Login.class);
				intent.putExtra("error","");
				//startActivity(intent);
							
			}
			else if(url.equals("http://websms.ntc.net.np/registration_new.jsp")){
				activity=LOGIN_PAGE;
				intent=new Intent(NtcWebSms.this,Login.class);
				intent.putExtra("error","Registration Failure : Invalid Username or Password ");
			}
			else if(url.equals("http://websms.ntc.net.np/index.jsp")){
				activity=LOGIN_PAGE;
				intent=new Intent(NtcWebSms.this,Login.class);
				intent.putExtra("error","");
			}
			else if(url.equals("http://websms.ntc.net.np/msgForm.jsp")/*||url.equals("http://websms.ntc.net.np/login.jsp")*/){
				activity=MESSEGE_PAGE;
				intent=new Intent(NtcWebSms.this,MessageForm.class);
				//intent.putExtra("error","");
				Toast.makeText(NtcWebSms.this,"Your message has been queued for delivery",Toast.LENGTH_LONG).show();
			}	
			else if(url.equals("http://websms.ntc.net.np/passRequest.jsp")){
				activity=FORGETPASS_PAGE;
				intent=new Intent(NtcWebSms.this,ForgetPassword.class);
				//intent.putExtra("error","");
			}
			else if(url.equals("http://websms.ntc.net.np/registrationIndex.jsp")){
				activity=REGISTRATION_PAGE;
				intent=new Intent(NtcWebSms.this,Register.class);
				//intent.putExtra("error","");
			}
			else if(url.equals("http://websms.ntc.net.np/login_forget.jsp")){
				activity=VALIDATION_PAGE;
				intent=new Intent(NtcWebSms.this,Validation.class);
				//intent.putExtra("error","");
			}
			intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
			//startActivity(intent);
			startActivityForResult(intent, 0);
			
			Url=url;
			if(pdialog!=null)
			if(pdialog.isShowing())
		    pdialog.dismiss();
			super.onPageFinished(view, url);
		}
	
	}
	public class MyWebChromeClient extends WebChromeClient{
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			System.out.println("Tag "+"Dialog:"+activity+":"+message);
			if(Url.equals("http://websms.ntc.net.np/login.jsp")){
				if(message.equals("null")){
					activity=MESSEGE_PAGE;
					Intent intent=new Intent(NtcWebSms.this,MessageForm.class);
					startActivity(intent);
				}
				else{
					System.out.println("Tag "+"loginAgain"+activity);
					activity=LOGIN_PAGE;
					Intent intent=new Intent(NtcWebSms.this,Login.class);
					intent.putExtra("error",message);
					startActivity(intent);
					
				}
				//Toast.makeText(NtcWebSms.this, message, 3000).show();
			}
			else if(activity==REGISTRATION_PAGE)
			{			
				Intent intent=new Intent(NtcWebSms.this,Register.class);
				startActivity(intent);
				Toast.makeText(NtcWebSms.this, message, 3000).show();
			}
			else if(activity==FORGETPASS_PAGE)
			{
				Intent intent=new Intent(NtcWebSms.this,ForgetPassword.class);
				startActivity(intent);
				Toast.makeText(NtcWebSms.this, message, 3000).show();
			}
	        result.confirm();
	        return true;
		}
	}
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(pdialog!=null)
		 if(pdialog.isShowing())
		    	pdialog.dismiss();
				
			if((keyCode==KeyEvent.KEYCODE_BACK)){
				new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
		        	.setTitle(R.string.quit)
		        	.setMessage(R.string.realy_quit)
		        	.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		            	android.os.Process.killProcess(android.os.Process.myPid());   
		                
		            }
		        }).setNegativeButton(R.string.no, null).show();
		        return true;
			}	    
			return super.onKeyDown(keyCode, event);
		}
    public void checkAndstart()
	{
    	System.out.println("Tag "+"NetworkTest");
    	ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
    
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
        	errorText.setText("Network Found!!!");
        	webView.loadUrl("http://websms.ntc.net.np/");
        	System.out.println("Tag "+"NetwortFound");
        	timeout=false;
        } 
        else 
        {
        	 timeout=true;
        	 errorText.setText("Network Problem,Please Try Later!!!");
        }
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
			final Dialog d=new Dialog(NtcWebSms.this);
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
