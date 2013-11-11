package com.karbens.utility;

import java.util.ArrayList;
import java.util.List;

import com.karbens.model.Brand;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "MyDay";

	// Table Names

	private static final String TABLE_PARENT = "Parent";
	private static final String TABLE_CONTENT = "Content";
	private static final String TABLE_CHILD = "Child";
	private static final String TABLE_BRAND = "Brand";
	
	// Common column names
	
	private static final String KEY_ID = "id"; // Primary Key

	// Parent table column names
	
	private static final String P_ID = "pid";
	private static final String P_NAME = "name";
	private static final String P_CONTENTURL = "contenturl";
	private static final String P_HASCHILD = "has_child";
	private static final String P_PARENTVIEWTIME = "parentviewtime";
	private static final String P_SLIDEBGPATH = "slidebgpath";
	private static final String P_TIMEINTERVAL = "timeinterval";
	private static final String P_FK = "content_id";

	// Content table column names

	private static final String C_NAME = "name";
	private static final String C_ID = "cid"; // Framework Key
	private static final String C_FK = "brand_id";
	private static final String C_DOWNLOADDATE = "down_date";
	private static final String C_DOWNLOADSTATUS = "down_status";


	// Child table column names
	
	private static final String CH_ID = "chid"; //Framework Key
	private static final String CH_NAME = "name";
	private static final String CH_CONTENTURL = "contenturl";
	private static final String CH_CHILDVIEWTIME = "childviewtime";
	private static final String CH_FILEPATH = "filepath";
	private static final String CH_FRAME = "frame";
	private static final String CH_ISANIMATED = "isanimated";
	private static final String CH_ANIMPATH = "animpath";
	private static final String CH_TIMEINTERVAL = "timeinterval";
	private static final String CH_TYPE = "type";
	private static final String CH_FK = "parent_id";

	// Brand table column names

	private static final String B_NAME = "name";
	private static final String B_ID = "bid";

	// CONTENT table create statement
	private static final String CREATE_TABLE_CONTENT = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_CONTENT
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ C_NAME
			+ " TEXT,"
			+ C_DOWNLOADDATE
			+ " TEXT,"
			+ C_ID
			+ " INTEGER,"
			+ C_DOWNLOADSTATUS
			+ " INTEGER,"
			+ C_FK
			+ " INTEGER,"
			+ "FOREIGN KEY("
			+ C_FK
			+ ") REFERENCES Brand(id) ON DELETE CASCADE" + ")";

	private static final String CREATE_TABLE_PARENT = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_PARENT
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ P_ID
			+ " INTEGER,"
			+ P_FK
			+ " INTEGER,"
			+ P_NAME
			+ " TEXT,"
			+ P_CONTENTURL
			+ " TEXT,"
			+ P_HASCHILD
			+ " INTEGER,"
			+ P_PARENTVIEWTIME
			+ " TEXT,"
			+ P_SLIDEBGPATH
			+ " INTEGER,"
			+ P_TIMEINTERVAL
			+ " INTEGER,"
			+ "FOREIGN KEY("
			+ P_FK
			+ ") REFERENCES Content(id) ON DELETE CASCADE" + ")";

	// Child table create statement
	private static final String CREATE_TABLE_CHILD = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_CHILD
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ CH_ID
			+ " INTEGER,"
			+ CH_FK
			+ " INTEGER,"
			+ CH_NAME
			+ " TEXT,"
			+ CH_CONTENTURL
			+ " TEXT,"
			+ CH_CHILDVIEWTIME
			+ " TEXT,"
			+ CH_FILEPATH
			+ " TEXT,"
			+ CH_FRAME
			+ " TEXT,"
			+ CH_TIMEINTERVAL
			+ " INTEGER,"
			+ CH_TYPE
			+ " INTEGER,"
			+ "FOREIGN KEY("
			+ CH_FK
			+ ") REFERENCES Parent(id) ON DELETE CASCADE" + ")";
	
	//+ CH_MISANIMATED
	//+ " BOOLEAN,"
	//+ CH_MANIMPATH
	//+ " INTEGER,"

	// Brand table create statement
	private static final String CREATE_TABLE_BRAND = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_BRAND
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ B_NAME
			+ " TEXT," 
			+ B_ID + " INTEGER" + ")";

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE_BRAND);
		db.execSQL(CREATE_TABLE_CONTENT);
		db.execSQL(CREATE_TABLE_PARENT);
		db.execSQL(CREATE_TABLE_CHILD);
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENT);
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT);
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILD);
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);
		onCreate(db);

	}

	/*
	 * This function will create a parent item in parent table
	 */
	public long createParent(Parent parent) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		
		values.put(P_ID, parent.getmId());
		values.put(P_NAME, parent.getmName());
		values.put(P_CONTENTURL, parent.getmContentUrl());
		values.put(P_HASCHILD, parent.getmHas_child());
		values.put(P_PARENTVIEWTIME, parent.getmParentViewTime());
		values.put(P_SLIDEBGPATH, parent.getmSlideBgPath());
		values.put(P_TIMEINTERVAL, parent.getmTimeInterval());
		values.put(P_FK, parent.getcId());
		// insert row
		// "row_ID" contains the value of row ID of row which has
		// been inserted
		long row_id = db.insert(TABLE_PARENT, null, values);
		db.close();
		Log.d("Database.class", "Parent class object values are added to DB");
		return row_id;

	}

	/*
	 * get single parent
	 */
	public Parent getParent(long Id) { 	//changed string to long 
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_PARENT + " WHERE "
				+ "pid" + " = " + Id ; // P_NAME;

		Cursor c = db.rawQuery(selectQuery, null);
		Parent p = null;
		if (c != null)
		{
			c.moveToFirst();

			
		}
		if(c.getCount() != 0)
		{
			p = new Parent();
			p.setmHas_child(c.getInt(c.getColumnIndex(P_ID)));
			p.setmName((c.getString(c.getColumnIndex(P_NAME))));
			p.setmContentUrl((c.getString(c.getColumnIndex(P_CONTENTURL))));
			p.setmHas_child(c.getInt(c.getColumnIndex(P_HASCHILD)));
			p.setmParentViewTime(c.getString(c.getColumnIndex(P_PARENTVIEWTIME)));
			p.setmSlideBgPath(c.getString(c.getColumnIndex(P_SLIDEBGPATH)));
			p.setmTimeInterval(c.getInt(c.getColumnIndex(P_TIMEINTERVAL)));
	
		}
		db.close();
		return p;
	}

	/*
	 * getting all parents
	 */
	public List<Parent> getAllParents() {
		List<Parent> parents = new ArrayList<Parent>();
		String selectQuery = "SELECT  * FROM " + TABLE_PARENT;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list

		if (c.moveToFirst()) {
			do {
				Parent p = new Parent();
				p.setmHas_child(c.getInt(c.getColumnIndex(P_ID)));
				p.setmName((c.getString(c.getColumnIndex(P_NAME))));
				p.setmContentUrl((c.getString(c.getColumnIndex(P_CONTENTURL))));
				p.setmHas_child(c.getInt(c.getColumnIndex(P_HASCHILD)));
				p.setmParentViewTime(c.getString(c
						.getColumnIndex(P_PARENTVIEWTIME)));
				p.setmSlideBgPath(c.getString(c.getColumnIndex(P_SLIDEBGPATH)));
				p.setmTimeInterval(c.getInt(c.getColumnIndex(P_TIMEINTERVAL)));

				// adding to Parent list
				parents.add(p);
			} while (c.moveToNext());
		}
		db.close();
		return parents;
	}

	/*
	 * Updating a Parent
	 */
	public int updateParent(Parent parent) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(P_ID, parent.getmId());
		values.put(P_NAME, parent.getmName());
		values.put(P_CONTENTURL, parent.getmContentUrl());
		values.put(P_HASCHILD, parent.getmHas_child());
		values.put(P_PARENTVIEWTIME, parent.getmParentViewTime());
		values.put(P_SLIDEBGPATH, parent.getmSlideBgPath());
		values.put(P_TIMEINTERVAL, parent.getmTimeInterval());

		// updating row
		db.close();
		return db.update(TABLE_PARENT, values, P_NAME + " = ?",
				new String[] { String.valueOf(parent.getmName()) });
	}

	/*
	 * Deleting a Parent
	 */
	public void deleteParent(Parent p) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PARENT, P_NAME + " = ?",
				new String[] { String.valueOf(p.getmName()) });
	}

	/* creating a content */

	public long createContent(Content content) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(C_NAME, content.getmName());
		values.put(C_ID, content.getmId());
		values.put(C_FK, content.getbId());
		values.put(C_DOWNLOADDATE, content.getLastDownloadDate());
		values.put(C_DOWNLOADSTATUS, content.getDownloadStatus());

		Log.d("name", content.getmName());

		// insert row
		// "inserting" contains the value of row ID of row which has
		// been inserted
		long inserting = db.insert(TABLE_CONTENT, null, values);
		db.close();
		return inserting;
	}

	/*
	 * get single content
	 */

	public Content getContent(long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_CONTENT + " WHERE "
				+ "cid" + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		Content content = null;
		
		if (c != null)
		{
			c.moveToFirst();
		}
		
		if(c.getCount() != 0)
		{
			content = new Content();
			content.setmName((c.getString(c.getColumnIndex(C_NAME))));
			content.setmId(c.getInt(c.getColumnIndex(C_ID)));
		}
		db.close();
		return content;
	}

	/*
	 * getting all Contents
	 */
	public List<Content> getAllContents() {
		List<Content> contents = new ArrayList<Content>();
		String selectQuery = "SELECT  * FROM " + TABLE_CONTENT;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Content content = new Content();
				content.setmName((c.getString(c.getColumnIndex(C_NAME))));
				content.setmId(c.getInt(c.getColumnIndex(C_ID)));

				contents.add(content);
			} while (c.moveToNext());
		}
		db.close();
		return contents;
	}

	/*
	 * Updating a Content
	 */
	public int updateContent(Content content) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(C_NAME, content.getmName());
		values.put(C_ID, content.getmId());

		// updating row
		
		return db.update(TABLE_CONTENT, values, C_ID + " = ?",
				new String[] { String.valueOf(content.getmId()) });
	}

	/*
	 * Deleting a Content
	 */
	public void deleteContent(Content content) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTENT, C_ID + " = ?",
				new String[] { String.valueOf(content.getmId()) });
	}

	public long createChild(Child child) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CH_ID, child.getmID());
		values.put(CH_NAME, child.getmName());
		values.put(CH_CONTENTURL, child.getmContentUrl());
		values.put(CH_CHILDVIEWTIME, child.getmChildViewTime());
		values.put(CH_FILEPATH, child.getmFilePath());
		values.put(CH_FRAME, child.getmFrame());
		//values.put(CH_MISANIMATED, child.getmIsAnimated());
		//values.put(CH_MANIMPATH, child.getmAnimPath());
		values.put(CH_TIMEINTERVAL, child.getmTimeInterval());
		values.put(CH_TYPE, child.getmType());
		values.put(CH_FK, child.getpID());
		// insert row
		// "inserting" contains the value of row ID of row which has
		// been inserted
		long inserted = db.insert(TABLE_CHILD, null, values);
		db.close();
		return inserted;

	}

	/*
	 * get single child
	 */
	public Child getChild(long ID) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_CHILD + " WHERE "
				+ "chid" + " = " + ID;

		Cursor c = db.rawQuery(selectQuery, null);
		Child ch = null;
		
		if (c != null)
		{	
			c.moveToFirst();

		
		}
		if(c.getCount() != 0)
		{
			ch = new Child();
			
			ch.setmID((c.getInt(c.getColumnIndex(CH_ID))));
			ch.setmName((c.getString(c.getColumnIndex(CH_NAME))));
			ch.setmContentUrl((c.getString(c.getColumnIndex(CH_CONTENTURL))));
			ch.setmChildViewTime(c.getString(c.getColumnIndex(CH_CHILDVIEWTIME)));
			ch.setmFilePath(c.getString(c.getColumnIndex(CH_FILEPATH)));
			//ch.setmIsAnimated(Boolean.parseBoolean(c.getString(c.getColumnIndex(CH_MISANIMATED))));
			//ch.setmAnimPath(c.getInt(c.getColumnIndex(CH_MANIMPATH)));
			ch.setmTimeInterval(c.getInt(c.getColumnIndex(CH_TIMEINTERVAL)));
			ch.setmType(c.getInt(c.getColumnIndex(CH_TYPE)));
		}
		db.close();
		return ch;
	}

	/*
	 * getting all Children
	 */
	public List<Child> getAllChild() {
		List<Child> children = new ArrayList<Child>();
		String selectQuery = "SELECT  * FROM " + TABLE_CHILD;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Child ch = new Child();
				ch.setmID(c.getInt(c.getColumnIndex(CH_ID)));
				ch.setmName((c.getString(c.getColumnIndex(CH_NAME))));
				ch.setmContentUrl((c.getString(c.getColumnIndex(CH_CONTENTURL))));
				ch.setmChildViewTime(c.getString(c
						.getColumnIndex(CH_CHILDVIEWTIME)));
				ch.setmFilePath(c.getString(c.getColumnIndex(CH_FILEPATH)));
				//ch.setmIsAnimated(Boolean.parseBoolean(c.getString(c.getColumnIndex(CH_MISANIMATED))));
				//ch.setmAnimPath(c.getInt(c.getColumnIndex(CH_MANIMPATH)));
				ch.setmTimeInterval(c.getInt(c.getColumnIndex(CH_TIMEINTERVAL)));
				ch.setmType(c.getInt(c.getColumnIndex(CH_TYPE)));

				// adding to todo list
				children.add(ch);
			} while (c.moveToNext());
		}
		db.close();
		return children;
	}

	/*
	 * Updating a child
	 */
	public int updateChild(Child child) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CH_ID, child.getmID());
		values.put(CH_NAME, child.getmName());
		values.put(CH_CONTENTURL, child.getmContentUrl());
		values.put(CH_CHILDVIEWTIME, child.getmChildViewTime());
		values.put(CH_FILEPATH, child.getmFilePath());
		values.put(CH_FRAME, child.getmFrame());
		//values.put(CH_MISANIMATED, child.getmIsAnimated());
		//values.put(CH_MANIMPATH, child.getmAnimPath());
		values.put(CH_TIMEINTERVAL, child.getmTimeInterval());
		values.put(CH_TYPE, child.getmType());
		//values.put(CH_FK, child.getpID());

		// updating row
		db.close();
		return db.update(TABLE_CHILD, values, CH_NAME + " = ?",
				new String[] { String.valueOf(child.getmName()) });

	}

	/*
	 * Deleting a Child
	 */
	public void deleteChild(Child child) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CHILD, CH_NAME + " = ?",
				new String[] { String.valueOf(child.getmName()) });
	}

	public long createBrand(Brand brand) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(B_NAME, brand.getmName());
		values.put(B_ID, brand.getmId());

		// insert row
		// "inserting" contains the value of row ID of row which has
		// been inserted
		long inserting = db.insert(TABLE_BRAND, null, values);
		db.close();
		return inserting;

	}

	/*
	 * get single brand
	 */
	public Brand getBrand(long brandId) {
		
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_BRAND + " WHERE "
				+ "bid" + " = " + brandId;

		Cursor c = db.rawQuery(selectQuery, null);
		Brand brand = null;
		if (c != null)
		{
			c.moveToFirst();

		}
		if(c.getCount() != 0)
		{
			brand = new Brand();
			brand.setmName((c.getString(c.getColumnIndex(B_NAME))));
			brand.setmId(c.getInt(c.getColumnIndex(B_ID)));
		}
		db.close();
		return brand;
	}

	/*
	 * getting all Brands
	 */
	public List<Brand> getAllBrands() {
		List<Brand> brands = new ArrayList<Brand>();
		String selectQuery = "SELECT  * FROM " + TABLE_BRAND;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Brand brand = new Brand();
				brand.setmName((c.getString(c.getColumnIndex(B_NAME))));
				brand.setmId(c.getInt(c.getColumnIndex(B_ID)));

				// adding to Brand list
				brands.add(brand);
			} while (c.moveToNext());
		}
		db.close();
		return brands;
	}

	/*
	 * Updating a Brand
	 */
	public int updateBrand(Brand brand) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(B_NAME, brand.getmName());
		values.put(B_ID, brand.getmId());

		// updating row
		return db.update(TABLE_BRAND, values, B_NAME + " = ?",
				new String[] { String.valueOf(brand.getmName()) });
	}

	/*
	 * Deleting a Brand
	 */
	public void deleteBrand(Brand brand) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BRAND, B_NAME + " = ?",
				new String[] { String.valueOf(brand.getmName()) });
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

}
