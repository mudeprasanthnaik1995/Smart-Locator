package com.org.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.lang.model.util.Elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.org.ui.driverScript.Driver;

import io.netty.util.internal.StringUtil;


public class Finder {

	static Context context=new Context();
	static ArrayList<String> xpaths = new ArrayList<String>();

	public static List<String> getCurrentPageTags(final WebDriver driver, final String tagsToIgnore) {
		List<WebElement> NumbeofTagsDisplayed = driver.findElements(By.xpath("//*"));
		HashSet<String> finaltags = new HashSet<String>();
		for(WebElement element : NumbeofTagsDisplayed) {
			String tagName=element.getTagName().trim();
			if(!Arrays.asList(tagsToIgnore.split(",")).contains("//"+tagName)) {
				finaltags.add("//"+tagName);
			}	
		}
		return new ArrayList<String>(finaltags);
	}

	public static void generateLocators(final WebDriver driver, final HashMap<String,String> element) {
		try {
		boolean isIDPresent=element.containsKey("id");
		boolean isNamePresent=element.containsKey("name");
		if(isIDPresent) {
			getXpathByID(driver,element);
		}else if (isNamePresent) {
			getXpathByName(driver,element);
		}else{
			getTextORSystemXpath(driver,element);
		}
		context.setFinalXpaths(xpaths);
		}catch(Exception e) {
			
		}
	}

	private static void getXpathByID(final WebDriver driver,final HashMap<String,String> element) {
		StringBuffer idXpath=new StringBuffer();
		idXpath.append(element.get("tag"));
		element.remove("tag");
		idXpath.append("[@id='");
		idXpath.append(element.get("id"));
		idXpath.append("']");
		uniqueXpathMaker(driver, element, idXpath.toString());
	}


	private static void getXpathByName(final WebDriver driver,final HashMap<String,String> element) {
		StringBuffer nameXpath=new StringBuffer();
		nameXpath.append(element.get("tag"));
		element.remove("tag");
		nameXpath.append("[@name='");
		nameXpath.append(element.get("name"));
		nameXpath.append("']");
		System.out.println(nameXpath.toString());
		uniqueXpathMaker(driver, element, nameXpath.toString());
	}

	/*private static void getLinkXpaths(final HashMap<String,String> element) {
		StringBuffer linkXpath=new StringBuffer();
		linkXpath.append(element.get("tag"));
		element.remove("tag");
		linkXpath.append("[text()='");
		linkXpath.append(element.get("name"));
		linkXpath.append("']");
		System.out.println(linkXpath.toString());
		xpaths.add(linkXpath.toString());
	}*/

	private static void getTextORSystemXpath(final WebDriver driver,final HashMap<String,String> element) {
		StringBuffer sysXpath=new StringBuffer();
		sysXpath.append(element.get("tag"));
		element.remove("tag");
		for(Entry<String, String> ele:element.entrySet()) {
			sysXpath.append("[@"+ele.getKey()+"='");
			sysXpath.append(ele.getValue());
			sysXpath.append("']");
		}
		uniqueXpathMaker(driver, element, sysXpath.toString());
	}
	public static void uniqueXpathMaker(final WebDriver driver,final HashMap<String,String> element,final String sysXpath) {
		boolean isunique=isXpathUnique(driver,sysXpath);
		boolean isDisplayed=isElementDisplayed(driver,sysXpath);
		boolean areDimensionswell=areDimensionsGood(driver, sysXpath);
		if(isunique && isDisplayed) {
			checkForTextXpathOrAddExistingXpath(driver,element,sysXpath);
		}
		if(!isunique) {
			makeXpathUnique(driver,element,sysXpath);
		}

	}
	private static boolean isXpathUnique(final WebDriver driver,final String xpath) {
		List<WebElement>elements=driver.findElements(By.xpath(xpath));
		if(elements.size()==1) {
			return true;
		}else {
			return false;
		}
	}
	private static void makeXpathUnique(final WebDriver driver,final HashMap<String,String> element, final String xpath) {
		List<WebElement>elements=driver.findElements(By.xpath(xpath));
		for(int i=1;i<=elements.size();i++) {
			String xpathBulder="("+xpath+")["+i+"]";
			if(isXpathUnique(driver,xpathBulder) && isElementDisplayed(driver, xpathBulder)) {
				checkForTextXpathOrAddExistingXpath(driver,element, xpathBulder);
			}else {
				break;
			}
		}
	}
	private static void checkForTextXpathOrAddExistingXpath(final WebDriver driver,final HashMap<String,String> element, final String xpath) {
		WebElement webElement=driver.findElement(By.xpath(xpath));
		String elementText=webElement.getText();
		if(StringUtil.isNullOrEmpty(elementText)&&!xpath.contains("//a")) {
			xpaths.add(xpath.toString());
		}else {
			StringBuffer textXpath=new StringBuffer();
			String [] splitXpath=xpath.split("\\[");
			if(splitXpath[0].equals("//a")) {
				textXpath.append(splitXpath[0]);
				textXpath.append("[text()='");
				textXpath.append(elementText);
				textXpath.append("']");
				xpaths.add(textXpath.toString());
			}
		}
	}

