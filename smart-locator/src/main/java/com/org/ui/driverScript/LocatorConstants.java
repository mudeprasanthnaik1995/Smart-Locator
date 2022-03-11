package com.org.ui.driverScript;

import org.openqa.selenium.By;

public class LocatorConstants {
	//public static final By RADIOLABLE=By.xpath("//input[@type='radio']/following-sibling::label | //input[@type='radio']/preceding-sibling::label");
	public static final By RADIO=By.xpath("//input[@type='radio']");
	//public static final By TEXTFIELDLABLE=By.xpath("//input[@type='text']/following-sibling::label | //input[@type='text']/preceding-sibling::label | //input[@type='password']/following-sibling::label | //input[@type='password']/preceding-sibling::label");
	public static final By TEXTFIELD=By.xpath("//input[@type='text'] | //input[@type='password']");
	public static final By BUTTON=By.xpath("//input[@type='button']");
}
