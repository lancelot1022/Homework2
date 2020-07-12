package TestLoginPage;

import java.util.List;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;

public class Base {
	
	public static void main(String[] args) throws Exception {
		
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		suites.add("./TestSuite.xml");
		testng.setTestSuites(suites);
		testng.run();
		
		
	} 

}
