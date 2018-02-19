package alldriverspack;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.relevantcodes.extentreports.LogStatus;

//import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import utilititespack.ConfigProp;
import utilititespack.ReportGenerator;

public class AppiumAppDriver  {
	WebDriver remoteDriver = null;
	public WebDriver getNewDriver() {
		
		String grid_HN= ConfigProp.getPropertyValue("Appium_Grid_HostName");
		String grid_port= ConfigProp.getPropertyValue("Appium_Grid_Port");
		String host="http://"+grid_HN+":"+grid_port+"/wd/hub";
		System.out.println("appium driver host"+host);
//		String host = ConfigProp.getPropertyValue("AppiumHost");
		String deviceName = ConfigProp.getPropertyValue("Device_ID");
		String deviceId= ConfigProp.getPropertyValue(deviceName); 
		String os = ConfigProp.getPropertyValue("OS");
		String appRelPath = ConfigProp.getPropertyValue("AppPath");
		File appFile = new File(appRelPath);
		String appPath = appFile.getAbsolutePath();
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, os);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceId);
		capabilities.setCapability(MobileCapabilityType.APP, appPath);
		System.out.println(appPath);
		try {
			remoteDriver = new AndroidDriver<WebElement>(new URL(host), capabilities);
			ReportGenerator.log(LogStatus.INFO, "App driver is initiated successfully");
		} catch (Exception e) 
		{
			e.printStackTrace();	
			ReportGenerator.log(LogStatus.ERROR, e.getMessage());
		}
		return remoteDriver;
	}
	
	

}