package com.karbens.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.karbens.application.EdetailingApplication;
import com.karbens.listeners.ContentDownloadListener;
import com.karbens.model.Brand;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;
import com.karbens.parser.ContentParser;

public class DataManager {

	GetLatestContents getLatestContents = null;
	Context mContext= null;
	static Parent aParent = null;
	static Child aChild = null;
	boolean isConnected = false;
	Database db;
	// downloadContent dwnContent  = null;
	//DownloadParent dwnParent = null;
	//DownloadChild dwnChild = null;
	DownloadContent dwnContent = null;
	//String localDwnPth="";
	//int contentIndex=0;
	ContentDownloadListener mListener = null;
	//int counterDownloads =0;
	int parentSize =0;
	
	public DataManager(ContentDownloadListener listener)
	{
		mListener = listener;
		mContext = (Context) listener;
		isConnected = hasConnectivity();
		
		if(isConnected)
		{
			//check for updates available
			getLatestContents = new GetLatestContents();
			getLatestContents.execute(""+Constants.XML_URL+"?username=strides&password=strides123&date=&cid=");
		}
		else
		{
			//show content from sqlite
			db = new Database(mContext);
		/*	List<Brand> brandData = db.getAllBrands();
			List<Content> contentData = db.getAllContents();
			List<Parent> parentData = db.getAllParents();
			List<Child>childData = db.getAllChild();*/
			Toast.makeText(mContext, "No Internet Connectivity", Toast.LENGTH_SHORT).show();
		}
		
	}
	

	 public boolean hasConnectivity() 
	 {
	          
	     ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

	     if (connectivityManager==null) 
	     {

	        return false;
	     }

	     NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

	     if (networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected()) 
	     {
	        return true;
	     }
	     else
	     {
	        return false;
	     }
	  }


	public class GetLatestContents extends AsyncTask<String, Void, ArrayList<Brand>>
	{

		
		@Override
		protected ArrayList<Brand> doInBackground(String... url) {
			String xml = null;

			try {
				// defaultHttpClient
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url[0]);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				xml = EntityUtils.toString(httpEntity);

				ContentParser parser = new ContentParser();
				EdetailingApplication.mBrandArr =  parser.getBranList(xml);
				
			
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.out.println(""+e.toString());
			
			}
			//return xml;
		
