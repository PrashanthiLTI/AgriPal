package utilititespack;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ReportGenerator {	
	public static ExtentReports report;
	public static ExtentTest logger;
	public static String reportPath;

	public static void initReport(){
		String fileName = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
		reportPath = "./All_Reports/" + "Report_" + fileName;
		report = new ExtentReports (reportPath +"/ExtentReport.html", true);
		report.config().documentTitle("Automation Report");
		report.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
		report.config().reportHeadline("Headline");
		report.config().reportName("Report Name");

	}

	public static void startlogger(String testName)
	{
		logger = report.startTest(testName);
	}

	public static void log(LogStatus logStatus, String stepName)
	{
		String SSValue=ConfigProp.getPropertyValue("ScreenShotPASS");
		String path="";
		switch (logStatus) {
		case PASS:
			if(SSValue.equalsIgnoreCase("YES")){
				path = WrapperMethods.captureScreen();				
				logger.log(logStatus, stepName + logger.addScreenCapture(path));
			}else{
				logger.log(logStatus, stepName);
			}
			break;

		case FAIL:
			path = WrapperMethods.captureScreen();	
			logger.log(logStatus, stepName + logger.addScreenCapture(path));
		case INFO:
		case SKIP:
		case WARNING:
			logger.log(LogStatus.INFO, stepName);
			break;
		case ERROR:
		case FATAL:
		case UNKNOWN:
			path = WrapperMethods.captureScreen();
			logger.log(logStatus, stepName + logger.addScreenCapture(path));
			break;
		default:			
			break;
		}
	}
	public void getResult(ITestResult result){
		if(result.getStatus() == ITestResult.FAILURE){
			logger.log(LogStatus.FAIL, "Test Case Failed is "+result.getName());
			logger.log(LogStatus.FAIL, "Test Case Failed is "+result.getThrowable());
		}else if(result.getStatus() == ITestResult.SKIP){
			logger.log(LogStatus.SKIP, "Test Case Skipped is "+result.getName());
		}
	
	}


	public static void endReport(){
		report.flush();
		//		report.close();
	}
} 




