package musala.page.objects;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;

public class Tools {
	
	/**
	 * 
	 * @param json_file - file with data
	 * @return Array with parsed data from JSON file.
	 * @throws IOException
	 * @throws ParseException
	 */
	@DataProvider(name = "jason_parsing")
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
