package alldriverspack;

import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.relevantcodes.extentreports.LogStatus;

import utilititespack.ConfigProp;
import utilititespack.ReportGenerator;

public class DeskDriver implements DriverInterface {
	WebDriver domDriver = null;
	WebDriver remoteDriver = null;

	public WebDriver getNewDriver() {
		String isRemote = ConfigProp.getPropertyValue("IsRemote");
		if ("yes".equalsIgnoreCase(isRemote)) {
			remoteDriver = getRemoteDriver();
			return remoteDriver;
		} else {
			if (domDriver == null) {
				switch (ConfigProp.getPropertyValue("Browser").toUpperCase()) {
				case "FIREFOX":
					domDriver = new FirefoxDriver();
					break;
				case "CHROME":
					System.setProperty("webdriver.chrome.driver", "C:\\Users\\10647503\\Documents\\Drivers\\chromedriver.exe");
					domDriver = new ChromeDriver();
					domDriver.manage().window().maximize();
					break;
				case "IE":
					System.setProperty("webdriver.ie.driver", "C:\\Users\\10647503\\Documents\\Drivers\\IEDriverServer.exe");
					domDriver = new InternetExplorerDriver();
					break;
				case "SAFARI":
					domDriver = new SafariDriver();
					break;
				default:
					domDriver = new FirefoxDriver();
					break;
				}
			}
			return domDriver;
		}

	}

	public WebDriver getRemoteDriver() {
		String SelHost = ConfigProp.getPropertyValue("SelHost");
		DesiredCapabilities desiredCap = null;
		try {
			switch (ConfigProp.getPropertyValue("Browser").toUpperCase()) {
			case "FIREFOX":
				desiredCap = DesiredCapabilities.firefox();
				ReportGenerator.log(LogStatus.INFO, "Firefox is initiated");
				break;
			case "CHROME":
				// System.setProperty("webdriver.chrome.driver",
				// "C:/chromedriver_win32/chromedriver.exe");
				desiredCap = DesiredCapabilities.chrome();
				desiredCap.setBrowserName("chrome");
				ReportGenerator.log(LogStatus.INFO, "Chrome is initiated");
				break;
			case "IE":
				System.out.println("IE - Inside Switch");
				 //System.setProperty("webdriver.ie.driver", "D:\\Users\\saravanan.mariappan\\Desktop\\Server\\IEDriverServer.exe");   
				desiredCap = DesiredCapabilities.internetExplorer();
				desiredCap.setBrowserName("internet explorer");
				desiredCap.setCapability(InternetExplorerDriver.BROWSER_ATTACH_TIMEOUT, 30000);
				desiredCap.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
				desiredCap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				desiredCap.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
				desiredCap.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
				ReportGenerator.log(LogStatus.INFO, "Internet Explorer is initiated");
				break;
			case "SAFARI":
					SafariOptions options = new SafariOptions();
					options.setUseCleanSession(true);
					desiredCap = DesiredCapabilities.safari();
					desiredCap.setCapability("webdriver.safari.noinstall", "true");
					ReportGenerator.log(LogStatus.INFO, "Safari browser is initiated");
				break;
			default:
				desiredCap = DesiredCapabilities.firefox();
				break;
			}
			remoteDriver = new RemoteWebDriver(new URL(SelHost), desiredCap);
		     remoteDriver.manage().window().maximize();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Driver Issue");
			e.printStackTrace();
			ReportGenerator.log(LogStatus.ERROR, e.getMessage());
		}
		return remoteDriver;
	}
}