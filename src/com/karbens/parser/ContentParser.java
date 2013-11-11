package com.karbens.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.karbens.model.Brand;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;

public class ContentParser {

	private static final String TAG_Start = "xml";
	
	private static final String TAG_Brands = "brands";
	private static final String TAG_Brand = "brand";
	private static final String TAG_BrandID = "id";
	private static final String TAG_BrandName = "name";
	
	private static final String TAG_Contents = "contents";
	private static final String TAG_Content = "content";
	private static final String TAG_ContentId = "id";
	private static final String TAG_ContentName = "name";
	
	private static final String TAG_Parents = "parents";
	private static final String TAG_Parent = "parent";
	private static final String TAG_PId = "id";
	private static final String TAG_PName = "name";
	private static final String TAG_PUrl = "url";
	private static final String TAG_PFrame = "frame";
	private static final String TAG_PChildStatus = "chlsStatus";
	
	private static final String TAG_Childs = "childs";
	private static final String TAG_Child = "child";
	private static final String TAG_CId = "id";
	private static final String TAG_CName = "name";
	private static final String TAG_CUrl = "url";
	private static final String TAG_CFrame = "frame";
	private static final String TAG_CType = "type";
	private static final String TAG_CText = "text";
	private static final String TAG_CTextStyle = "textstyle";

	
	ArrayList<Brand> mBrandArr = null;
	ArrayList<Content> mContentArr = null;
	ArrayList<Parent> mParentArr = null;
	ArrayList<Child> mChildArr = null;
	
