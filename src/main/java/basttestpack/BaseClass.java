package basttestpack;
import java.io.IOException;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import alldriverspack.DriverFactory;
import alltestspack.HomePageValidations;
import utilititespack.ReportGenerator;

public class BaseClass {
	
	@BeforeSuite(alwaysRun = true)
	public void presteps(ITestContext ctx) throws IOException
	{
		ReportGenerator.initReport();
	}
	

	@Test
	public static void homepageValidation() {
		ReportGenerator.startlogger("Home Page Validation");
		HomePageValidations hm = new HomePageValidations();		
		hm.homepageValidation();
	}
	
	
	
	@AfterSuite(alwaysRun = true)
	public void poststeps(ITestContext ctx) 
	{
		ReportGenerator.endReport();
		DriverFactory.driverCleanUp();
		
	}
	
}
