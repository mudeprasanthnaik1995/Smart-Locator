package com.org.ui.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;

import com.org.ui.driverScript.Driver;
import com.org.ui.util.Context;
import com.org.ui.util.Finder;

public class Locator {
	static Context context=new Context();
	public static void main(String[] args) {
		
		Driver.initializeWebDriver("https://unacademy.com/lesson/why-read-word-power-made-easy-and-its-importance/PDFX4IZ5");
		/*Finder reuable=new Finder();
		
		  List<String> Texts=reuable.getVisibleTextInList(By.xpath("//Body[1]//*[text()]"));
		  System.out.println(Texts);
		 */
		List<String> Tags=Finder.getCurrentPageTags();
		System.out.println(Tags);
		Tags.remove("//br");
		Tags.remove("//script");
		Tags.remove("//html");
		Tags.remove("//meta");
		Tags.remove("//head");
		Tags.remove("//body");
		Tags.remove("//div");
		Tags.remove("//tr");
		Tags.remove("//img");
		context.setListOfTags(Tags);
		ArrayList<ArrayList<HashMap<String,String>>> listoflistofAttributesOfallTag=new ArrayList<ArrayList<HashMap<String,String>>>();;
		int count=0;
		for(String tag:context.getListOfTags()) {
			ArrayList<HashMap<String,String>> listofAttributesOfallTag;
			listofAttributesOfallTag=Finder.identifyAttributesOfallTags(tag);
			listoflistofAttributesOfallTag.add(listofAttributesOfallTag);
			context.setListoflistofAttributesOfallTag(listoflistofAttributesOfallTag);
			count++;
		}
		System.out.println(count);
		System.out.println(context.getListoflistofAttributesOfallTag());
		for(ArrayList<HashMap<String,String>> sameTag:context.getListoflistofAttributesOfallTag()){
			for(HashMap<String,String> element:sameTag) {
				Finder.generateLocators(element);
			}
		}
		System.out.println(context.getFinalXpaths());
	}


}