	public ArrayList<Brand> getBranList(String xml) throws IOException{
		
		InputStream is = new ByteArrayInputStream(xml.getBytes());
	//	InputSource is = new InputSource(new StringReader(xml));
		System.out.println("reached..."+is.toString());
		Brand aBrand = null;
		
		String startNode= "";
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			
			XmlPullParser parser = factory.newPullParser();
			
			parser.setInput(is, null);
			
			String tagName=null;
		
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				
				if(eventType == XmlPullParser.START_TAG)
				{
					
					if(TAG_Brands.equals(tagName))
					{
						mBrandArr = new ArrayList<Brand>();
					}
					if(TAG_Brand.equals(tagName))
					{
						aBrand = new Brand();
					}
					else if(TAG_BrandID.equals(tagName))
					{
						parser.next();
						aBrand.setmId(Integer.parseInt(parser.getText()));
						System.out.println("Brand ID***"+aBrand.getmId());
					}
					else if(TAG_BrandName.equals(tagName))
					{
						parser.next();
						aBrand.setmName(parser.getText());
					}
					else if(TAG_Contents.equals(tagName))
					{
						mContentArr = new ArrayList<Content>();
						parseContentInfo(parser,aBrand);
					}
				}
				else if(eventType == XmlPullParser.END_TAG)
				{
					String endTagName = parser.getName();
					
					if(TAG_Brands.equals(endTagName))
					{
						mBrandArr.add(aBrand);
						break;
					}
				}
				
				parser.next();
				eventType = parser.getEventType();
				tagName = parser.getName();
				
			}
			
		} 
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		}
		

		return mBrandArr;
		
		
		
		
	}
	

	private void parseContentInfo(XmlPullParser parser,Brand aBrand) {

		int eventType;
		String tagName=null;
		Content aContent=null;
		try {
			eventType = parser.getEventType();
		
			tagName = parser.getName();
		
			while (true) 
			{

				if(eventType == XmlPullParser.START_TAG)
				{
					
					if(TAG_Content.equals(tagName))
					{
						aContent = new Content();
					}
					else if(TAG_ContentId.equals(tagName))
					{
						parser.next();
						aContent.setmId(Integer.parseInt(parser.getText()));
					}
					else if(TAG_ContentName.equals(tagName))
					{
						parser.next();
						aContent.setmName(parser.getText());
						System.out.println("Content Id***"+aContent.getmId());

					}
					else if(TAG_Parents.equals(tagName))
					{
						//parser.next();
						//aContent.setmName(parser.getText());
						mParentArr = new ArrayList<Parent>();
						parseParentInfo(parser,aContent);
					}
					
				}
				else if(eventType == XmlPullParser.END_TAG)
				{
					String endTagName = parser.getName();
					
					if(TAG_Content.equals(endTagName))
					{
						mContentArr.add(aContent);
					}
					else if(TAG_Contents.equals(endTagName))
					{
						aBrand.setmContentArr(mContentArr);
						break;
					}
				}
				
				parser.next();
				eventType = parser.getEventType();
				tagName = parser.getName();
			
				
				}
			}
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	
}


	private void parseParentInfo(XmlPullParser parser,Content aContent) {


		int eventType;
		String tagName=null;
		Parent aParent=null;
		try {
			eventType = parser.getEventType();
		
			tagName = parser.getName();
		
			while (true) 
			{

				if(eventType == XmlPullParser.START_TAG)
				{
					
					if(TAG_Parent.equals(tagName))
					{
						aParent = new Parent();
					}
					else if(TAG_PId.equals(tagName))
					{
						parser.next();
						aParent.setmId(Integer.parseInt(parser.getText()));
					}
					else if(TAG_PName.equals(tagName))
					{
						parser.next();
						aParent.setmName(parser.getText());
					}
					else if(TAG_PUrl.equals(tagName))
					{
						parser.next();
						aParent.setmContentUrl(parser.getText());
					}
					else if(TAG_PFrame.equals(tagName))
					{
						parser.next();
						aParent.setmFrame(parser.getText());
					}
					else if(TAG_PChildStatus.equals(tagName))
					{
						parser.next();
						aParent.setmHas_child(Integer.parseInt(parser.getText()));
					
					}
					else if(TAG_Childs.equals(tagName))
					{	
						//if(aParent.getmHas_child()>0)
						//{
							//parser.next();
							//aContent.setmName(parser.getText());
							mChildArr = new ArrayList<Child>();
							parseChildInfo(parser,aParent);
						//}
						
					}
					
				}
				else if(eventType == XmlPullParser.END_TAG)
				{
					String endTagName = parser.getName();
					
					if(TAG_Parent.equals(endTagName))
					{
						mParentArr.add(aParent);
						
					}
					else if(TAG_Parents.equals(endTagName))
					{
						aContent.setmParentArr(mParentArr);
						break;
					}
					
				}
				
				parser.next();
				eventType = parser.getEventType();
				tagName = parser.getName();
			
				
				}
			}
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	

		
		
	}


	private void parseChildInfo(XmlPullParser parser,Parent aParent) {
		int eventType;
		String tagName=null;
		Child aChild=null;
		try {
			eventType = parser.getEventType();
		
			tagName = parser.getName();
		
			while (true) 
			{

				if(eventType == XmlPullParser.START_TAG)
				{
					
					if(TAG_Child.equals(tagName))
					{
						aChild = new Child();
					}
					else if(TAG_CId.equals(tagName))
					{
						parser.next();
						aChild.setmID(Integer.parseInt(parser.getText()));
					}
					else if(TAG_CName.equals(tagName))
					{
						parser.next();
						aChild.setmName(parser.getText());
					}
					else if(TAG_CUrl.equals(tagName))
					{
						parser.next();
						aChild.setmContentUrl(parser.getText());
					}
					else if(TAG_CFrame.equals(tagName))
					{
						parser.next();
						aChild.setmFrame(parser.getText());
					}
					else if(TAG_CType.equals(tagName))
					{
						parser.next();
						aChild.setmType(Integer.parseInt(parser.getText()));
					
					}
					else if(TAG_CText.equals(tagName))
					{
						parser.next();
						aChild.setmText(parser.getText());
					}
					else if(TAG_CTextStyle.equals(tagName))
					{	
						
							parser.next();
							aChild.setmTextStyle(parser.getText());
							//mChild = new ArrayList<Child>();
							//parseChildInfo(parser);
				
						
					}
					
				}
				else if(eventType == XmlPullParser.END_TAG)
				{
					String endTagName = parser.getName();
					
					if(TAG_Child.equals(endTagName))
					{
						mChildArr.add(aChild);
						
					}
					else if(TAG_Childs.equals(endTagName))
					{
						aParent.setmChildArr(mChildArr);
						break;
					}
				}
				
				parser.next();
				eventType = parser.getEventType();
				tagName = parser.getName();
			
				
				}
			}
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	
		
	}
	
}