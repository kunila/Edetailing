package com.karbens.edetailing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.VideoView;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.MediaController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import com.karbens.application.EdetailingApplication;
import com.karbens.model.Brand;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;
import com.viewpagerindicator.LinePageIndicator;

@SuppressLint("ValidFragment")/**/
public class ParentActivity extends FragmentActivity{

	
	LinePageIndicator mIndicator = null;
	ViewPager  mPager = null;
	Button longPressExitBtn = null;
	Button scrollExitBtn = null;
	ParentAdapter mAdapter =null;
	LayoutInflater layoutInflater =null;
	Brand mBrand = null;
	Content mContent = null;
	Parent mParent = null;
	Child mChild = null;
	WebView mWebView = null;
    LinearLayout mLinearLay = null;
    LinearLayout exitLayout = null;
	DisplayMetrics dm = null;
	HorizontalScrollView hScroll = null;
	LinearLayout lin = null;
	LinearLayout slin = null;
	Spinner spin = null;
	int contentIndex = 0;
	int brandIndex = 0;
	
	ArrayList<Parent> mParentArr = new ArrayList<Parent>();
	ArrayList<Content> mContentArr = new ArrayList<Content>(); 
    ArrayList<Child> mChildArr = null;
    
    static final int SWIPE_MIN_DISTANCE = 120;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		Bundle b = getIntent().getExtras();
		if(b!=null)
		{

			brandIndex = b.getInt("brandindex");
			contentIndex = b.getInt("contentindex");
			
			mContent = EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex);
			
			for(int i = 0; i < mContent.getmParentArr().size(); i++)
			{
				if(mContent.getmParentArr().get(i).getIsDisabled() == false)
				{
					mParent = EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(i);
					mParentArr.add(mParent);
				}
			}
		}
		
		System.out.println("Content Name: "+mContent.getmName());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parent);
		
		dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
		
        //1-Image,2-Video,3-Text,4-Reference button
        
		
        slin = (LinearLayout) findViewById(R.id.scrolllayout);
        hScroll = (HorizontalScrollView) findViewById(R.id.scrollLayout);
        
        exitLayout = (LinearLayout) findViewById(R.id.exitlayout);
        longPressExitBtn = (Button) findViewById(R.id.btn);
        scrollExitBtn = (Button) findViewById(R.id.exitScrollBtn);

        lin = (LinearLayout) findViewById(R.id.lin);
       
        for (int i = 0; i <  mParentArr.size(); i++) 
        {
        	
        	final int index = i;
        	ImageView iv = new ImageView(this);
			LinearLayout.LayoutParams sparams = new LinearLayout.LayoutParams(70,70);
			
			/*String imagePath= mContent.getmParentArr().get(i).getmSlideBgPath();
			Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
	        Drawable drawableImage = new BitmapDrawable(bitmapImage);*/
			//iv.setBackgroundResource(((Parent) mContent.getmParentArr().get(i)).getmSlideBgPath());
	       // iv.setBackgroundDrawable(drawableImage);
	        
	    	File imgFile = new  File(mParentArr.get(index).getmSlideBgPath());
	    	if(imgFile.exists()){

	    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	    	    iv.setImageBitmap(myBitmap);

	    	}
	    	
	        
			iv.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) 
				{
					mPager.setCurrentItem(index);
					
					return false;
				}
			});
			
			
			sparams.setMargins(2,2,4,4);
			lin.addView(iv,sparams);
		}
        
       
      //slin.setTranslationY(-50f);
      //slin.setVisibility(View.GONE);
      
      
       DropDownAnim ddanim = new DropDownAnim(slin, 70, false);
	   ddanim.setDuration(0);
	   slin.startAnimation(ddanim);
        
        exitLayout.setVisibility(View.GONE);
      //mLinearLay.setTranslationY(-10f);
        
       
		mPager = (ViewPager) findViewById(R.id.parentView);
		mAdapter = new ParentAdapter(this);
		mPager.setAdapter(mAdapter);
		
		/*
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( dm );
        System.out.println("Height :"+dm.heightPixels);
        System.out.println("Width :"+dm.widthPixels);
        */

		//mIndicator.
	}

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
		
	}
