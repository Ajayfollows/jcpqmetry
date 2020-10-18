package com.jcp.automation.common.listeners;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import com.jcp.automation.common.utils.DBUtil;
import com.jcp.transformer.automation.util.StringUtil;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.client.TestNGScenario;

import org.testng.*;
import org.testng.xml.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



public class TestNGListener implements ISuiteListener, ITestListener, IReporter {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	ArrayList<String> failedOrSkippedMethods = new ArrayList<String>();
	ArrayList<String> passedMethods = new ArrayList<String>();
	ArrayList<String> failedMethods = new ArrayList<String>();
	ArrayList<String> skippedMethods = new ArrayList<String>();
	String accounts="";
	private void generateTestFailedXML(ITestContext context) throws IOException {
		XmlSuite suite = new XmlSuite();
		suite.setName(context.getSuite().getName());
		suite.setParallel("false");
		suite.setThreadCount(1);

		XmlTest test = new XmlTest(suite);
		test.setName(context.getName());
		test.setPreserveOrder("true");

		XmlMethodSelectors methodSelectors = new XmlMethodSelectors();
		XmlMethodSelector methodSelector = new XmlMethodSelector();
		methodSelectors.setMethodSelector(methodSelector);

		XmlScript xmlScript = new XmlScript();
		xmlScript.setLanguage("beanshell");
		String scriptDataPart = "";
		for (String methodFailedOrSkipped : failedOrSkippedMethods) {
			if (StringUtil.isNotEmpty(scriptDataPart)) {
				scriptDataPart = scriptDataPart + "||";
			}
			scriptDataPart =
					scriptDataPart
							+ String.format(
									"testngMethod.getMethodName().equalsIgnoreCase(\"%s\")",
									methodFailedOrSkipped);
		}
		xmlScript.setScript(scriptDataPart);
		methodSelector.setScript(xmlScript);
		ArrayList<XmlMethodSelector> selectors = new ArrayList<>();
		selectors.add(methodSelector);
		test.setMethodSelectors(selectors);
		XmlClass testClass = null;

		ArrayList<XmlClass> classes = new ArrayList<XmlClass>();
		ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();

		testClass = new XmlClass();
		testClass.setName("com.qmetry.qaf.automation.step.client.text.BDDTestFactory");

		testClass.setIncludedMethods(methodsToRun);
		classes.add(testClass);
		test.setXmlClasses(classes);

		methodsToRun.clear();

		test.setXmlClasses(classes);

		File file =
				new File(context.getOutputDirectory().replace(
						context.getSuite().getName(), "")
						+ "testng-failed-qas.xml");
		System.out.println("file" + file);

		FileWriter writer = new FileWriter(file);
		writer.write(suite.toXml());
		writer.close();
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out
				.println(ConfigurationManager.getBundle().getProperty("run-parameters"));
		System.out.println(ConfigurationManager.getBundle().subset("env"));
		System.out.println(ConfigurationManager.getBundle().subset("env"));
		System.setProperty("env.run.properties",
				ConfigurationManager.getBundle().subset("env").toString());
		try {
			generateTestFailedXML(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void onStart(ISuite suite) {
 
		try {
		//	Connection dbConnection=DBUtil.getDBConnection1();
			//getBundle().setProperty("DBConnection", dbConnection);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
	
	@Override
	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onStart(ITestContext context) {
		int count =
				Integer.parseInt(ConfigurationManager.getBundle().getString(
						"thread.count", "1"));
		context.getSuite().getXmlSuite().setThreadCount(count);
		if (ConfigurationManager.getBundle().getInt("thread.count", 1) > 1)
			context.getSuite().getXmlSuite().setParallel("methods");
		else context.getSuite().getXmlSuite().setParallel("false");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate todayDate = LocalDate.now();
		if(ConfigurationManager.getBundle().getString("test.environment").equalsIgnoreCase("prod")) {
			String fromDate= dtf.format(LocalDate.parse(todayDate.minusDays(10).toString())).toString();
			
			String toDate= dtf.format(todayDate).toString();
			ConfigurationManager.getBundle().setProperty("date.search.criteria"," AND to_char(yoh.order_date,'YYYYMMDD') > '"
											+fromDate+"' AND to_char(yoh.order_date,'YYYYMMDD') < '"+toDate+"'");
		}else {
			String fromDate= dtf.format(LocalDate.parse(todayDate.minusMonths(9).toString())).toString();
			String toDate= dtf.format(todayDate).toString();
			ConfigurationManager.getBundle().setProperty("date.search.criteria"," AND to_char(yoh.order_date,'YYYYMMDD') > '"
											+fromDate+"' AND to_char(yoh.order_date,'YYYYMMDD') < '"+toDate+"'");
		}
		
		//for OTDB
		/*String fromDate= dtf.format(LocalDate.parse(todayDate.minusMonths(14).toString())).toString();
		String toDate= dtf.format(LocalDate.parse(todayDate.minusMonths(13).toString())).toString();
		ConfigurationManager.getBundle().setProperty("date.search.criteria"," AND yoh.order_header_key > '"
										+fromDate+"' AND   yoh.order_header_key < '"+toDate+"'");*/
		

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public void onTestFailure(ITestResult result) {
		failedOrSkippedMethods.add(result.getTestName());
		failedMethods.add(result.getTestName());
		String channel = ConfigurationManager.getBundle().getString("channel");
		generateReportLink(result, "Failed");
		removeTestCaseExecutionDetails();
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		skippedMethods.add(result.getTestName());
		failedOrSkippedMethods.add(result.getTestName());
		generateReportLink(result, "Skipped");
		
		removeTestCaseExecutionDetails();
	}

	@Override
	public void onTestStart(ITestResult result) {
		logger.info("Scenario : " + result.getMethod().getMethodName());
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		passedMethods.add(result.getTestName()); 
		accounts=accounts+ ConfigurationManager.getBundle().getString("testexecution.dp.account.email")+":"+""+ConfigurationManager.getBundle().getString("testexecution.account.id")+" ,";
		System.out.println("accounts:"+accounts);
		generateReportLink(result, "Passed");
		removeTestCaseExecutionDetails();
		
	}

   private void removeTestCaseExecutionDetails() {
	   ConfigurationManager.getBundle().clearProperty("testexecution.rewards.email.id");
	   ConfigurationManager.getBundle().clearProperty("testexecution.dp.account.email");
	   ConfigurationManager.getBundle().clearProperty("testexecution.dp.account.firstname");
	   ConfigurationManager.getBundle().clearProperty(" testexecution.dp.account.lastname");
	   ConfigurationManager.getBundle().clearProperty("testexecution.dp.account.email");
	   ConfigurationManager.getBundle().clearProperty("testexecution.dp.account.password");
	   ConfigurationManager.getBundle().clearProperty("testexecution.migration.token.groupA");
	   ConfigurationManager.getBundle().clearProperty("testexecution.migration.token.groupB");
	   ConfigurationManager.getBundle().clearProperty("testexecution.verifyemail.token");
	   ConfigurationManager.getBundle().clearProperty(" testexecution.mailinator.inbox");
	   ConfigurationManager.getBundle().clearProperty("testexecution.dp.account.accesstoken");
	   ConfigurationManager.getBundle().clearProperty("testexecution.account.id");
	   ConfigurationManager.getBundle().subset("testexecution").clear();
	   ConfigurationManager.getBundle().clearProperty("testexecution.order.subtotal");
	
	}

   public void generateReportLink(ITestResult result, String status) {
		  if (result.getMethod().isTest()) {
		   try {
		       System.out.println("ScenarioFailed=" + ((TestNGScenario) result.getMethod()).getMethodName()
				      + "<>Status=" + status + "<>Testname="+result.getTestContext().getName());
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		   
		  }
		 }
	

	private XmlSuite getRootSuit(XmlSuite suite) {
		if (null != suite.getParentSuite()) {
			return getRootSuit(suite.getParentSuite());
		}
		return suite;
	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		// TODO Auto-generated method stub
		
	}

	
	

}
