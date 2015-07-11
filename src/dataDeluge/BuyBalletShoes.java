package dataDeluge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BuyBalletShoes {
	private static WebDriver driver;
	private static String baseUrl;
	
	private static int linksClicked = 0;
	private static int adsClicked = 0;
	private static int searches = 0;
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		List<String> terms = loadSearchTerms("searchTerms.txt");
		
		searchAll(terms);
		
		long stop = System.currentTimeMillis();
		
		printResults(stop - start);
	}

	private static void searchAll(List<String> terms) {
		for (String term : terms) {
			driver.get(baseUrl + "/");
			driver.findElement(By.id("lst-ib")).clear();
			driver.findElement(By.id("lst-ib")).sendKeys(term);
			driver.findElement(By.name("btnG")).click();
			searches++;
			clickLink();
		}
	}

	private static void clickLink() {
		if (!driver.findElements(By.id("vs1p1")).isEmpty()) {
			driver.findElement(By.id("vs1p1")).click();
			adsClicked++;
		} else if (!driver.findElements(By.id("vs0p1")).isEmpty()) {
			driver.findElement(By.id("vs1p1")).click();
			linksClicked++;
		}
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