*/
	
	private class ParentAdapter extends PagerAdapter implements View.OnTouchListener,OnGestureListener
	{
      
		 GestureDetector gd = null;
		 int childId = 0;
		 View childWebView = null;
		 Context mContext = null;
		 boolean translate=true;
	
		 
		 
		 public ParentAdapter(Context context)
		 {
			mContext = context;
			gd = new GestureDetector(this);
		 }
		 
		
		@Override
		public int getCount() {
			//System.out.println(""+mBrand.getmContentArr().get(0).getmParentArr().size());
			//return mBrand.getmContentArr().get(0).getmParentArr().size();
			return mContent.getmParentArr().size();
		
		}
		
		@Override
		public void destroyItem(View container, int position, Object view) {
			((ViewPager) container).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		 
		
       
		@Override
        public Object instantiateItem(View container, int position) {
       
			
        	layoutInflater = getLayoutInflater();
        	View layout = layoutInflater.inflate(R.layout.page_viewer_item,null);
        	
        	RelativeLayout mainContainerRelLay = (RelativeLayout) layout.findViewById(R.id.slideContainerRL);
        	
        	
        	RelativeLayout.LayoutParams vParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        	ImageView imgV = new ImageView(mContext);
        	
           // ImageView imgView = (ImageView) layout.findViewById(R.id.parentImgView);
            //imgV.setBackgroundResource((int)mBrand.getmContentArr().get(0).getmParentArr().get(position).getmSlideBgPath());
        	
        	//imgV.setBackgroundResource((int)mContent.getmParentArr().get(position).getmSlideBgPath());
        /*	String imagePath= mContent.getmParentArr().get(position).getmSlideBgPath();
			Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
	        Drawable drawableImage = new BitmapDrawable(bitmapImage);*/

			//iv.setBackgroundResource(((Parent) mContent.getmParentArr().get(i)).getmSlideBgPath());
	       // imgV.setBackgroundDrawable(drawableImage);
        	
        	
        	File imgFile = new  File(mParentArr.get(position).getmSlideBgPath());
        	if(imgFile.exists()){

        	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        	    imgV.setImageBitmap(myBitmap);

        	}
	        
            mainContainerRelLay.addView(imgV,vParams);
            
            imgV.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					
					return gd.onTouchEvent(event);
					//return false;
				}
			});
            
            imgV.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					//finish();
					System.out.println("Display Exit!!");
					exitLayout.setVisibility(View.VISIBLE);
					//****** Construct data model and switch to target application *******//
					
					return true;
				}
            });
            
            System.out.println("Index :"+position+"Child Count :"+mParentArr.get(position).getmChildArr().size());
            
            for(int i=0;i<(mParentArr.get(position).getmChildArr().size());i++)
            {
            	
            	final Child aChild = mParentArr.get(position).getmChildArr().get(i);
            	
            	int tempChilId = Integer.parseInt(""+position+""+i); // not used
           
            	
            	String childFrame = aChild.getmFrame();
            	
            	String[] frameArr = childFrame.split(",");
            	
            /*	int xValue = Integer.parseInt(frameArr [0]);
            	int yValue = Integer.parseInt(frameArr [1]);
            	int width = Integer.parseInt(frameArr [2]);
            	int height = Integer.parseInt(frameArr [3]);*/
            	
            	float xVal= Float.parseFloat(frameArr [0]);
            	int xValue = (int) Math.round(xVal);
            	float yVal= Float.parseFloat(frameArr [1]);
            	int yValue = (int) Math.round(yVal);
            	float widthFrame= Float.parseFloat(frameArr [2]);
            	int width = (int) Math.round(widthFrame);
            	float heightFrame= Float.parseFloat(frameArr [3]);
            	int height = (int) Math.round(heightFrame);
            	
            	
                //mWebView.getSettings().setSupportZoom(true);
                //System.out.println("Child Type testing :"+aChild.getmType());
                
                RelativeLayout.LayoutParams bparams = new RelativeLayout.LayoutParams(width,height);//RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);//
            	bparams.setMargins(xValue, yValue, 0, 0);
            		
                bparams.addRule(RelativeLayout.ALIGN_TOP, RelativeLayout.TRUE);//imgView.getId());
                
                
                //1-Image,2-Video,3-Text,4-Reference button
              //1-text,2-image,3-video,4-Reference button----new 
                if(aChild.getmType()==3)
                {
                	System.out.println("Video inside");
                	//mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                	//mWebView.loadDataWithBaseURL("file:///android_asset/", "<html><body><h1>Video</h1><video width=\"320\" height=\"240\" controls autobuffer><source src=\"file:///mnt/sdcard/mylan_add.mp4\" type=\"video/mp4\"></video></body></html>", "text/html", "utf-8", null);
                	//mWebView.loadDataWithBaseURL("file:///sdcard/", "<html><body><video width=\""+width+"px\" height=\""+height+"px\" controls><source src=\"mylan_add.mp4\" type=\"video/mp4\"></video></body></html>", "text/html", "utf-8", null);
                	//mWebView.loadDataWithBaseURL("file:///sdcard/", "<html><body><video width=\"100%\"  height=\"100%\"><source src=\"mylan_add.mp4\" type=\"video/mp4\"></video></body></html>", "text/html", "utf-8", null);
                	
                	final VideoView video = new VideoView(mContext);
                	//MediaController mMedia = new MediaController(mContext);
                	//mMedia.setAnchorView(video);
                	//video.setMediaController(mMedia);
                	Uri vidUri = Uri.parse("android.resource://"+"com.karbens.myday"+"/"+R.raw.mylan_add);
                	//Uri vidUri = Uri.parse("android.resource://"+"mylan_add.mp4");
                	//video.setVideoPath("android.resource://com.karbens/raw/mylan_add");
                	video.setVideoURI(vidUri);
                	//video.setVisibility(View.VISIBLE);
                	
                    video.start();
                	video.setZOrderOnTop(true);
                	
                	video.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							
							if(event.getAction() == MotionEvent.ACTION_DOWN)
							{
    							VideoDialogFragment videoViewDialog = new VideoDialogFragment(aChild.getmFilePath(),aChild.getmType(),mContext);
    						    videoViewDialog.show(getSupportFragmentManager(), "video dialog");
    						}
    						
    						return false;
							
						}
					});
                	
                	mainContainerRelLay.addView(video, bparams);
                	
                }
                else if(aChild.getmType()==4)
                {
                	
                	
                	// Reference Button
                	
                	Button  btnref = new Button(mContext);     
                	btnref.setBackgroundResource(R.drawable.ref_button);
                	
                	btnref.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View view) {
							
							// Should take this list of references from the child
								List list = new ArrayList();
								list.add("Reference 1");
								list.add("Reference 2");
								list.add("Reference 3");
								list.add("Reference 4");
							
							
    							spinnerDialogFragment spinnerViewDialog = new spinnerDialogFragment(list);
    							spinnerViewDialog.show(getSupportFragmentManager(), "spinner dialog");
    						
    					}
						
					});

                	
                	/*Spinner spin = new Spinner(mContext);
                	
                	List list = new ArrayList();
        			list.add("Reference 1");
        			list.add("Reference 2");
        			list.add("Reference 3");
        			list.add("Reference 4");
        			
        			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list);
        			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        			spin.setPrompt("");
        			spin.setAdapter(dataAdapter);
        			
        			spin.setOnItemSelectedListener(new customOnItemSelectedListener());
        			
        			spin.setBackgroundResource(R.drawable.ref_button);
        			
        			
        			//spin.setLayoutParams(new LayoutParams(width, height));
        			//spin.setDropDownWidth(width);
        			*/
        			
        			mainContainerRelLay.addView(btnref, bparams);
        			
        				
                }
                else 
                {
                	
                	mWebView = new WebView(mContext);
                	mWebView.setId(Integer.parseInt(""+position+""+i));
                    //mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
                    //mWebView.setScrollbarFadingEnabled(true);
                    mWebView.setWebChromeClient(new WebChromeClient());
       
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    //mWebView.getSettings().setDomStorageEnabled(true); 
                    //mWebView.getSettings().setPluginState(PluginState.ON);
                    //mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    mWebView.getSettings().setLoadWithOverviewMode(true);
                    mWebView.getSettings().setUseWideViewPort(true);
                    //mWebView.getSettings().setBuiltInZoomControls(true);
                    mWebView.getSettings().setAllowFileAccess(true);
                    mWebView.setInitialScale(1);
                   
                    //mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                   
                    mWebView.setOnTouchListener(new OnTouchListener() {
    					
    					@Override
    					public boolean onTouch(View v, MotionEvent event) {
    						
    						if(event.getAction() == MotionEvent.ACTION_DOWN)
    						{
    							WebViewDialogFragment webViewDialog = new WebViewDialogFragment(aChild.getmFilePath(),aChild.getmType());
    							webViewDialog.show(getSupportFragmentManager(), "web dialog");
    						}
    						
    						//return gd.onTouchEvent(event);
    						return false;
    					}
    				});
                    
                	
                	// Image or Text
                	mWebView.loadDataWithBaseURL("file:///android_res/drawable/", "<html><body><img src=\""+aChild.getmFilePath()+"\" width=100%\"/></body></html>", "text/html", "utf-8", null);
                	mainContainerRelLay.addView(mWebView, bparams);
                }
               
            }
            
            ((ViewPager) container).addView(layout);
             
        	return layout;
       
        }
		
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			
			//return false;
			return gd.onTouchEvent(event);
		}
		
		
		@Override
		public boolean onDown(MotionEvent arg0) {
			return false;
		}


		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
				float arg3) {
			System.out.println("On Fling");
			
			 if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) 
			 {
				 //slin.setVisibility(View.GONE);
			     //Toast.makeText(mContext, "bottomToTop",Toast.LENGTH_SHORT).show();
				 DropDownAnim ddanim = new DropDownAnim(slin, 70, false);
				 ddanim.setDuration(500);
				 slin.startAnimation(ddanim);
			 } 
			 else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE)
			 {
				 //slin.setVisibility(View.VISIBLE);
			     //Toast.makeText(mContext,"topToBottom  ", Toast.LENGTH_SHORT) .show();
				 DropDownAnim ddanim = new DropDownAnim(slin, 70, true);
				 ddanim.setDuration(500);
				 slin.startAnimation(ddanim);
			 }
			
			return false;
		}


		@Override
		public void onLongPress(MotionEvent arg0) {
			System.out.println("Long Press");
		}


		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			
			return false;
		}


		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			return false;
		}

		/*
		private void EnlargeChild(View view) {
	        // create set of animations
			AnimationSet replaceAnimation = new AnimationSet(false);
	        // animations should be applied on the finish line
	        replaceAnimation.setFillAfter(true);

	        TranslateAnimation trans = null;
	        ScaleAnimation scale = null;
	       
	        
	        int statusBarOffset = dm.heightPixels - mLinearLay.getMeasuredHeight();

	        int originalPos[] = new int[2];
	        view.getLocationOnScreen(originalPos);

	        int xDest = dm.widthPixels/2;
	        
	        xDest -= (view.getMeasuredWidth()*2/2);
	        
	        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2) - statusBarOffset;
	        
	        
	        trans = getTranslateAnim(0, xDest - originalPos[0] , 0, yDest - originalPos[1]);
	        scale = getScaleUpAnim();
	        
	        // add new animations to the set
	        replaceAnimation.addAnimation(scale);
	        replaceAnimation.addAnimation(trans);

	        // start our animation
	        view.startAnimation(replaceAnimation);
	    }
		
		
		private void ShrinkChild(View view) {
			
			// Shrink it back to original size
		}
		
		
		private TranslateAnimation getTranslateAnim(int fromX,int toX,int fromY,int toY)
		{
			// create translation animation
	        TranslateAnimation trans = new TranslateAnimation(fromX, toX , fromY, toY);
	        trans.setDuration(1000);
	        
	        return trans;
		}
		
		private ScaleAnimation getScaleUpAnim()
		{
			 // create scale animation
	        ScaleAnimation scale = new ScaleAnimation(1.0f,2.0f,1.0f,2.0f);//,
					//Animation.ABSOLUTE,0.5f,Animation.ABSOLUTE,0.5f);//(1.0f, xScale, 1.0f, yScale);
	        scale.setDuration(1000);
			
	        return scale;
		}
		
		
		private ScaleAnimation getScaleDwnAnim()
		{
			 // create scale animation
	        ScaleAnimation scale = new ScaleAnimation(1.0f,2.0f,1.0f,2.0f);//,
					//Animation.ABSOLUTE,0.5f,Animation.ABSOLUTE,0.5f);//(1.0f, xScale, 1.0f, yScale);
	        scale.setDuration(1000);
			
	        return scale;
		}	
		*/
		
		public class customOnItemSelectedListener implements OnItemSelectedListener {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Intent intent = new Intent(ParentActivity.this,WebViewActivity.class);
				startActivity(intent);
				//Toast.makeText(mContext, parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}
		
		
		
		//@SuppressLint("ValidFragment")/**/
		public class spinnerDialogFragment extends DialogFragment 
		{
			List mlist = null;
			
			public spinnerDialogFragment (List list)
			{
				mlist=list;
			}
			
			
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				
				// Get the layout inflater
			    LayoutInflater inflater = getActivity().getLayoutInflater();
			    
			    View dialogView = inflater.inflate(R.layout.spinner_dialog, null);
			    builder.setTitle("Select References:");
				builder.setView(dialogView);
				CharSequence[] cs = (CharSequence[]) mlist.toArray(new CharSequence[mlist.size()]);
				
				builder.setSingleChoiceItems(cs, -1,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                        dialog.dismiss();
		                        //Toast.makeText(mContext, "Selected Index"+" "+which, Toast.LENGTH_LONG).show();
		                        
		                        Intent intent = new Intent(ParentActivity.this,WebViewActivity.class);
		        				startActivity(intent);
		                    }
		                }).setPositiveButton(
		                "Cancel",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog,
		                            int whichButton) {
		                        dialog.dismiss();
		                    }
		                });
				
				/*
				Spinner spin = (Spinner)dialogView.findViewById(R.id.refSpinner);
            	
    			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, mlist);
    			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    			//spin.setPrompt("");
    			spin.setAdapter(dataAdapter);
    			
    			spin.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
    			
    			spin.performClick();
    			*/
				AlertDialog dialog  = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				
				return dialog;
			}
			
		}
		
	
		public class VideoDialogFragment extends DialogFragment implements OnDoubleTapListener,OnGestureListener 
		{

			VideoView mVideo = null;
			String mPath = "";
			int mType = 1;
			Context mContext = null;
			GestureDetector gd = null;
			
			
			public VideoDialogFragment (String path, int type,Context context)
			{
				mPath = path;
				mType = type;
				mContext = context;
				gd = new GestureDetector(this);
			}
			
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				
				// Get the layout inflater
			    LayoutInflater inflater = getActivity().getLayoutInflater();
			    
			    View dialogView = inflater.inflate(R.layout.video_dialog, null);
			    
				builder.setView(dialogView);
				
				builder.setCancelable(false);
				
				final VideoView video = (VideoView) dialogView.findViewById(R.id.childVideoView);
				
            	//MediaController mMedia = new MediaController(mContext);
            	//mMedia.setAnchorView(video);
            	//video.setMediaController(mMedia);
            	Uri vidUri = Uri.parse("android.resource://"+"com.karbens.myday"+"/"+R.raw.mylan_add);
            	
            	video.setVideoURI(vidUri);
            	
            	video.start();
            	video.setZOrderOnTop(true);
            	
            	
            	video.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						dismiss();
						return false;
					}
				});
            	
				
				AlertDialog dialog  = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				
				return dialog;
			}

			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onDoubleTap(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
				System.out.println("Double Tapped");
				return true;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Single Tapped");
				return true;
			}
				
		}
		

		//@SuppressLint("ValidFragment")/**/
		public class WebViewDialogFragment extends DialogFragment implements OnDoubleTapListener, OnGestureListener 
		{
			
			WebView mWebView = null;
			String mPath = "";
			int mType = 0;
			Context mContext = null;
			GestureDetector gd = null;
			
			public WebViewDialogFragment (String path, int type)
			{
				mPath = path;
				mType = type;
				//mContext = context;
				gd = new GestureDetector(this);
			}
			
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				
				// Get the layout inflater
			    LayoutInflater inflater = getActivity().getLayoutInflater();
			    
			    View dialogView = inflater.inflate(R.layout.webview_dialog, null);
			    
				builder.setView(dialogView);
				
				mWebView = (WebView) dialogView.findViewById(R.id.childWebView);
				
				mWebView.loadDataWithBaseURL("file:///android_res/drawable/", "<html><body><img src=\""+mPath+"\" width=100%\"/></body></html>", "text/html", "utf-8", null);
				
				//mWebView.loadDataWithBaseURL("file:///android_res/drawable/", "<html><body><img src=\""+mPath+"\" width=100%\"/></body></html>", "text/html", "utf-8", null);
				
				builder.setCancelable(false);
				
				
				
				mWebView.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						
						return gd.onTouchEvent(event);
					}
				});
				
				AlertDialog dialog  = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				
				return dialog;
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// dismiss dialog
				System.out.println("Dismiss WebView dialog");
				this.dismiss();
				return false;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				// TODO Auto-generated method stub
				
				return false;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

		}


	}

	
	public class DropDownAnim extends Animation {
	    private final int targetHeight;
	    private final View view;
	    private final boolean down;

	    public DropDownAnim(View view, int targetHeight, boolean down) {
	        this.view = view;
	        this.targetHeight = targetHeight;
	        this.down = down;
	        //this.view.setVisibility(View.VISIBLE);
	    }

	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        int newHeight;
	        
	        System.out.println("Animation Called");
	        if (down) 
	        {
	            newHeight = (int) (targetHeight * interpolatedTime);
	        }
	        else
	        {
	            newHeight = (int) (targetHeight * (1 - interpolatedTime));
	        }
	        
	        if (newHeight == 0)
	        {
	        	newHeight = 1;
	        }
	        
	        view.getLayoutParams().height = newHeight;
	        view.requestLayout();
	    }

	    @Override
	    public void initialize(int width, int height, int parentWidth,
	            int parentHeight) {
	        super.initialize(width, height, parentWidth, parentHeight);
	    }

	    @Override
	    public boolean willChangeBounds() {
	        return true;
	    }
	}
	
	
	/*private void copyAssets() 
	{
	    AssetManager assetManager = getAssets();
	    String[] files = null;
	    try 
	    {
	        files = assetManager.list("");
	    } 
	    catch (IOException e) 
	    {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    
	    for(String filename : files) 
	    {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          File outFile = new File(Environment.getExternalStorageDirectory(), filename);
	          System.out.println(""+Environment.getExternalStorageDirectory()+"/"+filename);
	          
	          out = new FileOutputStream(outFile);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } 
	        catch(IOException e) 
	        {
	            Log.e("tag", "Failed to copy asset file: " + filename, e);
	        }       
	    }
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}*/
	
}