	public static ArrayList<HashMap<String,String>> identifyAttributesOfallTags(final WebDriver driver,final String tagValue) {//final String tag
		List<WebElement> tags = driver.findElements(By.xpath(tagValue));
		ArrayList<HashMap<String,String>>listOfattributesofAlltags=new ArrayList<HashMap<String,String>>();
		for(WebElement tag:tags) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			Object attributes=executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) {items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", tag);
			List<String> attributeKeyValuePairs=Arrays.asList(attributes.toString().replace("{", "").replace("}", "").split(","));
			HashMap<String,String>attributesList=new HashMap<String, String>();
			attributesList.put("tag", tagValue);
			for(String keyValue:attributeKeyValuePairs) {
				String [] seperator=keyValue.split("=");
				if(!(seperator.length==1) && !StringUtils.isNumericSpace(seperator[1].trim()) && isNoSpecialCharInAttribute(seperator[1].trim())) {
					attributesList.put(seperator[0].trim(), seperator[1].trim());
				}
			}
			listOfattributesofAlltags.add(attributesList);
		}
		/*
		 * context.setListofAttributesOfallTag(listOfttributesList);
		 * System.out.println(context.getListofAttributesOfallTag());
		 */
		return listOfattributesofAlltags;
	}

	public static boolean isNoSpecialCharInAttribute(final String attributeValue) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]");
		Matcher matcher = pattern.matcher(attributeValue);
		return matcher.find();
	}


	public static boolean isElementDisplayed(final WebDriver driver,final String xpath) {
		boolean visibility=false;
		try {
			WebElement element=driver.findElement(By.xpath(xpath));
			if(element.isDisplayed()) {
				return true;
			}else {
				return visibility;
			}
		}catch(Exception e) {
			return visibility;
		}
	}
	public static boolean areDimensionsGood(final WebDriver driver,final String xpath) {
		boolean size=false;
		try {
			WebElement element=driver.findElement(By.xpath(xpath));
			Dimension elementSize=element.getSize();
			int height=elementSize.getHeight();
			int width=elementSize.getWidth();
			if(height==0 || width==0) {
				return true;
			}else {
				return size;
			}
		}catch(Exception e) {
			return size;
		}
	}
	public static void getFinalXpathsMap(final ArrayList<String> finalXpaths){
		HashMap<String, String> xpathMap= new HashMap<String, String>();
		for(String xpathValue:finalXpaths) {
			try {
				String[] dummyOne=xpathValue.split("='");
				String[] dummyTwo=dummyOne[1].split("\'");
				String xpathName=dummyTwo[0].replace(" ", "").replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
				if(!StringUtil.isNullOrEmpty(xpathName)) {
					xpathMap.put(xpathName, xpathValue);
				}
			}catch(ArrayIndexOutOfBoundsException e) {

			}
		}
		context.setFinalXpathsMap(xpathMap);
	}
}
