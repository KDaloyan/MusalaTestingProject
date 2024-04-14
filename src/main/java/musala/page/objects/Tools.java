package musala.page.objects;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.DataProvider;

public class Tools {
	
	/**
	 * 
	 * @param json_file - file with data
	 * @return Array with parsed data from JSON file.
	 * @throws IOException
	 * @throws ParseException
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
	 */
	public void move_click(WebElement element, WebDriver driver) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
		element.click();
	}

}
