package com.karbens.application;

import java.io.Serializable;
import java.util.ArrayList;
import com.karbens.model.Brand;
import com.karbens.model.Parent;
import com.karbens.utility.DataManager.DownloadContent;

import android.app.Application;

public class EdetailingApplication extends Application implements Serializable{

	public static ArrayList<Brand> mBrandArr = null;
	//public static ArrayList<DownloadContent> dwnContArr = null;
	public static ArrayList<Parent> mParentArr = null;
}
