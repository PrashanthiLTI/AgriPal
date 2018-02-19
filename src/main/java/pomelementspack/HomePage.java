package pomelementspack;

import org.openqa.selenium.By;

import utilititespack.Locators;
import utilititespack.WrapperMethods;

public class HomePage {
	public static By userID() {
		return WrapperMethods.locatorValue(Locators.ID, "email");
	}
	public static By password() {
		return WrapperMethods.locatorValue(Locators.ID, "password");
	}
	public static By loginBtn() {
		return WrapperMethods.locatorValue(Locators.XPATH, "//button[contains(.,'Login')]");
	}
	
	public static By userReg() {
		return WrapperMethods.locatorValue(Locators.XPATH, "//h4[contains(.,'User Registration')]");
	}
}
