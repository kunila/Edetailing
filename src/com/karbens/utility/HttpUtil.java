package com.karbens.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import com.karbens.application.EdetailingApplication;

import android.R.string;
import android.util.Log;

public class HttpUtil {

	private static final String TAG = HttpUtil.class.getSimpleName();
	private static String LOCAL_PATH = "";

	public static String download(int contentIndex, String url,
			String filename, String fldName, int opType) {

		boolean useProxy = false;

		String host = "107.108.85.10";
		int port = 80;
		InetSocketAddress addr = new InetSocketAddress(host, port);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
		HttpURLConnection connection = null;

		try {

			URL contentUrl = new URL(url);
			Log.d(TAG, "url = " + url);

			if (useProxy) {
				connection = (HttpURLConnection) contentUrl
						.openConnection(proxy);
			} else {
				connection = (HttpURLConnection) contentUrl.openConnection();
			}

			connection.connect();

			InputStream in = connection.getInputStream();
			String responseMessage = connection.getResponseMessage();
			Log.d(TAG, "res Mess = " + responseMessage);

			int responseCode = connection.getResponseCode();
			Log.d(TAG, "res Code = " + responseCode);

			int contentLength = connection.getContentLength();
			Log.d(TAG, "con Len = " + contentLength);

			/*
			if (opType == 0) //Fresh Download			
			{
				EdetailingApplication.mBrandArr
				.get(0)
				.getmContentArr()
				.get(contentIndex)
				.setContentSize(
						EdetailingApplication.mBrandArr.get(0)
								.getmContentArr().get(contentIndex)
								.getContentSize()
								+ contentLength);

			} 
			else if(opType == 1) // Resume
			{
				// DO Nothing
			}
			*/
			
			/*
			EdetailingApplication.mBrandArr
					.get(0)
					.getmContentArr()
					.get(contentIndex)
					.setDownloadSize(
							EdetailingApplication.mBrandArr.get(0)
									.getmContentArr().get(contentIndex)
									.getDownloadSize()
									+ contentLength);
			*/
	

			LOCAL_PATH = FileUtil.writeInputStreamToFile(contentIndex,
					filename, in, fldName);
			System.out.println("path for sdcard:" + LOCAL_PATH);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return LOCAL_PATH;

	}
}
