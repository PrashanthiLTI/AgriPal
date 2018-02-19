package utilititespack;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import alldriverspack.DriverFactory;

public class WrapperMethods {
	// static WebDriver driver = DriverFactory.getCurrentDriver();

	boolean cStatus = false;

	public static void verifyTextLength(By Byele, String value, int length, String expectedValue) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			if (element.getText().length() > length) {
				ReportGenerator.log(LogStatus.PASS, expectedValue + "is displayed.");
			}

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, expectedValue + "is not displayed.");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static List<WebElement> getWebElements(By byele) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		return driver.findElements(byele);
	}

	public static void click(WebElement element, String clickele) {
		try {
			element.click();
			// MethodDef.passLog("Button clicked");
			ReportGenerator.log(LogStatus.PASS, clickele + "Element is clicked successfully");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Click is not successfully");
			ReportGenerator.log(LogStatus.ERROR, "Button is NOT clicked successfully");
		}
	}

	public static void textAttributeNotEmpty(By Byele, String attributename, String name) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		try {
			String value = driver.findElement(Byele).getAttribute(attributename);
			if (!value.isEmpty())
				ReportGenerator.log(LogStatus.PASS,
						"Attribute " + attributename + " is present and its contains text -" + name);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Attribute " + attributename + " is not present for the text -" + name);
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}

	public static void validateResponseCode(String url, int exceptedCode) throws IOException {
		URL url1 = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		int code = connection.getResponseCode();
		if (code == exceptedCode) {
			ReportGenerator.log(LogStatus.PASS, "The response for the code is " + code);
		} else {
			ReportGenerator.log(LogStatus.FAIL,
					"The response code " + code + " doesn't match with the excepted code " + exceptedCode);
		}
	}

	public static void sendkeys(By locator, String text, String pc, String fc) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(locator);
			element.sendKeys(text);
			ReportGenerator.log(LogStatus.PASS, pc + "--" + text);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, fc + "--" + text);
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void sendkeysTextEnter(By Byele, String text) {

		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement elem = driver.findElement(Byele);
		try {
			elem.sendKeys(text + Keys.ENTER);
			ReportGenerator.log(LogStatus.PASS, "Pressed Enter with -" + text);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Not able to pressed enter with - " + text);
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void waitAndclick(By by, int timeout, String pc, String fc) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.elementToBeClickable(by)).click();
			ReportGenerator.log(LogStatus.PASS, pc);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, fc);
		}
	}

	public static void validateURLContents(String string, String pc, String fc) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String currentURL = driver.getCurrentUrl();
		if (currentURL.contains(string)) {
			ReportGenerator.log(LogStatus.PASS, pc);
		} else {
			ReportGenerator.log(LogStatus.PASS, fc + " The current url is " + currentURL);
		}
	}

	public static void waitAndCheckTextContains(By locator, int timeout, String compareText, String pc, String fc) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String Text = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			Text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
			System.out.println(Text + "----");
			if (Text.matches(compareText)) {
				ReportGenerator.log(LogStatus.PASS, pc);
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, fc);
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void waitForPageLoaded(WebDriver driver) {

		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		Wait<WebDriver> wait = new WebDriverWait(driver, 120);
		try {
			wait.until(expectation);
		} catch (Throwable error) {
			ReportGenerator.log(LogStatus.FAIL, error.getMessage());
		}
	}

	public static void waitForPageLoaded() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		Wait<WebDriver> wait = new WebDriverWait(driver, 120);
		try {
			wait.until(expectation);
		} catch (Throwable error) {
			ReportGenerator.log(LogStatus.FAIL, error.getMessage());
		}
	}

	public static void mouse_Hover(By Byelem, String value) {

		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement elem = driver.findElement(Byelem);
		try {
			Actions a1 = new Actions(driver);
			a1.moveToElement(elem).build().perform();
			ReportGenerator.log(LogStatus.PASS, "Mouse Over successfully on " + value);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Mouse Over is not getting successfully on " + value);
			ReportGenerator.log(LogStatus.INFO, "Mouse Over is not getting successfully on " + value);
		}
	}


	public static void clear(By locator) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.findElement(locator).clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void enter_URL(String URL) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			driver.navigate().to(URL);
			ReportGenerator.log(LogStatus.PASS, "<b>TEST URL:  </b> <a href=\"" + URL + "\">" + URL + "</a><BR>");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "URL is not naviagted -" + URL);
		}
	}



	public static By locatorValue(Locators locatorTpye, String value) {
		By by = null;
		switch (locatorTpye) {
		case ID:
			by = By.id(value);
			break;
		case NAME:
			by = By.name(value);
			break;
		case XPATH:
			by = By.xpath(value);
			break;
		case CSS:
			by = By.cssSelector(value);
			break;
		case LINKTEXT:
			by = By.linkText(value);
			break;
		case PARTIAL_LINKTEXT:
			by = By.partialLinkText(value);
			break;
		case TAG_NAME:
			by = By.tagName(value);
			break;
		case CLASS_NAME:
			by = By.className(value);
			break;
		case NA:
			break;
		}
		return by;
	}

	public static List<WebElement> locateElements(Locators locatorType, String value) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		By by = null;
		switch (locatorType) {
		case ID:
			by = By.id(value);
			break;
		case NAME:
			by = By.name(value);
			break;
		case XPATH:
			by = By.xpath(value);
			break;
		case CSS:
			by = By.cssSelector(value);
			break;
		case LINKTEXT:
			by = By.linkText(value);
			break;
		case PARTIAL_LINKTEXT:
			by = By.partialLinkText(value);
			break;
		case TAG_NAME:
			by = By.tagName(value);
			break;
		case CLASS_NAME:
			by = By.className(value);
			break;
		case NA:
			break;
		}
		try {
			return driver.findElements(by);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.ERROR, "Unable to Find the Element" + value);
			return null;
		}
	}

	public static void enter_Text(By Byele, String text) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			element.sendKeys(text);
			ReportGenerator.log(LogStatus.PASS, "Text is entered successfully");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Text is not entered successfully");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}

	}

	public static void click_on_Link(By Byele, String value, String Linkname) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			element.click();
			ReportGenerator.log(LogStatus.PASS, Linkname + " Link is Clicked successfully");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, Linkname + " Text is not clicked successfully");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}

	}

	public static void click_on_Button(By Byele, String value, String btn_name) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		try {
			WebElement element = driver.findElement(Byele);
			element.click();
			ReportGenerator.log(LogStatus.PASS, btn_name + " Button Clicked successfully");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, btn_name + " Button is not clicked successfully");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}

	public static void click(By by, String clickele) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			WebElement element = driver.findElement(by);
			element.click();
			ReportGenerator.log(LogStatus.PASS, clickele + " is clicked successfully");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Click is not successful" + e.getMessage());
			ReportGenerator.log(LogStatus.ERROR, "Button is NOT clicked successfully" + by.toString());
		}
	}

	public static void select_Value(By Byele, String value, String method, String SelectText) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			Select se = new Select(driver.findElement(Byele));
			switch (method) {
			case "index":
				se.selectByIndex(Integer.parseInt(SelectText));
				ReportGenerator.log(LogStatus.PASS, "Element is selected successfully");
				break;
			case "value":
				se.selectByValue(SelectText);
				ReportGenerator.log(LogStatus.PASS, "Element is selected successfully");
				break;
			case "visibletext":
				se.selectByVisibleText(SelectText);
				ReportGenerator.log(LogStatus.PASS, "Element is selected successfully");
				break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			ReportGenerator.log(LogStatus.FAIL, "Element is not selected");
			ReportGenerator.log(LogStatus.ERROR, "Element is not selected");
		}
	}

	public static void rightClick(By locator, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			Actions a1 = new Actions(driver);

			a1.contextClick(driver.findElement(locator)).build().perform();

			ReportGenerator.log(LogStatus.PASS, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ".<br> The error message is " + e.getMessage());
		}
	}

	public static void verify_Text(By Byele, String value, String compareText) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String Text = null;
		try {
			WebElement element = driver.findElement(Byele);
			Text = element.getText();
			if (Text.equalsIgnoreCase(value) == true) {
				ReportGenerator.log(LogStatus.PASS, "Text is same");
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Text is not same");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}

	}

	public static void verify_Title(String compareTitle) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String Title = null;

		try {
			Title = driver.getTitle();
			if (Title.equalsIgnoreCase(compareTitle) == true) {
				ReportGenerator.log(LogStatus.PASS, "Title is same" + Title);
			}

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Title is not same");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static String get_Title(String value) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String Title = null;
		try {
			Title = driver.getTitle();
			ReportGenerator.log(LogStatus.PASS, "Title:" + Title);

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Title not Found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
		return Title;

	}

	public static void contains_Text(By locator, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(locator);
			if (!element.getText().isEmpty()) {
				ReportGenerator.log(LogStatus.PASS, PassComments + element.getText());
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + "<br>The error message is " + e.getMessage());
		}
	}

	public static void verifyElement(WebElement element, String section) {
		try {
			element.isDisplayed();
			ReportGenerator.log(LogStatus.PASS, section + "- Available in the page");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, section + "NOT - Available in the page");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}

	public static void verifyElement(By locatorType, String section) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.findElement(locatorType).isDisplayed();
			ReportGenerator.log(LogStatus.PASS, section + "- Available in the page");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, section + "NOT - Available in the page");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}

	public static void assertIsTrue(Boolean condition, String Passmsg, String Failmsg) {
		try {
			if (condition == true)
				ReportGenerator.log(LogStatus.PASS, Passmsg);
			else
				ReportGenerator.log(LogStatus.FAIL, Failmsg);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void contains_Text(By Byele, String conText) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			WebElement element = driver.findElement(Byele);
			System.out.println(element.getText() + "===" + conText);
			if (element.getText().contains(conText) == true) {
				ReportGenerator.log(LogStatus.PASS, "Elements contains" + conText + "Text");
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Text is not entered successfully");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static boolean isDisplayed(By by) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			List<WebElement> element = driver.findElements(by);
			if (element.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
			return false;
		}
	}

	public static boolean checkIfPresent(By locator, int timeout) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			boolean result = false;
			if (driver.findElements(locator).size() == 0) {
			} else {
				result = true;
			}
			return result;
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.ERROR, "Element not found");
			return false;
		}
	}

	public static boolean checkIfPresent(List<WebElement> locator, int timeout, String PassFailMsg) {
		try {
			boolean result = false;
			if (locator.size() == 0) {
			} else {
				result = true;
			}

			if (result = true)
				ReportGenerator.log(LogStatus.PASS, PassFailMsg + " is present in the page");
			else
				ReportGenerator.log(LogStatus.FAIL, PassFailMsg + " is not present in the page");

			return result;
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.ERROR, "Element not found");
			return false;
		}
	}

	public static boolean isVisible(By by) {
		boolean isvisible;
		try {

			WebDriver driver = DriverFactory.getCurrentDriver();
			isvisible = driver.findElement(by).isDisplayed();
			System.out.println("Element is visible : " + isvisible);
			return isvisible;

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
			return false;
		}

	}

	public static void textNotEmpty(By Byele) {

		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement element = driver.findElement(Byele);

		try {
			if (!element.getText().isEmpty())

				ReportGenerator.log(LogStatus.PASS, "Elements contains text");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Elements not contains text");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}

	public static void startwith_Text(By Byele, String value, String startWithText) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			if (element.getText().startsWith(startWithText) == true) {
				ReportGenerator.log(LogStatus.PASS, "Text is start with" + startWithText + "");

			}

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Text is not start with");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void startwith_Text(By Byele, String startWithText) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			if (element.getText().startsWith(startWithText) == true) {
				ReportGenerator.log(LogStatus.PASS, "Text is start with" + startWithText + "");

			}

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Text is not start with");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public String get_Text(By Byele, String value) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String Text = null;
		try {
			WebElement element = driver.findElement(Byele);
			Text = element.getText();
			ReportGenerator.log(LogStatus.PASS, "Text is found");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Text is not found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
		return Text;
	}

	public static void close_Browser() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			Thread.sleep(5000);
			driver.quit();
			// MethodDef.passLog("Driver Closed");
			ReportGenerator.log(LogStatus.PASS, "Text is entered successfully");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Unable close the Driver");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}

	}

	public static void switch_Window() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		try {
			Set<String> win = driver.getWindowHandles();
			int i = 0;
			for (String w : win) {
				if (i == 1) {
					driver.switchTo().window(w);
					break;
				}
				if (i == 0) {
					driver.switchTo().window(w);
					// String parent=w;
					i = i + 1;
					ReportGenerator.log(LogStatus.PASS, "Switch window");
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			ReportGenerator.log(LogStatus.FAIL, "Unable Switch the Window");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void accept_Alert() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		try {
			driver.switchTo().alert().accept();
			ReportGenerator.log(LogStatus.PASS, "Alert is Accepted");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ReportGenerator.log(LogStatus.FAIL, "Alert is not found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}

	public static void dismiss_Alert() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		try {
			driver.switchTo().alert().dismiss();
			ReportGenerator.log(LogStatus.PASS, "Alert dismissed");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			ReportGenerator.log(LogStatus.FAIL, "Alert is not found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public String get_Alert_Text() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String alertText = null;
		try {
			alertText = driver.switchTo().alert().getText();
			ReportGenerator.log(LogStatus.PASS, "Alert Text get successful");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			ReportGenerator.log(LogStatus.FAIL, "Alert is not found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
		return alertText;

	}

	public static void verify_LinkText(By Byele, String value, String compareText) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			if (driver.findElement(Byele).isDisplayed()) {
				String s = driver.findElement(Byele).getAttribute("href");
				if (s.equalsIgnoreCase(compareText)) {
					ReportGenerator.log(LogStatus.PASS, "Link Text is verified");
				}
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Link Text is not same");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void Implicit_Wait() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	public static void maximize_Window() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.manage().window().maximize();
			// MethodDef.passLog("Window is maximized");
			ReportGenerator.log(LogStatus.PASS, "Text is entered successfully");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Window is not maximized");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static WebElement Explicit_Wait(By locator, int timeout, String Passlog, String Faillog) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);

			element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			// MethodDef.passLog("Element is visible");
			ReportGenerator.log(LogStatus.PASS, Passlog);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, Faillog);
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
		return element;

	}

	public List<String> webtable_Content(By Byele, String value) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		List<String> tableContents = null;
		try {
			tableContents = new ArrayList<String>();
			WebElement element = driver.findElement(Byele);
			List<WebElement> rowCollection = element.findElements(By.tagName("tr"));
			System.out.println("Numer of rows in this table: " + rowCollection.size());
			int i_RowNum = 1;
			for (WebElement rowElement : rowCollection) {
				List<WebElement> colCollection = rowElement.findElements(By.tagName("td"));
				int i_ColNum = 1;
				for (WebElement colElement : colCollection) {
					System.out.println("Row " + i_RowNum + " Column " + i_ColNum + " Data " + colElement.getText());
					tableContents.add(colElement.getText());
					i_ColNum = i_ColNum + 1;

				}
				i_RowNum = i_RowNum + 1;
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Record is not found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}

		return tableContents;
	}

	public List<String> webtable_selectValuebyrow(String tblLocator, String tbllocatorvalue, String row) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		List<String> tblContent = new ArrayList<String>();
		try {
			// List<WebElement> rows =
			// driver.findElements(By.xpath("//table[@class='Sample']//tr["+row+"]"));
			List<WebElement> rows = driver
					.findElements(By.xpath("//table[@" + tblLocator + "=" + tbllocatorvalue + "]//tr[" + row + "]"));
			// for every line, store both columns
			for (WebElement row1 : rows) {
				WebElement key = row1.findElement(By.tagName("td"));
				tblContent.add(key.getText());

			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Record is not found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}

		return tblContent;
	}

	public String webtable_selectValuebyRC(String tblLocator, String tbllocatorvalue, int row, int column,
			String options) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String cellValue = null;
		try {

			// List<WebElement> rows =
			// driver.findElements(By.xpath("//table[@class='Sample']//tr["+row+"]"));
			WebElement cell = (WebElement) driver.findElements(By.xpath(
					"//table[@" + tblLocator + "=" + tbllocatorvalue + "]//tr[" + row + "]//td[" + column + "]"));
			cellValue = cell.getText();
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Record is not found");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}

		return cellValue;
	}

	public static void selectFrame(By Byele, String frameselectionOption, String frameselectionValue) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			switch (frameselectionOption) {
			case "index":
				driver.switchTo().frame(Integer.parseInt(frameselectionValue));
				ReportGenerator.log(LogStatus.INFO, "Frame is selected");
				break;
			case "name":
				driver.switchTo().frame(frameselectionValue);
				ReportGenerator.log(LogStatus.INFO, "Frame is selected");
				break;
			case "webelement":
				driver.switchTo().frame(driver.findElement(Byele));
				ReportGenerator.log(LogStatus.INFO, "Frame is selected");
				break;
			}
		} catch (Exception e) {

			ReportGenerator.log(LogStatus.FAIL, "Frame is not completed");
			ReportGenerator.log(LogStatus.ERROR, "Frame is not completed" + e.getMessage());
		}

	}

	public static void defaultContent() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {

			driver.switchTo().defaultContent();

		} catch (Exception e) {

			ReportGenerator.log(LogStatus.FAIL, "defaultFrame is not completed");
			ReportGenerator.log(LogStatus.ERROR, "Frame is not completed" + e.getMessage());
		}

	}

	public static void page_scrollDown() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,"
					+ "document.body.scrollHeight,document.documentElement.clientHeight));");
			ReportGenerator.log(LogStatus.PASS, "Page Scrolldown is completed");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Page Scrolldown is not completed");
			ReportGenerator.log(LogStatus.ERROR, "Page Scrolldown is not completed" + e.getMessage());
		}
	}

	public static void scrollDown() {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0,550)", "");
			ReportGenerator.log(LogStatus.PASS, "Page Scrolldown is completed");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Page Scrolldown is not completed");
			ReportGenerator.log(LogStatus.ERROR, "Page Scrolldown is not completed" + e.getMessage());
		}
	}

	public static void parentIFrame() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.switchTo().parentFrame();
			ReportGenerator.log(LogStatus.INFO, "Switched to parent frame");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "defaultFrame is not completed");
			ReportGenerator.log(LogStatus.ERROR, "Frame is not completed" + e.getMessage());
		}
	}

	public static void scroll_Into_Element(By Byele, String value) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			ReportGenerator.log(LogStatus.PASS, "Page Scrolldown is not completed");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Page Scrolldown is not completed");
			ReportGenerator.log(LogStatus.ERROR, "Page Scrolldown is not completed" + e.getMessage());
		}
	}

	public boolean scrolling_By_CoordinatesofAPage(int x, int y) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + "," + y + "");
			ReportGenerator.log(LogStatus.PASS, "Page Scrolldown is completed");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Page Scrolldown is not completed");
			ReportGenerator.log(LogStatus.ERROR, "Page Scrolldown is not completed" + e.getMessage());
		}
		return true;
	}

	public static String captureScreen() {
		String path;
		File trgtPath = null;
		WebDriver driverLoc = DriverFactory.getCurrentDriver();
		try {
			WebDriver augmentedDriver = new Augmenter().augment(driverLoc);
			File source = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
			path = ReportGenerator.reportPath + "/" + source.getName();
			System.out.println(path);
			trgtPath = new File(path);

			FileUtils.copyFile(source, trgtPath);
		} catch (Exception e) {
			path = "Failed to capture screenshot: " + e.getMessage();
			ReportGenerator.log(LogStatus.FAIL, "Failed to capture screenshot");
			ReportGenerator.log(LogStatus.ERROR, path);
		}
		return trgtPath.getAbsolutePath();

	}

	public static void captureScreenshot() {
		String path;
		File trgtPath = null;
		WebDriver driverLoc = DriverFactory.getCurrentDriver();
		try {
			WebDriver augmentedDriver = new Augmenter().augment(driverLoc);
			File source = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
			path = ReportGenerator.reportPath + "/" + source.getName();
			System.out.println(path);
			trgtPath = new File(path);
			FileUtils.copyFile(source, trgtPath);
		} catch (Exception e) {

			ReportGenerator.log(LogStatus.FAIL, "Not able to take screenshot");
		}
	}

	public static void waitForPageLoad() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(pageLoadCondition);
		} catch (Exception e) {
		}

	}

	public String getValueOfCookieNamed(String name) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String Cookie_name = null;
		try {
			Cookie_name = driver.manage().getCookieNamed(name).getValue();
			ReportGenerator.log(LogStatus.PASS, "Cookie value get completed");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Cookie value is not get completed");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
		return Cookie_name;

	}

	public static void addCookie(String name, String value, String domain, String path, Date expiry) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.manage().addCookie(new Cookie(name, value, domain, path, expiry));
			ReportGenerator.log(LogStatus.PASS, "Cookie is added");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Cookie is not added");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}

	}

	public static void deleteAllCookies() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.manage().deleteAllCookies();
			ReportGenerator.log(LogStatus.PASS, "Cookie is deleted");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Cookie is not deleted");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void deleteCookieNamed(String name) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.manage().deleteCookieNamed(name);
			ReportGenerator.log(LogStatus.PASS, "Cookie deleted by name");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Cookie is not deleted by name");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public Set<Cookie> getAllCookies() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		Set<Cookie> Cookie_name = null;
		try {
			Cookie_name = driver.manage().getCookies();
			for (Cookie cookiename : Cookie_name) {
				ReportGenerator.log(LogStatus.PASS, "Cookies Name" + cookiename.getName() + "Cookies Name"
						+ cookiename.getValue() + "" + cookiename.getExpiry());
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Not able to retrieve the cookies");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}

		return Cookie_name;
	}

	public static String addCred(String pageURL) {
		if ((pageURL.contains("dev.staging.")) || (pageURL.contains("ref.staging."))) {
			pageURL = pageURL.replace("http://", "http://ed:MJotWs4n@");
		}
		return pageURL;
	}

	public static void waitAndClick(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator)).click();
			ReportGenerator.log(LogStatus.INFO, PassComments);
			Thread.sleep(3000);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void waitAndClick(By locator, int timeOut) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "The error message is " + e.getMessage());
		}
	}

	public static void waitAndClickable(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
			ReportGenerator.log(LogStatus.INFO, PassComments);
			Thread.sleep(3000);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void waitAndClickByJavaScript(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			Thread.sleep(5000);
			WebElement element = driver.findElement(locator);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click()", element);
			ReportGenerator.log(LogStatus.INFO, PassComments);
			Thread.sleep(3000);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void waitForClickable(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			ReportGenerator.log(LogStatus.INFO, PassComments);
			Thread.sleep(3000);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static int getXPATHCount(By locator, int timeOut) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		int count = 0;

		try {
			List<WebElement> list = driver.findElements(locator);
			count = list.size();

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL,
					"Failed to retrieve count" + ". <br> The error message is " + e.getMessage());
		}
		return count;
	}

	public static String getTagName(By locator, int timeOut) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String name = null;
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			name = wait.until(ExpectedConditions.elementToBeClickable(locator)).getTagName();

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL,
					"Failed to retrieve tagname" + ". <br> The error message is " + e.getMessage());
		}
		return name;
	}

	public static void waitForVisiblity(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			ReportGenerator.log(LogStatus.INFO, PassComments);
			Thread.sleep(3000);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static String getYear() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		String yearInString = String.valueOf(year);
		return yearInString;
	}

	public static String getMonth() {
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat("MMMM").format(cal.getTime());
	}

	public static String getDay() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);

		DecimalFormat mFormat = new DecimalFormat("00");
		return mFormat.format(Double.valueOf(day));
	}

	public static void waitAndLocate(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			ReportGenerator.log(LogStatus.PASS, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void waitAndVerify(By locator, String value, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		String webText;
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			WebElement element = driver.findElement(locator);
			webText = element.getText();
			if (webText.equalsIgnoreCase(value) == true) {
				ReportGenerator.log(LogStatus.INFO, PassComments + ", Retrieved - " + webText + ", Expected - " + value);
			} else {
				ReportGenerator.log(LogStatus.WARNING, FailComments + ", Retrieved - " + webText + ", Expected - " + value);
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void waitForElementTodisappear(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			ReportGenerator.log(LogStatus.PASS, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void waitAndrightClick(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			Actions a1 = new Actions(driver);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			a1.contextClick(driver.findElement(locator)).build().perform();

			ReportGenerator.log(LogStatus.INFO, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ".<br> The error message is " + e.getMessage());
		}
	}

	public static void openURL(String URL, String passComments, String failComments) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			driver.navigate().to(URL);
			ReportGenerator.log(LogStatus.PASS, passComments + " -- " + URL);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, failComments + " -- " + URL);
		}
	}

	public static void enter_Text(By locator, String text, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(locator);
			element.clear();
			element.sendKeys(text);
			ReportGenerator.log(LogStatus.INFO, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void enter_Text_HTML_BODY(By locator, String text, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(locator);
			// element.clear();
			element.sendKeys(text);
			ReportGenerator.log(LogStatus.PASS, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void click(By locator, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(locator);
			element.click();
			ReportGenerator.log(LogStatus.PASS, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void switchToiFrame(By Byele, String PassComments, String FailComments) {

		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement element = driver.findElement(Byele);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			driver.switchTo().frame(element);
			ReportGenerator.log(LogStatus.INFO, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.ERROR, FailComments);
		}
	}

	public static void contains_Text(By locator, String conText, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String text = null;
		try {
			WebElement element = driver.findElement(locator);
			text = element.getText();
			if (text.contains(conText) == true) {
				ReportGenerator.log(LogStatus.PASS, PassComments + text);
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + " The excepted text is " + conText + " but in actual text is "
					+ text + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void contains_Text(By locator1, By locator2, String conText, String PassComments,
			String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String text1 = null, text2 = null;
		try {
			WebElement element1 = driver.findElement(locator1);
			text1 = element1.getText();
			if (text1.contains(conText) == true) {
				ReportGenerator.log(LogStatus.PASS, PassComments + element1.getText());
			}
		} catch (Exception e) {
			try {
				WebElement element2 = driver.findElement(locator2);
				text2 = element2.getText();
				if (text2.contains(conText) == true) {
					ReportGenerator.log(LogStatus.PASS, PassComments + element2.getText());
				}
			} catch (Exception e1) {
				ReportGenerator.log(LogStatus.FAIL, FailComments + " The excepted text is " + conText
						+ " but in actual text is " + text2 + ". <br> The error message is " + e1.getMessage());
			}

		}
	}

	public static void contains_Text_Attribute(By locator, String attrname, String conText, String PassComments,
			String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String text = null;
		try {
			String value = driver.findElement(locator).getAttribute(attrname);

			if (value.contains(conText) == true) {
				ReportGenerator.log(LogStatus.PASS, PassComments + conText + " Text");
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + " The excepted text is " + conText + " but in actual text is "
					+ text + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void contains_Text_Attribute(By locator, String attrname, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String text = null;
		String value = driver.findElement(locator).getAttribute(attrname);
		try {
			if (!value.isEmpty()) {
				ReportGenerator.log(LogStatus.PASS, PassComments + value);
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL,
					FailComments + " actual text is " + value + ". <br> The error message is " + e.getMessage());
		}
	}

	public static String getTextFromAttribute(By locator, String attr, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String value = null;
		try {
			value = driver.findElement(locator).getAttribute(attr);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + " The error message is " + e.getMessage());
		}
		return value;
	}

	public static String getTextFromAttribute(By locator, String attr, int timeOut) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		String value = null;
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			value = driver.findElement(locator).getAttribute(attr);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Failed to retrieve tagname" + " The error message is " + e.getMessage());
		}
		return value;
	}

	public static String getTextFromElement(By locator, int timeOut) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		String value = null;
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			value = driver.findElement(locator).getText();
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Failed to retrieve tagname" + " The error message is " + e.getMessage());
		}
		return value;
	}

	public static void selectValue(By locator, String method, String selectText, String passComments,
			String failComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			Select se = new Select(driver.findElement(locator));
			switch (method) {
			case "index":
				se.selectByIndex(Integer.parseInt(selectText));
				ReportGenerator.log(LogStatus.PASS, passComments);
				break;
			case "value":
				se.selectByValue(selectText);
				ReportGenerator.log(LogStatus.PASS, passComments);
				break;
			case "visibletext":
				se.selectByVisibleText(selectText);
				ReportGenerator.log(LogStatus.PASS, passComments);
				break;
			}

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, failComments);
		}
	}

	public static String getSelectOptionValue(By locator) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement option = null;
		try {
			Select se = new Select(driver.findElement(locator));
			option = se.getFirstSelectedOption();
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Exception caught on getting options from Select box");
		}

		return option.getText();

	}

	public static void clickNValidate(By locator, String expected) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		// waitAndClick(locator, 10);
		ReportGenerator.log(LogStatus.INFO, "Element  : " + locator.toString());
		driver.findElement(locator).click();
		SleepSeconds(5);
		String actual = "Notclickable";
		if (driver.findElements(locator).size() == 0) {
			actual = "clickable";
			Back();
			SleepSeconds(5);
		}
		if (expected.equals(actual)) {
			ReportGenerator.log(LogStatus.PASS, "The element click to action is as expected : " + expected);
		} else {
			ReportGenerator.log(LogStatus.FAIL, "Expected is : " + expected + ", But actual is : " + actual);
		}
	}

	// Author : Jeyabal
	public static void SleepSeconds(int timeout) {
		try {
			Thread.sleep(timeout * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void Back() {
		AndroidDriver<WebElement> driver = (AndroidDriver<WebElement>) DriverFactory.getCurrentDriver();
		driver.pressKeyCode(AndroidKeyCode.BACK);
	}

	public static void uploadFile(String assetName) throws
	InterruptedException, AWTException, IOException {

		File resourcesDirectory = new File("resources");
		System.out.println(resourcesDirectory.getCanonicalPath());
		String filename = resourcesDirectory.getCanonicalPath() + "\\Images\\" +
				assetName;
		setClipboardData(filename);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(5000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(10000);
		ReportGenerator.log(LogStatus.PASS, "Selecting celum asset completed....");

	}

	//	public static void uploadFile(String assetName) throws InterruptedException, AWTException, IOException {
	//		String[] resourcesFolder = { "resources", "resourcesIE" };
	//		for (int i = 0; i < resourcesFolder.length; i++) {
	//			File resourcesDirectory = new File(resourcesFolder[i]);
	//			System.out.println(resourcesDirectory.getCanonicalPath());
	//			String filename = resourcesDirectory.getCanonicalPath() + "\\Images\\" + assetName;
	//			setClipboardData(filename);
	//		}
	//
	//		Robot robot = new Robot();
	//		robot.keyPress(KeyEvent.VK_CONTROL);
	//		robot.keyPress(KeyEvent.VK_V);
	//		robot.keyRelease(KeyEvent.VK_V);
	//		robot.keyRelease(KeyEvent.VK_CONTROL);
	//		Thread.sleep(5000);
	//		robot.keyPress(KeyEvent.VK_ENTER);
	//		robot.keyRelease(KeyEvent.VK_ENTER);
	//		Thread.sleep(10000);
	//		ReportGenerator.log(LogStatus.PASS, "Selecting celum asset completed....");
	//
	//	}

	public static void setClipboardData(String string) {
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

	public static void scroll_Until_Found(By ele, int scroll_no) throws InterruptedException {

		int scroll_tmp = 0;
		WebDriver driver = DriverFactory.getCurrentDriver();
		Implicit_Wait(1);

		do {
			Thread.sleep(4000);
			WebElement element = driver.findElement(By.xpath("html/body/div[1]/div[3]/div/div"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = -1 >>> 1", element);
			scroll_tmp++;
			System.out.println("Scroll time ......  " + scroll_tmp);
			if (isDisplayed(ele)) {
				WebElement pic = driver.findElement(ele);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", pic);
				waitAndClick(ele, 10, "Elemet clicked after scrolling", "Failed to click ele in scroll");
				break;
			}
		} while (scroll_tmp != scroll_no);
		Implicit_Wait(30);
	}

	// Author : Jeyabal
	public static void waitForElement(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			ReportGenerator.log(LogStatus.PASS, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ".<br> The error message is " + e.getMessage());
		}
	}

	public static void containsText(By locator, String conText, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String text = null;
		try {
			WebElement element = driver.findElement(locator);
			text = element.getText();
			if (text.contains(conText) == true) {
				ReportGenerator.log(LogStatus.PASS, PassComments + conText + "Text");
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + " expected contains text is " + conText
					+ " but in actual text is " + text + ".<br> The error message is " + e.getMessage());
		}
	}

	public static int GetYDistance(By locator1, By locator2) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		int yDistance;
		yDistance = 0;
		try {
			WebElement elem1 = driver.findElement(locator1);
			WebElement elem2 = driver.findElement(locator2);

			int e1_y = elem1.getLocation().y;
			int e1_height = elem1.getSize().height;
			int e2_y = elem2.getLocation().y;
			int e1_bot_loc = e1_y + (int) (e1_height * 1.3);

			yDistance = e2_y - e1_bot_loc;
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, e.getLocalizedMessage());
		}
		return yDistance;
	}

	public static boolean isElementPresentOrNot(By locator) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(locator);
			element.isDisplayed();
			System.out.println("Element Present : " + locator.toString());
			return true;
		} catch (Exception e) {
			System.out.println("Element Not Present : " + locator.toString());
			return false;
		}
	}

	public static boolean isElemTextMatches(By locator, String compareText) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		String text = null;
		try {
			WebElement element = null;
			if (driver.findElements(locator).size() == 2) {
				element = driver.findElements(locator).get(1);
			} else {
				element = driver.findElements(locator).get(0);
			}

			text = element.getText();
			System.out.println("Found Heading : \"" + text + "\"");
			System.out.println("Expected Heading : \"" + compareText + "\"");
			if (text.equals(compareText)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * public static String getAttribute(WebElement elem, String attribute) {
	 * return elem.getAttribute(attribute); }
	 */
	public static String getAttribute(By locator, String attribute) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement elem = driver.findElement(locator);
		return elem.getAttribute(attribute);
	}

	public static String getText(By locator, int timeOut) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getText();
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, ".<br> The error message is " + e.getMessage());
			return null;
		}
	}

	public static void waitForElementByDriver(By locator, int timeOut, String PassComments, String FailComments,
			WebDriver mobileDriver) {
		WebDriver driver = mobileDriver;
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			ReportGenerator.log(LogStatus.INFO, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ".<br> The error message is " + e.getMessage());
		}
	}

	public static String captureScreen(WebDriver driver) {
		// WebDriver driver = DriverFactory.getCurrentDriver();
		String path;
		File trgtPath = null;
		WebDriver driverLoc = driver;
		try {
			WebDriver augmentedDriver = new Augmenter().augment(driverLoc);
			File source = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
			// java.util.Date date= new java.util.Date();
			// Timestamp tstamp = new Timestamp(date.getTime());
			// path = ConfigProp.getPropertyValue("SSPath") + source.getName();
			path = ReportGenerator.reportPath + "/" + source.getName();
			System.out.println(path);
			trgtPath = new File(path);

			FileUtils.copyFile(source, trgtPath);
		} catch (Exception e) {
			path = "Failed to capture screenshot: " + e.getMessage();
			ReportGenerator.log(LogStatus.FAIL, "Failed to capture screenshot");
			ReportGenerator.log(LogStatus.ERROR, path);
		}
		return trgtPath.getAbsolutePath();

	}

	public static void Implicit_Wait(int timeout) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	public static void checkallpageload() {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, 120);

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
					"body > img,#header-main > div > a,.metadata-header__logo,img._2Lgc-,.zn-header__text-page_header,.logo-style,div.media__video,.zn-header__text-page_header,.nav__logo,a#logo,.money-logo,a#br-logo,a.logo-entertainment,div.nav__container #logo")));
			ReportGenerator.log(LogStatus.INFO, "Page is loaded");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.WARNING, "Page did not load on time");

		}
	}

	public static void scrollIntoViewByElement(By element) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		for (int i = 0; i < 20; i++) {
			List<WebElement> elementw = driver.findElements(element);
			if (elementw.size() == 0) {
				jse.executeScript("window.scrollBy(0,550)", "");
				continue;
			} else {
				jse.executeScript("arguments[0].scrollIntoView()", elementw.get(0));
				break;
			}
		}
	}

	public static WebElement findElementXpath(String xpath) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		return driver.findElement(By.xpath(xpath));
	}

	public static void moveToElementClick(By Byele) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement element = driver.findElement(Byele);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().build().perform();
	}

	public static void moveToElementClick(WebElement element) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().build().perform();
	}

	/**
	 * Moves the Mouse pointer to the element (Mouse Over)
	 * 
	 * @param element
	 *            Element to be mouse hovered
	 * @param go
	 *            Web Driver Instances
	 */
	public static void moveToElement(By Byele) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).build().perform();
	}

	/**
	 * Send Keys to Element - Triggered through Javascript - Useful when
	 * elements having Javascript events
	 * 
	 * @param element
	 * @param string
	 */
	public static void sendKeysJavaScript(By Byele, String string) {

		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].setAttribute('value', '" + string + "')", element);
	}

	public static void sendKeys(By Byele, Keys Key) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);

		element.sendKeys(Key);
	}

	public static void sendKeys(By Byele, String Key) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);

		element.sendKeys(Key);
	}

	public static void clickJavaScript(By Byele) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public static void clickJavaScript(By Byele, String pc, String fc) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			ReportGenerator.log(LogStatus.PASS, pc);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, fc);
		}
	}

	public static String isdevice() {
		return ConfigProvider.getConfig("Platform");
	}

	public static String environment() {
		String Environment = System.getProperty("environment");

		String Env[] = Environment.split("_");

		return Env[0].trim();
	}

	public static String Site() {
		String Environment = System.getProperty("environment").trim();

		String Env[] = Environment.split("_");

		return Env[1].trim();
	}

	public static void closeTermsOfService(By Byele) {

		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if (!element.isDisplayed())
			try {
				wait.until(ExpectedConditions.elementToBeClickable(element));
			} catch (Exception e) {
				// error.addError("TEST");
			}
		try {
			// li.click();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			ReportGenerator.log(LogStatus.INFO, "Terms of Service was present & closed");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, "Terms of Service is not present");
		}

	}

	public static void selectByVisibleText(By Byele, String visibleText) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement element = driver.findElement(Byele);

		Select drop = new Select(element);
		drop.selectByVisibleText(visibleText);
	}

	public static void switchToDefaultContent() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		driver.switchTo().defaultContent();
	}

	public static void switchToFrame(By Byele) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebElement Frame_Element = driver.findElement(Byele);

		driver.switchTo().frame(Frame_Element);
	}

	/**
	 * Switches focus of the webdriver to a particular frame in the page
	 * 
	 * @param go
	 * @param Frame_id
	 */
	public static void switchToFrame(String Frame_id) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		driver.switchTo().frame(Frame_id);
	}

	/**
	 * Switches webdriver focus to primary (base) window
	 * 
	 * @param go
	 *            webdriver instance
	 */
	public static void switchToPrimaryWindow() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		driver.switchTo().window(driver.getWindowHandle());

	}

	/**
	 * Switches webdriver focus to last opened wev browser window
	 * 
	 * @param go
	 *            webdriver instance
	 */
	public static void switchToLastWindow() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		Set<String> winHandles = driver.getWindowHandles();
		for (String winHandle : winHandles)
			driver.switchTo().window(winHandle);
	}

	private static JsonObject extractObject(HttpResponse resp) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		StringBuffer s = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			s.append(line);
		}
		rd.close();
		JsonParser parser = new JsonParser();
		JsonObject objToReturn = (JsonObject) parser.parse(s.toString());
		// System.out.println(objToReturn.toString());
		// System.out.println(objToReturn.get("proxyId"));
		return objToReturn;
	}

	public static WebElement findElementCss(String csspath) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		return driver.findElement(By.cssSelector(csspath));
	}


	public static void theoplayerelementpresent() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
				"//div[contains(@class,'vjs-subtitles-button vjs-menu-button vjs-control')][@style='display: block;']")));
	}

	public static void textNotEmpty(By Byele, String passfailmsg) {

		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement element = driver.findElement(Byele);

		try {
			if (!element.getText().isEmpty())

				ReportGenerator.log(LogStatus.PASS, passfailmsg + " is present");
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, passfailmsg + " is not present");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}

	public static void scrollUp() {
		WebDriver driver = DriverFactory.getCurrentDriver();

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,-550)", "");
	}

	public static WebElement getWebElement(By byele) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		return driver.findElement(byele);
	}

	public static void clearEleText(By locator, String pc, String fc) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			driver.findElement(locator).clear();
			ReportGenerator.log(LogStatus.PASS, pc);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.PASS, fc);
		}
	}

	public static void waitPresenceOfEleLocated(By locator, int timeOut) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public static boolean isDisplayed(By by, String pc, String fc) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			List<WebElement> element = driver.findElements(by);
			if (element.isEmpty()) {
				ReportGenerator.log(LogStatus.FAIL, fc);
				return false;
			} else {
				ReportGenerator.log(LogStatus.PASS, pc);
				return true;
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
			return false;
		}
	}

	public static String getCssValue(By by, String cssValue) {

		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			WebElement element = driver.findElement(by);
			return element.getCssValue(cssValue);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
			return null;
		}
	}

	public static void verifyCssValue(By by, String cssValue, String Contains, String PassFail) {

		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			WebElement element = driver.findElement(by);
			if (element.getCssValue(cssValue).contains(Contains)) {
				ReportGenerator.log(LogStatus.PASS, PassFail + " is present");
			} else {
				ReportGenerator.log(LogStatus.FAIL, PassFail + " is not present");

			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static List<WebElement> getCookies(By Byele) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			List<WebElement> element = driver.findElements(Byele);
			if (element.isEmpty()) {
				ReportGenerator.log(LogStatus.FAIL, "No Cookies");
				return null;
			}
			return element;
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "No Cookies");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
			return null;
		}
	}

	public static void contains_Text(WebElement element, String conText) {
		try {
			if (element.getText().contains(conText) == true) {
				ReportGenerator.log(LogStatus.PASS, "Elements contains" + conText + "Text");
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, "Elements doex not contains" + conText + "Text");
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}
	public static boolean validateJSErrors() {

		boolean errorFoundJavaScripts = false;
		ExpectedCondition e = new ExpectedCondition() {
			public Boolean apply(Object d) {
				try {
					JavascriptExecutor js = (JavascriptExecutor) d;
					Boolean isReady = (Boolean) js.executeScript("return AdfPage.PAGE.isSynchronizedWithServer()");
					return isReady;
				} catch (WebDriverException e) {
					// somthingwrongbutidontcare
					ReportGenerator.log(LogStatus.FAIL, "JAVA SCRIPT is not synchronised with the server" + e.getMessage());
					return false;
				}
			}
		};

		if (!errorFoundJavaScripts) {
			ReportGenerator.log(LogStatus.PASS, "No JavaScript Errors found on this page");
		}

		return errorFoundJavaScripts;
	}

	public static void waitElemToBeClickable(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			ReportGenerator.log(LogStatus.INFO, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static void expectedResult(By by, String pc, String fc) {
		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			WebElement element = driver.findElement(by);
			element.click();
			ReportGenerator.log(LogStatus.PASS, pc);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, fc);
		}
	}

	public static void waitForVisiblity(By locator, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, 30);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			ReportGenerator.log(LogStatus.PASS, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}

	public static boolean isElementPresentOrNot(By locator, String pc, String fc) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(locator);
			element.isDisplayed();
			System.out.println("Element Present : " + pc);
			return true;
		} catch (Exception e) {
			System.out.println("Element Not Present : " + fc);
			return false;
		}
	}

	public static void assertHasAttribute(WebElement element, String attr, String content, String passmessage,
			String errmessage) {
		if (content.startsWith("//") && attr.equals("href")) {
			content = "http:" + content;
		}
		if (element.getAttribute(attr).endsWith(content) || (element.getAttribute(attr) + "/").endsWith(content)
				|| (content).endsWith(element.getAttribute(attr) + "/")
				|| (content).endsWith(element.getAttribute(attr))) {
			ReportGenerator.log(LogStatus.PASS, passmessage);
		} else {
			ReportGenerator.log(LogStatus.FAIL, errmessage + " content " + content + " href " + element.getAttribute(attr));
		}

	}

	public static void textNotEmpty(By Byele, String passfailmsg, String fc) {

		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement element = driver.findElement(Byele);

		try {
			if (!element.getText().isEmpty())

				ReportGenerator.log(LogStatus.PASS, passfailmsg);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, fc);
			ReportGenerator.log(LogStatus.INFO, e.getMessage());

		}
	}


	public static void checkElementClickable(By locator, int timeOut, String PassComments, String FailComments) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			ReportGenerator.log(LogStatus.INFO, PassComments);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + ". <br> The error message is " + e.getMessage());
		}
	}


	public static void contains_Text_Attribute(WebElement element, String attrname, String conText, String PassComments,
			String FailComments) {

		String text = null;
		try {
			String value = element.getAttribute(attrname);

			if (value.contains(conText) == true) {
				ReportGenerator.log(LogStatus.PASS, PassComments + conText + " Text");
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, FailComments + " The excepted text is " + conText + " but in actual text is "
					+ text + ". <br> The error message is " + e.getMessage());
		}
	}





	public static void clickJavaScript(WebElement Byele) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		// WebElement element = driver.findElement(Byele);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", Byele);
	}

	public static void moveToElement(WebElement element) {
		WebDriver driver = DriverFactory.getCurrentDriver();

		Actions actions = new Actions(driver);
		actions.moveToElement(element).build().perform();
	}

	public static void conditionVerify(Boolean element, String Passmsg, String Failmsg) {
		try {
			if (element == true)
				ReportGenerator.log(LogStatus.PASS, Passmsg);
			else
				ReportGenerator.log(LogStatus.FAIL, Failmsg);
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void scrollToPageBottom() {
		JavascriptExecutor jse = (JavascriptExecutor) DriverFactory.getCurrentDriver();
		jse.executeScript(
				"window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
	}

	public static boolean isDispWE(WebElement element, String pc, String fc) {
		try {
			if (!element.isDisplayed()) {
				ReportGenerator.log(LogStatus.FAIL, fc);
				return false;
			} else {
				ReportGenerator.log(LogStatus.PASS, pc);
				return true;
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
			return false;
		}
	}

	public static void verifyNotCssValue(By by, String cssValue, String Contains, String PassFail) {

		try {
			WebDriver driver = DriverFactory.getCurrentDriver();
			WebElement element = driver.findElement(by);
			if (!element.getCssValue(cssValue).contains(Contains)) {
				ReportGenerator.log(LogStatus.PASS, PassFail + " is not present");
			} else {
				ReportGenerator.log(LogStatus.FAIL, PassFail + " is present");

			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void hasContent(By element, String passmessage, String errmessage) {
		try {
			if (!getWebElement(element).getText().isEmpty()) {
				ReportGenerator.log(LogStatus.PASS, passmessage);
			} else {
				ReportGenerator.log(LogStatus.FAIL, errmessage);
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static void startWith(By Byele, String value, String pc, String fc) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			if (element.getText().startsWith(value) == true) {
				ReportGenerator.log(LogStatus.PASS, pc);

			}

		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, fc);
			ReportGenerator.log(LogStatus.INFO, e.getMessage());
		}
	}

	public static WebElement getfirstvisibleElement(List<WebElement> elements) {
		WebElement element = null;
		try {
			if (elements.size() != 0) {
				for (int i = 0; i < elements.size(); i++) {
					try {
						if (elements.get(i).isDisplayed()) {
							element = elements.get(i);
							break;
						}
					} catch (StaleElementReferenceException e) {

					}
				}
				if (element == null)

					throw new NullPointerException("Specified WebElement is not displayed in current page");
			} else
				throw new NullPointerException("Specified WebElement is not available");
		} catch (NullPointerException ne) {
			ReportGenerator.log(LogStatus.FAIL, ne.getMessage());
		}
		return element;

	}

	public static void scrollDown1000(WebDriver go) {

		JavascriptExecutor jse = (JavascriptExecutor) go;
		jse.executeScript("window.scrollBy(0,1000)", "");
	}

	public static void failLog(String string) {
		ReportGenerator.log(LogStatus.FAIL, string);
	}
	public static void Radiobuttonselected(By Byele, String Pass, String Fail) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		try {
			WebElement element = driver.findElement(Byele);
			if (element.isSelected()) {
				ReportGenerator.log(LogStatus.PASS, Pass);
			} else {
				ReportGenerator.log(LogStatus.FAIL, Fail);
			}
		} catch (Exception e) {
			ReportGenerator.log(LogStatus.FAIL, e.getMessage());

		}
	}

	public static void cookieclear(){
		WebDriver driver = DriverFactory.getCurrentDriver();
		driver.navigate().to("https://stage.www-m.cnn.com/debug/set-speedtrap/index.html");
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys("1");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
	}

	public static void scroll_Into_Element(By locator) {
		WebDriver driver = DriverFactory.getCurrentDriver();
		WebElement element = null;
		for (int iloop = 1; iloop <= 50; iloop++) {
			try {
				element = driver.findElement(locator);
				System.out.println(element.isDisplayed());
				ReportGenerator.log(LogStatus.INFO, "Page Scrolldown completed");
				break;
			} catch (Exception e) {				
				((JavascriptExecutor) driver).executeScript("window.scroll(0,2000);");

			}
		}
	}
}