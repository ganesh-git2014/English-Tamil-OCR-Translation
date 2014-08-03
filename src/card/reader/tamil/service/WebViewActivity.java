package card.reader.tamil.service;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	private final static int FILECHOOSER_RESULTCODE=1;  
	private ValueCallback<Uri> mUploadMessage;  
	WebView wv;
	
	@Override  
	 protected void onActivityResult(int requestCode, int resultCode,  
	                                    Intent intent) {  
	  if(requestCode==FILECHOOSER_RESULTCODE)  
	  {  
	   if (null == mUploadMessage) return;  
	            Uri result = intent == null || resultCode != RESULT_OK ? null  
	                    : intent.getData();  
	            mUploadMessage.onReceiveValue(result);  
	            mUploadMessage = null;  
	  }
	  }  

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		wv=(WebView) findViewById(R.id.webView1);
		wv.setWebViewClient(new MyBrowser());
		wv.getSettings().setLoadsImagesAutomatically(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		wv = new WebView(this);  
		
		 wv.loadUrl("http://www.newocr.com/");
		    
		 wv.setWebChromeClient(new WebChromeClient()  
		    {  
		           //The undocumented magic method override  
		           //Eclipse will swear at you if you try to put @Override here  
		        // For Android 3.0+
		        public void openFileChooser(ValueCallback<Uri> uploadMsg) {  

		            mUploadMessage = uploadMsg;  
		            Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
		            i.addCategory(Intent.CATEGORY_OPENABLE);  
		            i.setType("image/*");  
		            WebViewActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  

		           }

		        // For Android 3.0+
		           public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
		           mUploadMessage = uploadMsg;
		           Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		           i.addCategory(Intent.CATEGORY_OPENABLE);
		           i.setType("*/*");
		           WebViewActivity.this.startActivityForResult(
		           Intent.createChooser(i, "File Browser"),
		           FILECHOOSER_RESULTCODE);
		           }

		        //For Android 4.1
		           public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
		               mUploadMessage = uploadMsg;  
		               Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
		               i.addCategory(Intent.CATEGORY_OPENABLE);  
		               i.setType("image/*");  
		               WebViewActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), WebViewActivity.FILECHOOSER_RESULTCODE );

		           }

		    });  


		    setContentView(wv);  

		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}
	public class MyBrowser extends WebViewClient
	{
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        // TODO Auto-generated method stub
	        super.onPageStarted(view, url, favicon);
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // TODO Auto-generated method stub

	        view.loadUrl(url);
	        return true;

	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	        // TODO Auto-generated method stub
	        super.onPageFinished(view, url);

	        
	    }
	}


}
