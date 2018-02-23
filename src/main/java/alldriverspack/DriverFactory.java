package alldriverspack;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import utilititespack.ConfigProp;
import utilititespack.ReportGenerator;

public class DriverFactory {
	private static ThreadLocal<WebDriver> currentDriver = new ThreadLocal<WebDriver>();

	public static WebDriver getCurrentDriver() {
		WebDriver driver = currentDriver.get();
		if(driver != null){
			System.out.println("Driver  init"+driver.toString());
		return driver;
		}else{
			System.out.println("Driver not init");
			ReportGenerator.log(LogStatus.ERROR, "Driver Not Initialised");
			return null;
		}
	}
	
	public static void driverInit() {
		System.out.println("Driver is  "+ConfigProp.getPropertyValue("Driver").toUpperCase() );
		switch (ConfigProp.getPropertyValue("Driver").toUpperCase()) {
		case "FIREFOX":
			currentDriver.set(new DeskDriver().getNewDriver());
			break;	
		case "CHROME":
			currentDriver.set(new DeskDriver().getNewDriver());
			break;	
		case "IE":
			currentDriver.set(new DeskDriver().getNewDriver());
			break;	
		case "APPIUMAPP":
			currentDriver.set(new AppiumAppDriver().getNewDriver());
			break;
		case "SAFARI":
			currentDriver.set(new DeskDriver().getNewDriver());
			break;
		default:
			System.out.println("Unknown Driver");
		}
	}

	public static void driverCleanUp() {
		WebDriver driver = currentDriver.get();
		if(driver != null){
		switch (ConfigProp.getPropertyValue("Driver").toUpperCase()) {
		case "APPIUMAPP":	
			AndroidDriver<?> k = (AndroidDriver<?>)DriverFactory.getCurrentDriver();
			k.closeApp();
			ReportGenerator.log(LogStatus.INFO, "App is closed successfully");
			break;
		default:
			getCurrentDriver().close();
			break;
		}		
		getCurrentDriver().quit();
		
	}
	currentDriver.remove();
}
}
