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
import java.util.stream.Collectors;

import javax.lang.model.util.Elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.org.ui.driverScript.Driver;

import io.netty.util.internal.StringUtil;


public class Finder {

	static Context context=new Context();
	static ArrayList<String> xpaths = new ArrayList<String>();
	public static List<String> getCurrentPageTags() {
		List<WebElement> NumbeofTagsDisplayed = Driver.getWebDriver().findElements(By.xpath("//*"));
		HashSet<String> finaltags = new HashSet<String>();
		for(WebElement element : NumbeofTagsDisplayed) {
			finaltags.add("//"+element.getTagName());
		}
		return new ArrayList<String>(finaltags);
	}

	public static void generateLocators(final HashMap<String,String> element) {
		boolean isIDPresent=element.containsKey("id");
		boolean isNamePresent=element.containsKey("name");
		boolean islinkPresent=element.containsValue("//a");
		if(isIDPresent) {
			getXpathByID(element);
		}else if (isNamePresent) {
			getXpathByName(element);
		}else{
			getTextORSystemXpath(element);
		}
		context.setFinalXpaths(xpaths);
	}

	private static void getXpathByID(final HashMap<String,String> element) {
		StringBuffer idXpath=new StringBuffer();
		idXpath.append(element.get("tag"));
		element.remove("tag");
		idXpath.append("[@id='");
		idXpath.append(element.get("id"));
		idXpath.append("']");
		xpaths.add(idXpath.toString());
	
	}
	private static void getXpathByName(final HashMap<String,String> element) {
		StringBuffer nameXpath=new StringBuffer();
		nameXpath.append(element.get("tag"));
		element.remove("tag");
		nameXpath.append("[@name='");
		nameXpath.append(element.get("name"));
		nameXpath.append("']");
		System.out.println(nameXpath.toString());
		xpaths.add(nameXpath.toString());
	}
	
	private static void getLinkXpaths(final HashMap<String,String> element) {
		StringBuffer linkXpath=new StringBuffer();
		linkXpath.append(element.get("tag"));
		element.remove("tag");
		linkXpath.append("[text()='");
		linkXpath.append(element.get("name"));
		linkXpath.append("']");
		System.out.println(linkXpath.toString());
		xpaths.add(linkXpath.toString());
	}
	
	private static void getTextORSystemXpath(final HashMap<String,String> element) {
		StringBuffer sysXpath=new StringBuffer();
		sysXpath.append(element.get("tag"));
		element.remove("tag");
		for(Entry<String, String> ele:element.entrySet()) {
			sysXpath.append("[@"+ele.getKey()+"='");
			sysXpath.append(ele.getValue());
			sysXpath.append("']");
		}
		boolean isunique=isXpathUnique(sysXpath.toString());
		if(isunique) {
			checkForTextXpath(element, sysXpath.toString());
		}
		if(!isunique) {
			makeXpathUnique(element,sysXpath.toString());
		}
	}

