package com.org.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Context {
	private List<String> listOfTags;
	//private HashMap<String,String> attributesOfaTag;
	//private ArrayList<HashMap<String,String>> listofAttributesOfallTag;
	private ArrayList<ArrayList<HashMap<String,String>>> listoflistofAttributesOfallTag;
	private static ArrayList<String> finalXpaths;
	private static HashMap<String, String> finalXpathsMap;
	
	
	public static HashMap<String, String> getFinalXpathsMap() {
		return finalXpathsMap;
	}

	public static void setFinalXpathsMap(HashMap<String, String> finalXpathsMap) {
		Context.finalXpathsMap = finalXpathsMap;
	}

	public ArrayList<String> getFinalXpaths() {
		return finalXpaths;
	}

	public void setFinalXpaths(ArrayList<String> finalXpaths) {
		this.finalXpaths = finalXpaths;
	}

	public ArrayList<ArrayList<HashMap<String, String>>> getListoflistofAttributesOfallTag() {
		return listoflistofAttributesOfallTag;
	}

	public void setListoflistofAttributesOfallTag(
			ArrayList<ArrayList<HashMap<String, String>>> listoflistofAttributesOfallTag) {
		this.listoflistofAttributesOfallTag = listoflistofAttributesOfallTag;
	}

	/*public ArrayList<HashMap<String, String>> getListofAttributesOfallTag() {
		return listofAttributesOfallTag;
	}

	public void setListofAttributesOfallTag(ArrayList<HashMap<String, String>> listofAttributesOfallTag) {
		this.listofAttributesOfallTag = listofAttributesOfallTag;
	}*/

	public List<String> getListOfTags() {
		return listOfTags;
	}

	public void setListOfTags(List<String> tags) {
		this.listOfTags = tags;
	}

	/*public HashMap<String, String> getAttributesOfaTag() {
		return attributesOfaTag;
	}

	public void setAttributesOfaTag(HashMap<String, String> attributesOfaTag) {
		this.attributesOfaTag = attributesOfaTag;
	}*/


}
