package musala.page.objects;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tools {
	private WebDriverWait wait;
	
	/**
	 * 
	 * @param json_file - file with data
	 * @return Array with parsed data from JSON file.
	 * @throws IOException
	 * @throws ParseException
	 * @author K.Dzhongov
	 */
	public String[] json_reader(String json_file) throws IOException, ParseException{
		JSONParser jsonParser = new JSONParser();
		
		FileReader reader = new FileReader(json_file);
		Object obj = jsonParser.parse(reader);
		JSONObject jsonObject = (JSONObject)obj;
		JSONArray array = (JSONArray)jsonObject.get("emails");
		
		String arr[] = new String[array.size()];
		
		for(int i=0;i<array.size();i++) {
			JSONObject emails = (JSONObject)array.get(i);
			String userEmail = (String)emails.get("email");
			arr[i]= userEmail;
		}
		return arr;
	}


	/**
	 * Move to element and click on it.
	 * @param element - WebElement for click
	 * @param driver - WebDriver
	 * @author K.Dzhongov
	 * @throws InterruptedException 
	 */
	public void move_click(WebElement element, WebDriver driver) throws InterruptedException {
		wait = new WebDriverWait(driver, Duration.ofMillis(10000));
//		Actions actions = new Actions(driver);
//		actions.moveToElement(element);
//		actions.perform();
//		element.click();
		wait.until(ExpectedConditions.elementToBeClickable(element));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}
	
	/**
	 * Wait for the URL of current page to be specific URL
	 * @param url - the URL that page should be.
	 * @param driver
	 * @author K.Dzhongov
	 */
	public void waithForUrl(String url, WebDriver driver) {
		wait = new WebDriverWait(driver, Duration.ofMillis(10000));
		wait.until(ExpectedConditions.urlToBe(url));
	}

}
