package alltestspack;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

import alldriverspack.DriverFactory;
import pomelementspack.HomePage;
import utilititespack.ConfigProp;
import utilititespack.ReportGenerator;
import utilititespack.WrapperMethods;

public class HomePageValidations {

	public HomePageValidations()
	{
		System.out.println(ConfigProp.getPropertyValue("Browser").toUpperCase());
		DriverFactory.driverInit();
		//		ReportGenerator.log(LogStatus.INFO,"OS-" + ConfigProp.getPropertyValue("OS") + "Browser-" + ConfigProp.getPropertyValue("Browser"));

	}

	public void homepageValidation()
	{
		ReportGenerator.log(LogStatus.INFO, "Home Page Validaiton Started");
		String url = ConfigProp.getPropertyValue("BaseUrl");
		WrapperMethods.enter_URL(url);
		if(WrapperMethods.isDisplayed(HomePage.userID()))
		{
			ReportGenerator.log(LogStatus.PASS, "User ID Text box is displayed");
			WrapperMethods.sendKeys(HomePage.userID(), "123451");
		}
		else
		{
			ReportGenerator.log(LogStatus.FAIL, "User ID Text box not is displayed");
		}
		if(WrapperMethods.isDisplayed(HomePage.password()))
		{
			ReportGenerator.log(LogStatus.PASS, "Password Text box is displayed");
			WrapperMethods.sendKeys(HomePage.password(), "Avenger.123#");
		}
		else
		{
			ReportGenerator.log(LogStatus.FAIL, "Password Text box not is displayed");
		}
		if(WrapperMethods.isDisplayed(HomePage.loginBtn()))
		{
			ReportGenerator.log(LogStatus.PASS, "Login Button is displayed");
			WrapperMethods.click(HomePage.loginBtn(), "Clicked on Login Button", "Unable to click on Login Button");
		}
		else
		{
			ReportGenerator.log(LogStatus.FAIL, "Login Button not is displayed");
		}

		WrapperMethods.waitForElement(HomePage.userReg(), 30, "Login Successful", "User Registration is not displayed");
		

	}
}
