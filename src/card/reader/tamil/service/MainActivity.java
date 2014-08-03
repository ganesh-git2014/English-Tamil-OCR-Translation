package card.reader.tamil.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import card.reader.tamil.service.RequestTask;
import card.reader.tamil.service.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener,AsyncResponse {
	
	
	static final int  REQUEST_TAKE_PHOTO = 1;
	private final int IMAGE_PICKER_REQUEST = 99;
	private final int RESPONSE_OK = 200;
	String URL="";
	 ProgressDialog dialog1;
	RequestTask asncTask;

	String mCurrentPhotoPath;
	ImageView iv;
	TextView tv;
	Button b1,b2;
	
	boolean take_picture=false;
	
	RequestTask asyncTask;
	private String apiKey="";
	private String langCode="en";
	private String fileName;
	String convertedText="World";
	String transalatedText="";
	Context mContect=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		b1= (Button )findViewById(R.id.button1);
		b2=(Button )findViewById(R.id.button2);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		tv=(TextView) findViewById(R.id.textView1);
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setTextIsSelectable (true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	
	
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	            Log.d("Location:,",photoFile+"");
	            fileName=photoFile+"";
	            Log.d("Filename Check:",fileName);
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	           
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	
	    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK&&take_picture==true) {
//	        Bundle extras = data.getExtras();
//	        Bitmap imageBitmap = (Bitmap) extras.get("data");
//	        iv.setImageBitmap(imageBitmap);
	    	take_picture=false;
	    	Log.d("Filename:",fileName+"");
	    	Log.d("key:",apiKey+"");
	    	Log.d("lang:",langCode+"");
	    	
	    	if (fileName != null && !apiKey.equals("") && !langCode.equals("")) {
	    		
				final ProgressDialog dialog = ProgressDialog.show( MainActivity.this, "Loading ...", "Converting to text.", true, false);
				final Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						final OCRServiceApi apiClient = new OCRServiceApi(apiKey);
						apiClient.convertToText(langCode, fileName);

						// Doing UI related code in UI thread
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();

								convertedText=apiClient.getResponseText();
								URL="http://api.mymemory.translated.net/get?q="+Uri.encode(convertedText)+"&langpair=en%7Cta-IN";
								Log.d("URL::",URL);
								asyncTask= (RequestTask) new RequestTask(MainActivity.this ).execute(URL);
								
								// Showing response dialog
//								final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//								alert.setMessage(apiClient.getResponseText());
//								Log.d("RESPONSEEEE::",apiClient.getResponseText());
//								alert.setPositiveButton(
//									"OK",
//									new DialogInterface.OnClickListener() {
//										public void onClick( DialogInterface dialog, int id) {
//										}
//									});
//
//								// Setting dialog title related from response code
//								if (apiClient.getResponseCode() == RESPONSE_OK) {
//									alert.setTitle("Success");
//								} else {
//									alert.setTitle("Faild");
//								}
//
//								alert.show();
							}
						});
					}
				});
				thread.start();
			} else {
				Toast.makeText(MainActivity.this, "All data are required.", Toast.LENGTH_SHORT).show();
			}
	    }
	    
	    
	    if (requestCode == IMAGE_PICKER_REQUEST &&resultCode == RESULT_OK&&take_picture==false) {
			fileName = getRealPathFromURI(data.getData());
			//picNameText.setText("Selected: en" + getStringNameFromRealPath(fileName));
if (fileName != null && !apiKey.equals("") && !langCode.equals("")) {
	    		
				final ProgressDialog dialog = ProgressDialog.show( MainActivity.this, "Loading ...", "Converting to text.", true, false);
				final Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						final OCRServiceApi apiClient = new OCRServiceApi(apiKey);
						apiClient.convertToText(langCode, fileName);

						// Doing UI related code in UI thread
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								 dialog1 = ProgressDialog.show( MainActivity.this, "Loading ...", "Converting to Tamil.", true, false);
								convertedText=apiClient.getResponseText();
								URL="http://api.mymemory.translated.net/get?q="+Uri.encode(convertedText)+"&langpair=en%7Cta-IN";
								Log.d("URL::",URL);
								asyncTask= (RequestTask) new RequestTask(MainActivity.this ).execute(URL);
								
								// Showing response dialog
//								final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//								alert.setMessage(apiClient.getResponseText());
//								Log.d("RESPONSEEEE::",apiClient.getResponseText());
//								alert.setPositiveButton(
//									"OK",
//									new DialogInterface.OnClickListener() {
//										public void onClick( DialogInterface dialog, int id) {
//										}
//									});
//
//								// Setting dialog title related from response code
//								if (apiClient.getResponseCode() == RESPONSE_OK) {
//									alert.setTitle("Success");
//								} else {
//									alert.setTitle("Faild");
//								}
//
//								alert.show();
							}
						});
					}
				});
				thread.start();
			} else {
				Toast.makeText(MainActivity.this, "All data are required.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private String getRealPathFromURI(final Uri contentUri) {
		final String[] proj = { MediaStore.Images.Media.DATA };
		final Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}


	private String getStringNameFromRealPath(final String bucketName) {
		return bucketName.lastIndexOf('/') > 0 ? bucketName.substring(bucketName.lastIndexOf('/') + 1) : bucketName;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.button1:
			//dispatchTakePictureIntent();
			startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGE_PICKER_REQUEST);
		//Intent i=new Intent(this,WebViewActivity.class);
		//startActivity(i);
			break;
		case R.id.button2:
			take_picture=true;
			dispatchTakePictureIntent();
			break;
		}
		
	}

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		dialog1.dismiss();
		JSONObject temp=null;
		String value="";
		try {
			temp=new JSONObject(output);
			temp=temp.getJSONObject("responseData");
			Log.d("TEMp is:",temp.toString());
			value=temp.getString("translatedText");
//			value=temp.getJS("translatedText").toString();
			Log.d("TEMp is:",temp.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv.setText(value.toString());
	}
}

