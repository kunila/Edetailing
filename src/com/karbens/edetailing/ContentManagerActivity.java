package com.karbens.edetailing;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.karbens.application.EdetailingApplication;
import com.karbens.listeners.ContentDownloadListener;
import com.karbens.listeners.DownloadProgressListener;
import com.karbens.model.Brand;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;
import com.karbens.parser.ContentParser;
import com.karbens.utility.DataManager;
import com.karbens.utility.Database;
import com.karbens.utility.HttpUtil;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ContentManagerActivity extends Activity implements ContentDownloadListener,DownloadProgressListener{
	
	LinearLayout gridlin = null;
	Brand mBrand = null;
	Content mContent = null;
	Parent mParent = null;
	Child mChild = null;
	LayoutInflater layoutInflater = null;
	DataManager mDataManager = null;
	ContentParser mParser = null;
	ArrayList<Parent> mParentArr = new ArrayList<Parent>();
	ArrayList<Content> mContentArr = new ArrayList<Content>(); 
    ArrayList<Child> mChildArr = null;
    ContentDownloadListener mListener = null;
    ImageAdapter mAdapter = null;
    
    //for progress bar
    private static int myProgressWaited=0;
    private Runnable myThread ;
	//private int progressStatus=0;
	private Handler myHandler=new Handler();
	boolean proStatus=false;
	
	private int counter=0;
	private long fileSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);
		
		GridView gridView = (GridView) findViewById(R.id.grid_view);
		
		if(mDataManager == null)
		{
			mDataManager=new DataManager(this);
		}
		
		//progressbar
	
		// Instance of ImageAdapter Class
		mAdapter = new ImageAdapter(this);
		gridView.setAdapter(mAdapter);

		
	}
	

	public class ImageAdapter extends BaseAdapter {
		
		private Context mContext;

		// Constructor
		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			if(EdetailingApplication.mBrandArr==null)
			{
				return 0;
			}
			else
			{
				return EdetailingApplication.mBrandArr.get(0).getmContentArr().size();  //mThumbIds.length;
			}
			
		}

		@Override
		public Object getItem(int position) {
			return EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position);  //mThumbIds[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			final int index = position;
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layoutInflater = getLayoutInflater();
			final ProgressBar progressBar;
		
        	View layout = layoutInflater.inflate(R.layout.grid_item,null);
        	ImageView imgView = (ImageView) layout.findViewById(R.id.grid_item_image);
        	imgView.setBackgroundResource(R.drawable.content_frame);
        	
        	final Button downloadbutton = (Button) layout.findViewById(R.id.dwnBtn);
        	
        	final Button resumeDwnBtn = (Button) layout.findViewById(R.id.resumeBtn);
        	
        	final Button pauseButton = (Button) layout.findViewById(R.id.pauseBtn);
        	
        	myProgressWaited=0;
	        progressBar=(ProgressBar)layout.findViewById(R.id.player_exp_bar);
	        
	        int contentSize = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getContentSize();
	        
	        int progressValue = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getProgressValue();
	        
	        if(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getIsDownloading() == true)
	        {
	        	progressBar.setVisibility(View.VISIBLE);
	        	pauseButton.setVisibility(View.VISIBLE);
	        	resumeDwnBtn.setVisibility(View.INVISIBLE);
	        	downloadbutton.setVisibility(View.INVISIBLE);
	        }
	        else
	        {
	        	if(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getDownloadStatus() == 0)
		        {
		        	progressBar.setVisibility(View.INVISIBLE);//(View.INVISIBLE);
					downloadbutton.setVisibility(View.VISIBLE);
					resumeDwnBtn.setVisibility(View.INVISIBLE);
					pauseButton.setVisibility(View.INVISIBLE);
		        }
		        else if(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getDownloadStatus() == 1)
		        {
		        	progressBar.setVisibility(View.INVISIBLE);
					downloadbutton.setVisibility(View.INVISIBLE);
					resumeDwnBtn.setVisibility(View.INVISIBLE);
					pauseButton.setVisibility(View.INVISIBLE);
		        }
		        else if(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getDownloadStatus() == 2)
		        {
		        	progressBar.setVisibility(View.INVISIBLE);
					downloadbutton.setVisibility(View.INVISIBLE);
					resumeDwnBtn.setVisibility(View.VISIBLE);
					pauseButton.setVisibility(View.INVISIBLE);
		        }
	        }
	        
	        
	        	
	        
	        if(contentSize != 0)
	        {
	        	
		        
		        System.out.println("progressValue :"+progressValue);

		        System.out.println("contentSize :"+contentSize);
		        
		        //float actualProgress = (1/contentSize)*progressValue;
		        progressBar.setProgress(progressValue);
		        
				progressBar.setMax(contentSize);

				
	        }
	        			
			
        	downloadbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
			        progressBar.setVisibility(View.VISIBLE);
			        pauseButton.setVisibility(View.VISIBLE);
			        downloadbutton.setVisibility(View.INVISIBLE);
			        resumeDwnBtn.setVisibility(View.INVISIBLE);
			        
			        System.out.println("Content Index put for download :"+index);
			        
			        
			        EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).setIsDownloading(true);
			    	//Fresh Download
			        mDataManager.downloadData(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index),index,0);//calls downloadData with index

			     
				}
			});
        	
        	pauseButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
			        pauseButton.setVisibility(View.INVISIBLE);
			        downloadbutton.setVisibility(View.INVISIBLE);
			        resumeDwnBtn.setVisibility(View.VISIBLE);
			        progressBar.setVisibility(View.VISIBLE);
			        
					Toast.makeText(mContext, "Cancel download", Toast.LENGTH_LONG).show();
					
					EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).setIsDownloading(false);
					// Cancel all current downloads for this content
					for(int i = 0;i < EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getmDwnContentArr().size(); i++)
					{
						EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getmDwnContentArr().get(i).cancel(true);
					}
					
					// Now insert whatever has been downloaded
					
					
				}
			});
        	
        	
        	resumeDwnBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pauseButton.setVisibility(View.VISIBLE);
			        downloadbutton.setVisibility(View.INVISIBLE);
			        resumeDwnBtn.setVisibility(View.INVISIBLE);
					progressBar.setVisibility(View.VISIBLE);
					
					EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).setIsDownloading(true);
					
			        mDataManager.downloadData(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index),index,1);//calls downloadData with index
				}
			});
 
        	
        	
        	TextView contentLbl = (TextView) layout.findViewById(R.id.grid_item_label);
        	
        	System.out.println(""+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getmName());
        	contentLbl.setText(""+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getmName());
        	
        	imgView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					if(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getDownloadStatus() == 1)
					{
						// Sending image id to FullScreenActivity
						Intent i = new Intent(ContentManagerActivity.this, ParentGridActivity.class);

						i.putExtra("brandindex", 0);
						i.putExtra("contentindex", index);
						
						startActivity(i);

					}
					else
					{
						Toast.makeText(mContext, "Please download first!", Toast.LENGTH_LONG).show();
					}
					
				}
			});
        	
        	return layout;
			
		}

	}

 /*
	@Override
	public void allDownloadComplete() {

		EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).setDownloadStatus(1);
		
	}
*/

	@Override
	public void parsingComplete() {
		mAdapter.notifyDataSetChanged();
	}


	@Override
	public void downloadFinished() {
		mAdapter.notifyDataSetChanged();
	}


	@Override
	public void updateDownloadProgress(int contentId, long totalread) {
		
		
		//System.out.println("Total Read :"+totalread);
		//EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentId).setProgressValue(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentId).getProgressValue()+(int)totalread);
		
		
		//mAdapter.notifyDataSetChanged();
	}


	@Override
	public void allDownloadComplete(int contentIndex) 
	{
		// TODO Auto-generated method stub
		//EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadStatus(1);
		EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setIsDownloading(false);
		mAdapter.notifyDataSetChanged();
	}

}