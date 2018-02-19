package alldriverspack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utilititespack.ConfigProp;
import utilititespack.ReportGenerator;
import utilititespack.WrapperMethods;

public class test {
	public static ExtentReports report;
	public static ExtentTest logger;
	public static String reportPath;

	@BeforeTest
	public static void initReport(){
		String fileName = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
		reportPath = "./All_Reports/" + "Report_" + fileName;
		report = new ExtentReports (reportPath +"/ExtentReport.html", true);
		report.config().documentTitle("Automation Report");
		report.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
		report.config().reportHeadline("Headline");
		report.config().reportName("Report Name");
		
	}
	
	@Test
	public static void info()
	{
		logger=report.startTest("HomePage Validation");
		logger.log(LogStatus.INFO, "Info");
	}
	public static void log(LogStatus logStatus, String testName, String stepName)
	{
		String SSValue=ConfigProp.getPropertyValue("ScreenShotPASS");
		String path="";
		logger=report.startTest(testName);
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
			logger.log(LogStatus.INFO, "Info" +stepName);
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
		report.endTest(logger);
		report.flush();
	}

	@AfterTest
	public static void endReport(){
		report.flush();
		report.close();
    }

}