			//System.out.println("Strin of xml :"+xml);
			return EdetailingApplication.mBrandArr;
		}
		
		
		@Override
		protected void onPostExecute(ArrayList<Brand> result) {
				
			mListener.parsingComplete();
			
			super.onPostExecute(result);
		}
		 
		
	}


	public int downloadData(Content content,int contIndex,int operationType)  
	{
		
		//int totCount=0;
		//contentIndex = contIndex;
		
		ArrayList<DownloadContent> dwnCntArr = new ArrayList<DownloadContent> ();
		
		
		for (int l = 0; l < content.getmParentArr().size(); l++) 
		{
			final int parentIndex = l; // parent index
			aParent = content.getmParentArr().get(l);
			parentSize = content.getmParentArr().size();
			
			if(aParent.getmSlideBgPath() == "")
			{
				dwnContent = new DownloadContent(contIndex, parentIndex, 0, 0, content,operationType);
				String fileName = aParent.getmContentUrl().substring(aParent.getmContentUrl().lastIndexOf('/')+1, aParent.getmContentUrl().length());
				dwnContent.execute(aParent.getmContentUrl(),fileName,content.getmName()+"/"+aParent.getmName());
				dwnCntArr.add(dwnContent);
				
				// Get download size
				EdetailingApplication.mBrandArr
				.get(0)
				.getmContentArr()
				.get(contIndex)
				.setDownloadSize(
						EdetailingApplication.mBrandArr.get(0)
								.getmContentArr().get(contIndex)
								.getDownloadSize()
								+ 1);
			}
			
			
			
			for(int i=0;i<aParent.getmChildArr().size();i++)
			{
				
				final int childIndex = i; // Child Index
				aChild =  aParent.getmChildArr().get(i);
				parentSize= parentSize+aParent.getmChildArr().size();
				
				if(aChild.getmType()==2 || (aChild.getmType()==3)) //image or video or reference button
				{
					
					if(aChild.getmFilePath() == "")
					{
						dwnContent = new DownloadContent(contIndex, parentIndex, childIndex, 1, content,operationType);
						String childFileName = aChild.getmContentUrl().substring(aChild.getmContentUrl().lastIndexOf('/')+1, aChild.getmContentUrl().length());
						String childFolder=content.getmName()+"/"+aParent.getmName();
						//String childFileName = aParent.getmContentUrl().substring(aParent.getmContentUrl().lastIndexOf('/')+1, aParent.getmContentUrl().length());
						
						dwnContent.execute(aChild.getmContentUrl(),childFileName,childFolder);
						
						dwnCntArr.add(dwnContent);
						
						
						
						// Get download size
						EdetailingApplication.mBrandArr
						.get(0)
						.getmContentArr()
						.get(contIndex)
						.setDownloadSize(
								EdetailingApplication.mBrandArr.get(0)
										.getmContentArr().get(contIndex)
										.getDownloadSize()
										+ 1);
						
					}
					
					
					if(operationType == 0)
					{
						// Get download content size
						EdetailingApplication.mBrandArr
						.get(0)
						.getmContentArr()
						.get(contIndex)
						.setContentSize(
								EdetailingApplication.mBrandArr.get(0)
										.getmContentArr().get(contIndex)
										.getContentSize()
										+ 1);
					}
					
				}
				
			}
			
			if(operationType == 0)
			{
				// Get download content size
				EdetailingApplication.mBrandArr
				.get(0)
				.getmContentArr()
				.get(contIndex)
				.setContentSize(
						EdetailingApplication.mBrandArr.get(0)
								.getmContentArr().get(contIndex)
								.getContentSize()
								+ 1);
			}
			
		}
		
		
		EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contIndex).setmDwnContentArr(dwnCntArr);
		dwnCntArr = null;
		
		//totCount=counterDownloads;
		return 0;
		
		
	}
	
	public class DownloadContent extends AsyncTask<String, Void, String>
	{
		int pIndex = 0;
		int chIndex = 0;
		int mType = 0;
		Content aContent = null;
		int contentIndex = 0;
		String localDwnPth = "";
		int operationType = 0; // Fresh Download
		
		public DownloadContent(int cntIndex,int pPosition,int chPosition,int type,Content content,int opType) // index - position,type - Parent/Child
		{
			pIndex = pPosition; // Parent Index
			chIndex = chPosition; // Child Index
			mType = type;
			aContent = content;
			contentIndex = cntIndex;
			operationType = opType;
			
		}

		@Override
		protected String doInBackground(String... params) {

			String downloadUrl = params[0];
			String fileName = params[1];
			String folderName = params[2];
			System.out.println("URL :"+downloadUrl);
			System.out.println("FOLDER NAME :"+folderName);
			System.out.println("FILE NAME :"+fileName);
			
			localDwnPth = HttpUtil.download(contentIndex,downloadUrl, fileName, folderName,operationType);
			
			/*if(localDwnPth!=null)
			{
				counterDownloads=counterDownloads+1;
				System.out.println("counter: "+counterDownloads);
			}*/
			return localDwnPth;
		}
		
	    @Override
	    protected void onCancelled() {
	    	// TODO Auto-generated method stub
	    	EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadStatus(2);
	    	//insertContentToDB(aContent);
	    	System.out.println("Async Cancelled..");
	    	super.onCancelled();
	    }
	    
	    
		@Override
		protected void onPostExecute(String result) {
			
			//Update the data structure with the local file path
			
			if(mType==0)
			{
				//System.out.println("Path to update :"+result);
				
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).setmSlideBgPath(result);
				System.out.println("Updated to Data Structure :"+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).getmSlideBgPath());
			}
			else
			{
				//System.out.println("Path to update :"+result);
				
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).getmChildArr().get(chIndex).setmFilePath(result);
				System.out.println("Updated to Data Structure :"+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).getmChildArr().get(chIndex).getmFilePath());
				
					
			}
			
			EdetailingApplication.mBrandArr
			.get(0)
			.getmContentArr()
			.get(contentIndex)
			.setProgressValue(
					EdetailingApplication.mBrandArr.get(0)
							.getmContentArr().get(contentIndex)
							.getProgressValue()
							+ 1);
			
			
			
			if(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getDownloadSize() == EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getProgressValue())
			{

				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setLastDownloadDate(currentDateTimeString);
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadStatus(1);
				//EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setIsDownloading(false);
				
				insertContentToDB(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex),contentIndex);
			}
	
			mListener.downloadFinished();
			
			super.onPostExecute(result);
		}
	}
	
	/*
	public class DownloadParent extends AsyncTask<String, Void, String>
	{
		int parentIndex = 0;
		int mType = 0;
		Content aContent = null;
		int contentIndex = 0;
		String localDwnPth = "";
		
		public DownloadParent(int cntIndex,int pIndex,int type,Content content) // index - position,type - Parent/Child
		{
			parentIndex = pIndex; // Parent Index
			mType = type;
			aContent = content;
			contentIndex = cntIndex;
		}

		@Override
		protected String doInBackground(String... params) {

			String downloadUrl = params[0];
			String fileName = params[1];
			String folderName = params[2];
			
			//localDwnPth=HttpUtil.download(contentIndex,downloadUrl, fileName, folderName,operationType);
			
			//if(localDwnPth!=null)
			//{
				//counterDownloads=counterDownloads+1;
				//System.out.println("counter: "+counterDownloads);
			//}
			return localDwnPth;
		}
		
	 
		
		@Override
		protected void onPostExecute(String result) {
			
			//Update the data structure with the local file path
			//String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
			//EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setLastDownloadDate(currentDateTimeString);
			//EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadStatus(1);
			EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(parentIndex).setmSlideBgPath(result);
			System.out.println("updated "+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(parentIndex).getmSlideBgPath());
	
			mListener.downloadFinished();
			
			super.onPostExecute(result);
		}
		
	
		

		private void insertContentToDB(Content aContent2) {

			for (int i = 0; i < EdetailingApplication.mBrandArr.size(); i++) 
			{
				Brand aBrand = EdetailingApplication.mBrandArr.get(i);
				long aBrandId = aBrand.getmId();
				Brand bBrand =db.getBrand(aBrandId);
				if(bBrand==null)
				{
					
				long brandKey = db.createBrand(aBrand);
				
				for(int j = 0; j <EdetailingApplication.mBrandArr.get(i).getmContentArr().size(); j++)	
				{
					Content aContent = EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j);
					aContent.setbId(brandKey);
					long aContentId  =	aContent.getmId();
					Content bContent =	db.getContent(aContentId);
					if(bContent==null)
					{
						long contentKey =db.createContent(aContent);
					
						for(int k = 0; k <EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().size(); k++)	
						{
							
							Parent aParent = EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k);
							aParent.setcId(contentKey);
							long aParentId = aParent.getmId();
							//String aParentName = aParent.getmName();
							Parent bParent = db.getParent(aParentId);
							if(bParent==null)
							{
								long parentKey =db.createParent(aParent);
								for (int l = 0; l < EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmChildArr().size(); l++) 
								{
									
									Child aChild = EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmChildArr().get(l);
									aChild.setpID(parentKey);
									long aChildId = aChild.getmID();
									Child bChild = db.getChild(aChildId);
									if(bChild==null)
										db.createChild(aChild);
								}
						 	}
						  }
					  }
					
				}
				
				}
				
				
			}
			
		}
		
	}
	*/
	/*
	public class DownloadChild extends AsyncTask<String, Void, String>
	{
		final int childIndex;
		final int mParentIndex;
		Content aContent = null;
		int contentIndex = 0;
		String localDwnPth = "";
		
		public DownloadChild(int cntIndex,int index,int parentIndex,Content content) // index - position,type - Parent/Child
		{
			childIndex = index;
			mParentIndex = parentIndex;
			aContent = content;
			contentIndex = cntIndex;
		}

		@Override
		protected String doInBackground(String... params) {
		
			String downloadUrl = params[0];
			String fileName = params[1];
			String fldName = params[2];

			
			//localDwnPth=HttpUtil.download(contentIndex,downloadUrl, fileName, fldName);
			
			
			return localDwnPth;
			
		
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			// update the data structure with the local file path
			System.out.println("contentIndex :"+contentIndex+" mParentIndex: "+mParentIndex+ " position: "+ childIndex);
			
			EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(mParentIndex).getmChildArr().get(childIndex).setmFilePath(result);
						
			mListener.downloadFinished();
			//counterDownloads=counterDownloads+1;
			//aContent.setDownloadCount(mParentIndex);
			if(aContent.getmParentArr().get(aContent.getmParentArr().size()-1).getmChildArr().size() == childIndex)
			{
				//insertContentToDB(aContent);
			}
			
			super.onPostExecute(result);
		}
		
	
		
	}
*/
	
	private void insertContentToDB(Content aContent,int contentIndex) {
		
		db = new Database(mContext);
		
		
		for (int i = 0; i < EdetailingApplication.mBrandArr.size(); i++) 
		{
			Brand aBrand = EdetailingApplication.mBrandArr.get(i);
			
			
			long aBrandId = aBrand.getmId();
			Brand bBrand =db.getBrand(aBrandId);
			
			if(bBrand==null)
			{
				
				long brandKey = db.createBrand(aBrand);
				
				for(int j = 0; j <EdetailingApplication.mBrandArr.get(i).getmContentArr().size(); j++)	
				{
					aContent = EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j);
					aContent.setbId(brandKey);
				
					long aContentId  =	aContent.getmId();
					Content bContent =	db.getContent(aContentId);
					if(bContent==null)
					{
						long contentKey =db.createContent(aContent);
					
						for(int k = 0; k <EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().size(); k++)	
						{
							
							Parent aParent = EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k);
							aParent.setcId(contentKey);
							long aParentId = aParent.getmId();
							//String aParentName = aParent.getmName();
							Parent bParent = db.getParent(aParentId);
							if(bParent==null)
							{
								long parentKey =db.createParent(aParent);
								for (int l = 0; l < EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmChildArr().size(); l++) 
								{
									//contentCount
									//aContent.setContentCount(EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().size()+EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmChildArr().size());
									//System.out.println("count of content : "+aContent.getContentCount());
									
									Child aChild = EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmChildArr().get(l);
									aChild.setpID(parentKey);
									long aChildId = aChild.getmID();
									Child bChild = db.getChild(aChildId);
									if(bChild==null)
										db.createChild(aChild);
								}
						 	}
						  }
					  }
					
				}
			
			
		  }
			
			
		}
		//aContent.setProgressCount(counterDownloads);
		mListener.allDownloadComplete(contentIndex);
		
	}
	
}
