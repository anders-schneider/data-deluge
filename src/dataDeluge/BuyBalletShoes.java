package dataDeluge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BuyBalletShoes {
	private static WebDriver driver;
	private static String baseUrl;
	
	private static int linksClicked = 0;
	private static int adsClicked = 0;
	private static int searches = 0;
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability("phantomjs.binary.path", "C:\\Program Files\\PhantomJS\\phantomjs-2.0.0-windows\\bin");
//		
//		System.out.println(capabilities.getCapability("phantomjs.binary.path"));
		
		String[] cliArgs = { "--ignore-ssl-errors=yes" };
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgs);
		
		//driver = new FirefoxDriver();
		driver = new PhantomJSDriver(capabilities);
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		List<String> terms = loadSearchTerms("searchTerms.txt");
		
		searchAll(terms);
		driver.quit();
		
		long stop = System.currentTimeMillis();
		
		printResults(stop - start);
	}

	private static void searchAll(List<String> terms) {
		System.out.println("About to begin searching...");
		for (String term : terms) {
			System.out.println("Beginning the search for " + term);
			driver.get(baseUrl);
			driver.findElement(By.name("q")).clear();
			driver.findElement(By.name("q")).sendKeys(term);
			driver.findElement(By.name("btnG")).click();
			searches++;
			clickLink();
		}
	}

	private static void clickLink() {
		String baseXPath = "/html/body/div[1]/div[5]/div[4]/div[6]";
		
		if (!driver.findElements(By.xpath(baseXPath + "/div[2]/div[4]/div/div/div/ol/li[1]/h3/a[2]")).isEmpty()) {
			// Try to find an ad on the side
			driver.findElement(By.xpath(baseXPath + "/div[2]/div[4]/div/div/div/ol/li[1]/h3/a[2]")).click();
			adsClicked++;
		} else if (!driver.findElements(By.xpath(baseXPath + "/div[2]/div[4]/div/div/div[2]/ol/li[1]/h3/a[2]")).isEmpty()) {
			// Try to find an ad on the side underneath "Shop for ... on Google"
			driver.findElement(By.xpath(baseXPath + "/div[2]/div[4]/div/div/div[2]/ol/li[1]/h3/a[2]")).click();
			adsClicked++;
		} else if (!driver.findElements(By.xpath(baseXPath + "/div[2]/div[3]/div/div[1]/div[3]/div/ol/li[1]/h3/a[2]")).isEmpty()) {
			// Try to find top ad in the middle (of multiple such ads)
			driver.findElement(By.xpath(baseXPath + "/div[2]/div[3]/div/div[1]/div[3]/div/ol/li[1]/h3/a[2]")).click();
			adsClicked++;
		} else if (!driver.findElements(By.xpath(baseXPath + "/div[2]/div[3]/div/div[1]/div[3]/div/ol/li/h3/a[2]")).isEmpty()) {
			// Try to find top ad in the middle (only 1)
			driver.findElement(By.xpath(baseXPath + "/div[2]/div[3]/div/div[1]/div[3]/div/ol/li/h3/a[2]")).click();
			adsClicked++;
		} else if (!driver.findElements(By.xpath(baseXPath + "/div[2]/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a")).isEmpty()) {
			// Try to find top non-ad link after ad block in the middle
			driver.findElement(By.xpath(baseXPath + "/div[2]/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a")).click();
			linksClicked++;
		} else if (!driver.findElements(By.xpath(baseXPath + "/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a")).isEmpty()) {
			// Try to find top non-ad, middle link after "Shop for ... on Google" block
			driver.findElement(By.xpath(baseXPath + "/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a")).click();
			linksClicked++;
		} else if (!driver.findElements(By.xpath(baseXPath + "/div[2]/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a/html/body/div[1]/div[5]/div[4]/div[6]/div[2]/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a")).isEmpty()) {
			// Try to find top link with no ads, no frills
			driver.findElement(By.xpath(baseXPath + "/div[2]/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a/html/body/div[1]/div[5]/div[4]/div[6]/div[2]/div[3]/div/div[2]/div[2]/div/div/ol/div[2]/li[1]/div/h3/a")).click();
			linksClicked++;
		}
		
		
//		if (!driver.findElements(By.id("vs1p1")).isEmpty()) {
//			driver.findElement(By.id("vs1p1")).click();
//			adsClicked++;
//		} else if (!driver.findElements(By.id("vs0p1")).isEmpty()) {
//			driver.findElement(By.id("vs1p1")).click();
//			linksClicked++;
//		}
	}

	private static List<String> loadSearchTerms(String string) {
		List<String> result = new ArrayList<String>();
		result.add("buy ballet shoes");
		result.add("tickets to nutcracker");
		result.add("how much does a tutu cost");
		result.add("ballerina diet");
		
		return result;
	}
	
	private static void printResults(long ms) {
		System.out.println("\n------------------------ SUMMARY OF ACTIVITY ------------------------\n");
		System.out.println("Number of searches performed: " + searches);
		System.out.println("Number of ad links clicked: " + adsClicked);
		System.out.println("Number of search links clicked: " + linksClicked);
		System.out.println("\nTotal time: " + ms / 1000 + " seconds");
	}
}