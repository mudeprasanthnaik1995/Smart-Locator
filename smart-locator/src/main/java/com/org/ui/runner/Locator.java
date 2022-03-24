package com.org.ui.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.org.ui.driverScript.Driver;
import com.org.ui.util.Context;
import com.org.ui.util.Finder;

public class Locator {
	static Context context=new Context();
	
		public static HashMap<String, String> getFinalXpathLocators(final WebDriver driver, final String tagsToIgnore) {
			List<String> Tags=Finder.getCurrentPageTags(driver,tagsToIgnore);
			System.out.println(Tags);
			context.setListOfTags(Tags);
			ArrayList<ArrayList<HashMap<String,String>>> listoflistofAttributesOfallTag=new ArrayList<ArrayList<HashMap<String,String>>>();
			for(String tag:context.getListOfTags()) {
				ArrayList<HashMap<String,String>> listofAttributesOfallTag;
				listofAttributesOfallTag=Finder.identifyAttributesOfallTags(driver,tag);
				listoflistofAttributesOfallTag.add(listofAttributesOfallTag);
				context.setListoflistofAttributesOfallTag(listoflistofAttributesOfallTag);
			}
			System.out.println(context.getListoflistofAttributesOfallTag());
			for(ArrayList<HashMap<String,String>> sameTag:context.getListoflistofAttributesOfallTag()){
				for(HashMap<String,String> element:sameTag) {
					Finder.generateLocators(driver,element);
				}
			}
			System.out.println(context.getFinalXpaths());
			Finder.getFinalXpathsMap(context.getFinalXpaths());
			System.out.println(context.getFinalXpathsMap());
			return context.getFinalXpathsMap();
		}
	


}
