package com.karbens.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.karbens.listeners.DownloadProgressListener;

import android.os.Environment;
import android.util.Log;

public class FileUtil {


	private static final String TAG = FileUtil.class.getSimpleName();
	private static String CACHE_PATH = "/sdcard/";
	private static final int DATA_PACKET_SIZE = 1024;
	static String completePath="";
	//static DownloadProgressListener mDownloadProgressListener = null;
	static File sdCard = Environment.getExternalStorageDirectory();
	
	
	//returns path url hanged long to string
	public static String writeInputStreamToFile(int contentIndex,String fileName,
			InputStream responseStream, String folderName) throws IOException {
		
		long totalRead = 0;
		int notificationReads = 0;
		
		CACHE_PATH= sdCard.getAbsolutePath()+"/edetailing/"+folderName+"/";
	
		//completePath = CACHE_PATH +folderName +"/" + fileName;
		File fileDirectory = new File(CACHE_PATH);
		
		fileDirectory.mkdirs();
			
		System.out.println("File path to be created :"+CACHE_PATH+ fileName);
		File f = new File(fileDirectory,fileName);
		
		if(f.exists()){
			f.delete();
		}
		
		f.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(CACHE_PATH+ fileName,"rw");
		
		if (raf != null) {
			long length = raf.length();
			raf.seek(length);
			totalRead = length;
		}
		
		try {
			int read = 0;
			
			int available = responseStream.available();
			
			int packetSize = 0;
			
			if(available > (DATA_PACKET_SIZE * 10)){
				packetSize = available;
			}
			
			//This loop will exit if data has been downloaded completely or the network has got lost 
			while (read != -1) 
			{
				byte buffer[] = new byte[packetSize];
				
				read = responseStream.read(buffer);
				
				if(read != -1)
				{
					
					//fos.write(buffer, 0, read);
					raf.write(buffer, 0, read);
					
					totalRead += read;
					notificationReads += read;
			        
					/*
					if(mDownloadProgressListener != null )//&& notificationReads > DownloadConstants.PROGRESS_NOTIFICATION_THRESHOLD)
					{
						
						mDownloadProgressListener.updateDownloadProgress(contentIndex, totalRead);
						notificationReads = 0;
					}
					*/
					
					Thread.sleep(5);//To make UI responsive. Shud be taken care later.
					
					//Put a call back here to calling entity for progress bar
					
					available = responseStream.available();
					
					if(available > packetSize)
					{
						packetSize = available;
					}
					else
					{
						packetSize = DATA_PACKET_SIZE * 10;
					}
				
				}
			}

			if (raf != null) 
			{
				raf.close();
			}        
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+"");
		}
		
		return CACHE_PATH+ fileName;
	}
	
	
	
}
