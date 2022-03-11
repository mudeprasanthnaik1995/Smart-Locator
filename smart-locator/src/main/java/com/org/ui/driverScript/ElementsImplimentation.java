package com.org.ui.driverScript;

import java.util.HashSet;

import org.openqa.selenium.WebElement;

public class ElementsImplimentation {

	public long getRadioLocators() {
		HashSet<WebElement> totalRadios = new HashSet<WebElement>(Driver.getWebDriver().findElements(LocatorConstants.RADIO));
		for(WebElement radio:totalRadios) {

		}
		return totalRadios.size();
	}

	public long getTextLocators() {
		HashSet<WebElement> totalTextFileds = new HashSet<WebElement>(Driver.getWebDriver().findElements(LocatorConstants.TEXTFIELD));
		for(WebElement textField:totalTextFileds) {
			
		}
		return totalTextFileds.size();
	}


}
