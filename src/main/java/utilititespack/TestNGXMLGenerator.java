package utilititespack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestNGXMLGenerator {

	public static void main(String[] args) {
		System.out.println("******Inside TestNG Xml Creation******");

	        XmlSuite suite = new XmlSuite();
	        suite.setName("AgriPal");
	        suite.setThreadCount(10);

	        XmlTest test = new XmlTest(suite);
	        test.setName("Test");
	        test.setPreserveOrder("true");

	        XmlClass testClass = null;


	        ArrayList<XmlClass> classes = new ArrayList<XmlClass>();
	        ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();

	        testClass = new XmlClass();
	        testClass.setName("basttestpack.BaseClass");
	        methodsToRun.add(new XmlInclude("homepageValidation"));

	        testClass.setIncludedMethods(methodsToRun);
	        classes.add(testClass);
	        test.setXmlClasses(classes);

	        test.setXmlClasses(classes);

	        File file = new File("TestNG.xml");

	        FileWriter writer;
			try {
				writer = new FileWriter(file);
				writer.write(suite.toXml());
		        writer.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        		

			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			suites.add(suite);
			TestNG tng = new TestNG();
			tng.setXmlSuites(suites);
			tng.run();
	}

}
