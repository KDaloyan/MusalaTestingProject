package musala.page.objects;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

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
	 */
	public void move_click(By locator, WebDriver driver) {
		WebElement element = driver.findElement(locator);
		wait = new WebDriverWait(driver, Duration.ofMillis(10000));
		wait.until(ExpectedConditions.elementToBeClickable(locator));
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
		element.click();
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