	private static boolean isXpathUnique(final String xpath) {
		List<WebElement>elements=Driver.getWebDriver().findElements(By.xpath(xpath));
		if(elements.size()==1) {
			return true;
		}else {
			return false;
		}
	}
	private static void makeXpathUnique(final HashMap<String,String> element, final String xpath) {
		List<WebElement>elements=Driver.getWebDriver().findElements(By.xpath(xpath));
		for(int i=1;i<=elements.size();i++) {
			String xpathBulder="("+xpath+")["+i+"]";
			if(isXpathUnique(xpathBulder)) {
				checkForTextXpath(element, xpathBulder);
			}else {
				break;
			}
		}
	}
	private static void checkForTextXpath(final HashMap<String,String> element, final String xpath) {
		WebElement webElement=Driver.getWebDriver().findElement(By.xpath(xpath));
		String elementText=webElement.getText();
		if(StringUtil.isNullOrEmpty(elementText)) {
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

	public static ArrayList<HashMap<String,String>> identifyAttributesOfallTags(final String tagValue) {//final String tag
		List<WebElement> tags = Driver.getWebDriver().findElements(By.xpath(tagValue));
		ArrayList<HashMap<String,String>>listOfattributesofAlltags=new ArrayList<HashMap<String,String>>();
		for(WebElement tag:tags) {
			JavascriptExecutor executor = (JavascriptExecutor) Driver.getWebDriver();
			Object attributes=executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) {items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", tag);
			List<String> attributeKeyValuePairs=Arrays.asList(attributes.toString().replace("{", "").replace("}", "").split(","));
			HashMap<String,String>attributesList=new HashMap<String, String>();
			attributesList.put("tag", tagValue);
			for(String keyValue:attributeKeyValuePairs) {
				String [] seperator=keyValue.split("=");
				if(!(seperator.length==1) && !StringUtils.isNumericSpace(seperator[1].trim())) {
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




	public void UIvalidation(By arg, String[] UIElements, String objType) {
		String[] UIElementsNames = null;
		UIElementsNames = UIElements;
		String[] Elements = new String[50];
		Arrays.fill(Elements, null);
		Elements = Arrays.stream(Elements).filter(Objects::nonNull).toArray(String[]::new);
		ArrayList<String> SameElements = new ArrayList<String>();
		ArrayList<String> JunkElements = new ArrayList<String>();
		SameElements.clear();
		JunkElements.clear();
		Runtime r = Runtime.getRuntime();
		r.gc();
		String[] argument = arg.toString().split(" ");

		switch (argument[1]) {

		case "//radio":

			By radiomapper = By.xpath("//input[@type='radio']/following-sibling::label | //input[@type='radio']/preceding-sibling::label");
			Elements = WebListtoStringArray(radiomapper);
			Elements = Arrays.stream(Elements).filter(Objects::nonNull).toArray(String[]::new);
			for (int i = 0; i < Elements.length; i++) {
				for (int j = 0; j < UIElementsNames.length; j++) {
					if (UIElementsNames[j].equalsIgnoreCase(Elements[i])) {
						SameElements.add(UIElementsNames[j]);

						/*
						 * report.updateTestLog("Verify " + UIElementsNames[j] + " " + objType +
						 * " is Present", "Verified " + UIElementsNames[j] + " of type " + objType +
						 * " is displayed", Status.DONE);
						 */

					} else {
						JunkElements.add(UIElementsNames[j]);
						JunkElements.add(Elements[i]);
					}
				}
			}
			System.out.println("SameElements" + SameElements);
			Set<String> Newradiobuttons = new LinkedHashSet<>(JunkElements);
			Newradiobuttons.removeAll(SameElements);
			System.out.println("NewElements" + Newradiobuttons);

			if (Newradiobuttons.isEmpty()) {
				/*
				 * report.updateTestLog("Verify All Elements of type " + objType +
				 * " is Present", "Verified " + SameElements + " of type " + objType +
				 * " is displayed", Status.DONE);
				 */
			} else {
				/*
				 * report.updateTestLog( "Verify New Elements of type " + objType +
				 * " is Present or additional data given by you", "Verified " + Newradiobuttons
				 * + " of type " + objType + " is displayed", Status.WARNING);
				 */
			}
			break;

		case "//input":

			By fieldmapper = By.xpath(argument[1] + " | " + argument[1] + "[@type='text']/following-sibling::label"
					+ " | " + argument[1] + "[@type='text']/preceding-sibling::label");
			Elements = WebListtoStringArray(fieldmapper);
			Elements = Arrays.stream(Elements).filter(Objects::nonNull).toArray(String[]::new);
			for (int i = 0; i < Elements.length; i++) {
				for (int j = 0; j < UIElementsNames.length; j++) {
					if (UIElementsNames[j].equalsIgnoreCase(Elements[i])) {
						SameElements.add(UIElementsNames[j]);

						/*
						 * report.updateTestLog("Verify " + UIElementsNames[j] + " " + objType +
						 * " is Present", "Verified " + UIElementsNames[j] + " of type " + objType +
						 * " is displayed", Status.DONE);
						 */

					} else {
						JunkElements.add(UIElementsNames[j]);
						JunkElements.add(Elements[i]);
					}
				}
			}
			System.out.println("SameElements" + SameElements);
			Set<String> NewFields = new LinkedHashSet<>(JunkElements);
			NewFields.removeAll(SameElements);
			System.out.println("NewElements" + NewFields);

			if (NewFields.isEmpty()) {
				/*
				 * report.updateTestLog("Verify All Elements of type " + objType +
				 * " is Present", "Verified " + SameElements + " of type " + objType +
				 * " is displayed", Status.DONE);
				 */
			} else {
				/*
				 * report.updateTestLog( "Verify New Elements of type " + objType +
				 * " is Present or additional data given by you", "Verified " + NewFields +
				 * " of type " + objType + " is displayed", Status.WARNING);
				 */
			}
			break;

		case "//button":

			By buttonmapper = By.xpath(argument[1] + " | " + argument[1] + " /following-sibling::label" + " | "
					+ argument[1] + "/preceding-sibling::label");
			Elements = WebListtoStringArray(buttonmapper);
			Elements = Arrays.stream(Elements).filter(Objects::nonNull).toArray(String[]::new);
			for (int i = 0; i < Elements.length; i++) {
				for (int j = 0; j < UIElementsNames.length; j++) {
					if (UIElementsNames[j].equalsIgnoreCase(Elements[i])) {
						SameElements.add(UIElementsNames[j]);

						/*
						 * report.updateTestLog("Verify " + UIElementsNames[j] + " " + objType +
						 * " is Present", "Verified " + UIElementsNames[j] + " of type " + objType +
						 * " is displayed", Status.DONE);
						 */

					} else {
						JunkElements.add(UIElementsNames[j]);
						JunkElements.add(Elements[i]);
					}
				}
			}
			System.out.println("SameElements" + SameElements);
			Set<String> NewButtons = new LinkedHashSet<>(JunkElements);
			NewButtons.removeAll(SameElements);
			System.out.println("NewElements" + NewButtons);

			if (NewButtons.isEmpty()) {
				/*
				 * report.updateTestLog("Verify All Elements of type " + objType +
				 * " is Present", "Verified " + SameElements + " of type " + objType +
				 * " is displayed", Status.DONE);
				 */
			} else {
				/*
				 * report.updateTestLog( "Verify New Elements of type " + objType +
				 * " is Present or additional data given by you", "Verified " + NewButtons +
				 * " of type " + objType + " is displayed", Status.WARNING);
				 */
			}
			break;

		case "//label":
			By labelmapper = By.xpath(argument[1] + " | " + argument[1] + " /following-sibling::label" + " | "
					+ argument[1] + "/preceding-sibling::label");
			Elements = WebListtoStringArray(labelmapper);
			Elements = Arrays.stream(Elements).filter(Objects::nonNull).toArray(String[]::new);
			for (int i = 0; i < Elements.length; i++) {
				for (int j = 0; j < UIElementsNames.length; j++) {
					if (UIElementsNames[j].equalsIgnoreCase(Elements[i])) {
						SameElements.add(UIElementsNames[j]);

						/*
						 * report.updateTestLog("Verify " + UIElementsNames[j] + " " + objType +
						 * " is Present", "Verified " + UIElementsNames[j] + " of type " + objType +
						 * " is displayed", Status.DONE);
						 */

					} else {
						JunkElements.add(UIElementsNames[j]);
						JunkElements.add(Elements[i]);
					}
				}
			}
			System.out.println("SameElements" + SameElements);
			Set<String> NewLabels = new LinkedHashSet<>(JunkElements);
			NewLabels.removeAll(SameElements);
			System.out.println("NewElements" + NewLabels);

			if (NewLabels.isEmpty()) {
				/*
				 * report.updateTestLog("Verify All Elements of type " + objType +
				 * " is Present", "Verified " + SameElements + " of type " + objType +
				 * " is displayed", Status.DONE);
				 */
			} else {
				/*
				 * report.updateTestLog( "Verify New Elements of type " + objType +
				 * " is Present or additional data given by you", "Verified " + NewLabels +
				 * " of type " + objType + " is displayed", Status.WARNING);
				 */
			}
			break;

		case "//select":

			By dropdownmapper = By.xpath(argument[1] + "/option[contains(text(),'" + UIElementsNames + "')]");

			for (int i = 0; i < Elements.length; i++) {
				for (int j = 0; j < UIElementsNames.length; j++) {
					if (UIElementsNames[j].equalsIgnoreCase(Elements[i])) {
						SameElements.add(UIElementsNames[j]);

						/*
						 * report.updateTestLog("Verify " + UIElementsNames[j] + " " + objType +
						 * " is Present", "Verified " + UIElementsNames[j] + " of type " + objType +
						 * " is displayed", Status.DONE);
						 */

					} else {
						JunkElements.add(UIElementsNames[j]);
						JunkElements.add(Elements[i]);
					}
				}
			}
			System.out.println("SameElements" + SameElements);
			Set<String> NewDropdown = new LinkedHashSet<>(JunkElements);
			NewDropdown.removeAll(SameElements);
			System.out.println("NewElements" + NewDropdown);

			if (NewDropdown.isEmpty()) {
				/*
				 * report.updateTestLog("Verify All Elements of type " + objType +
				 * " is Present", "Verified " + SameElements + " of type " + objType +
				 * " is displayed", Status.DONE);
				 */
			} else {
				/*
				 * report.updateTestLog( "Verify New Elements of type " + objType +
				 * " is Present or additional data given by you", "Verified " + NewDropdown +
				 * " of type " + objType + " is displayed", Status.WARNING);
				 */
			}
			break;
		}

	}

	public List<String> getCurrentPageTags_old() {
		List<WebElement> NumbeofTagsDisplayed = Driver.getWebDriver().findElements(By.xpath("//*"));
		String[] Elements = new String[NumbeofTagsDisplayed.size()];
		int i = 0;
		for (WebElement a : NumbeofTagsDisplayed) {
			Elements[i] ="//"+ a.getTagName();
			if (Elements[i].isEmpty()) {
				// report.updateTestLog("No web Elements found", Status.WARNING); 
			} else {
				i++;
			}
		}		
		List<String> list = Arrays.stream(Elements).distinct().filter(Objects::nonNull).collect(Collectors.toList());
		return list;	
	}

	public int getNumberOfBodyDisplayedInDOM() {
		List<WebElement> NumbeofBodyDisplayed = Driver.getWebDriver().findElements(By.xpath("//body"));
		int number = NumbeofBodyDisplayed.size();
		int count =0;
		for(int i=0;i<=number;i++) {
			if(NumbeofBodyDisplayed.get(i).isDisplayed()) {
				count++;
			}			
		}
		return count;		
	}


	// converts web element list into string array and returns web element Text.
	// Pass 'xpath' of elements like(//label) as 'arg'.

	public String[] WebListtoStringArray(By arg) {
		List<WebElement> listOfElements = Driver.getWebDriver().findElements(arg);
		String[] Elements = new String[listOfElements.size()];
		int i = 0;
		for (WebElement a : listOfElements) {
			Elements[i] = a.getText();
			if (Elements[i].isEmpty()) {
				/* report.updateTestLog("No web Elements found", Status.WARNING); */

			} else {

				i++;
			}
		}
		String[] cleanedArray = Arrays.stream(Elements).filter(Objects::nonNull).toArray(String[]::new);
		return cleanedArray;

	}

	public List<String> getVisibleTextInList(By arg) {
		List<WebElement> listOfElements = Driver.getWebDriver().findElements(arg);
		String[] Elements = new String[listOfElements.size()];
		int i = 0;
		for (WebElement a : listOfElements) {
			System.out.println(listOfElements.get(i).getText());
			if(a.isDisplayed()|a.isEnabled()) {
				Elements[i] = a.getText().trim();
				i++;		
			}
		}
		List<String> list = Arrays.stream(Elements).distinct().filter(Objects::nonNull).collect(Collectors.toList());
		List<String> FinalitemList = Arrays.stream(Arrays.stream(list.toString().replace("\n", ",").replace("[", "").replace("]", "").split(",")).map(String::trim).toArray(String[]::new)).distinct().filter(Objects::nonNull).collect(Collectors.toList());
		return FinalitemList;	    
	}

	public List<String> removeNewLine(List<String> list) {
		List<String> listOfElements = new ArrayList<String>();
		String[] FinallistOfElements = null;
		for(int i=0;i<list.size();i++) {
			String values = list.get(i).replaceAll("\n", ",").trim();
			String value=values.replace(",", ",");
			listOfElements.add(value);
		}
		String newvalues =listOfElements.toString();
		FinallistOfElements = newvalues.split(",");
		List<String> Finallist = Arrays.stream(FinallistOfElements).distinct().filter(Objects::nonNull).collect(Collectors.toList());
		//List<String> FinalValues=new ArrayList<String>(new HashSet<>(removeNewLine(Finallist)));	
		return Finallist;
	}

	public List<String> removeDuplicates(List<String> finalValues) {
		for(int i = 0; i < finalValues.size(); i++) {
			for(int j = i + 1; j < finalValues.size(); j++) {
				if(finalValues.get(i).equals(finalValues.get(j))){
					finalValues.remove(j);
					j--;
				}
			}
		}
		return finalValues;
	}


}
