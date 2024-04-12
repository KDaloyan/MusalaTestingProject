package musala.test;


import java.io.IOException;
import java.time.Duration;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import musala.page.objects.Tools;

public class TestCases {

	private WebDriver driver;
	public String url;
	
	@DataProvider(name = "jason_parsing")
	public String[] json_reader() throws IOException, ParseException {
		Tools tools = new Tools();
		return tools.json_reader("../MusalaTestingProject/data/emails.json");
	}
	
	@DataProvider(name = "applyData")
	public Object[][] applyData() {
	 return new Object[][] {
	   { "Kaloyan", "kaloyan.dzhongov@gmail.com", "0888033498","www.linkedin.com/in/kaloyan-dzhongov-360603132","Test Message" },
	   { "", "kaloyan.dzhongov@gmail","", "","Test Message" },
	   { "Kaloyan", "", "0888033498","www.linkedin.com/in/kaloyan-dzhongov-360603132","" },
	   { "", "kaloyan.dzhongov@gmail.com", "0888033498","www.linkedin.com/in/kaloyan-dzhongov-360603132","" }
	 };
	}

	@Parameters({"browser","url"})
	@BeforeMethod(alwaysRun = true)
	public void initialize(String browser, String url) {
		this.url = url;
		switch (browser) {
		case "chrome":
			driver = new ChromeDriver();
			break;
		case "firefox":
			driver = new FirefoxDriver();
			break;
		default:
			throw new ExceptionInInitializerError("Browser name is not correct");
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(8000));
	}

	@Test(dataProvider = "jason_parsing")
	public void testCase1(String email) throws InterruptedException {
		
		// 1. Visit http://www.musala.com/
		driver.get(url);
		driver.manage().window().maximize();

		// 2. Scroll down and go to ‘Contact Us’
		WebElement contactUs = driver.findElement(By.cssSelector("[data-alt=\"Contact us\"]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", contactUs);

		// 3. Fill all required fields except email
		WebElement name = driver.findElement(By.name("your-name"));
		name.sendKeys("Kaloyan Dzhongov");

		// 4. Under email field enter string with wrong email format (e.g., test@test)
		WebElement emailField = driver.findElement(By.name("your-email"));
		emailField.sendKeys(email);

		WebElement message = driver.findElement(By.name("your-message"));
		message.sendKeys("Test massage");

		// 5. Click ‘Send’ button
		WebElement send_button = driver.findElement(By.cssSelector("[value=\"Send\"]"));
		((JavascriptExecutor) driver).executeScript("arguments[0];", send_button);
		send_button.click();

		// 6. Verify that error message ‘The e-mail address entered is invalid.’ appears
		WebElement email_error = driver.findElement(By.cssSelector("[data-name=\"your-email\"] .wpcf7-not-valid-tip"));
		org.testng.Assert.assertEquals(email_error.getText(), "The e-mail address entered is invalid.");
	}

	@Test()
	public void testCase2() throws InterruptedException {

		// 1. Visit http://www.musala.com/
		driver.get(url);
		driver.manage().window().maximize();

		// 2. Click ‘Company’ tap from the top

		WebElement company = driver.findElement(By.cssSelector("#navbar #menu .menu-item-887 a"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", company);
		Thread.sleep(1000);

		// 3. Verify that the correct URL (http://www.musala.com/company/) loads
		String url1 = driver.getCurrentUrl();
		Assert.assertEquals(url1, "https://www.musala.com/company/"); //TODo

		// 4. Verify that there is ‘Leadership’ section
		WebElement leadershipSection = driver.findElement(By.cssSelector(".company-members h2"));
		String sectionTitle = leadershipSection.getText();
		Assert.assertEquals(sectionTitle, "Leadership");
		String originalWindow = driver.getWindowHandle();

		// 5. Click the Facebook link from the footer
		WebElement facebook_link = driver.findElement(By.cssSelector(".musala-icon-facebook"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", facebook_link);
		Thread.sleep(1000);
		// 6. Verify that the correct URL (https://www.facebook.com/MusalaSoft?fref=ts)
		// loads and verify if the Musala Soft profile picture appears on the new page
		for (String windowHandle2 : driver.getWindowHandles()) {
			if (!originalWindow.contentEquals(windowHandle2)) {
				driver.switchTo().window(windowHandle2);
				break;
			}
		}
		String url2 = driver.getCurrentUrl();
		 Assert.assertEquals(url2,
		 "https://www.facebook.com/MusalaSoft?fref=ts");
	
		//Open first opened tab
		for (String windowHandle : driver.getWindowHandles()) {
			if (originalWindow.contentEquals(windowHandle)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}
	}

	@Test(dataProvider = "applyData")
	public void testCase3(String name, String email, String mobile, String linkedin, String message) throws InterruptedException {

		// 1. Visit http://www.musala.com/
		driver.get(url);
		driver.manage().window().maximize();

		// 2. Navigate to Careers menu (from the top)
		WebElement careers = driver.findElement(By.cssSelector("#navbar #menu .menu-item-478 a"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", careers);

		// 3. Click ‘Check our open positions’ button
		driver.findElement(By.cssSelector(".contact-label-code")).click();

		// 4. Verify that ‘Join Us’ page is opened (can verify that URL is correct:
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "https://www.musala.com/careers/join-us/");// ToDo

		// 5. From the dropdown ‘Select location’ select ‘Anywhere’
		driver.findElement(By.name("get_location")).click();
		driver.findElement(By.cssSelector("[value=\"Anywhere\"]")).click();
		
		WebElement acceptCookie = driver.findElement(By.cssSelector("#wt-cli-accept-all-btn"));
		acceptCookie.click();
		
		//6. Choose open position by name (e.g., Experienced Automation QA Engineer)
		WebElement position1 = driver.findElement(By.cssSelector("[alt=\"Automation QA Engineer\"]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(position1).perform();
		actions.click(position1).perform();

		//7. Verify that 4 main sections are shown: General Description, Requirements, Responsibilities, What we offer
		WebElement grn_descript = driver.findElement(By.cssSelector(".joinus-content:nth-child(1) .requirements:nth-child(1) h2"));
		Assert.assertEquals(grn_descript.getText(), "General description");
		WebElement responsibilities = driver.findElement(By.cssSelector(".joinus-content:nth-child(2) .requirements:nth-child(1) h2"));
		Assert.assertEquals(responsibilities.getText(), "Responsibilities");
		WebElement requirenments = driver.findElement(By.cssSelector(".joinus-content:nth-child(1) .requirements:nth-child(2) h2"));
		Assert.assertEquals(requirenments.getText(), "Requirements");
		WebElement what_we_offer = driver.findElement(By.cssSelector(".joinus-content:nth-child(2) .requirements:nth-child(2) h2"));
		Assert.assertEquals(what_we_offer.getText(), "What we offer");
		
		//8. Verify that ‘Apply’ button is present at the bottom
		WebElement apply = driver.findElement(By.cssSelector("input.btn-apply"));
		//9. Click ‘Apply’ button
		actions.moveToElement(apply);
		actions.perform();
		apply.click();
		
		//10. Prepare a few sets of negative data (e.g., leave required field(s) empty, enter e-mail with invalid format etc.)
		driver.findElement(By.name("your-name")).sendKeys(name);
		driver.findElement(By.name("your-email")).sendKeys(email);
		driver.findElement(By.name("mobile-number")).sendKeys(mobile);
		driver.findElement(By.name("linkedin")).sendKeys(linkedin);
		driver.findElement(By.name("your-message")).sendKeys(message);
		
		// 11.Upload a CV document, and click ‘Send’ button
		WebElement uploadCV = driver.findElement(By.name("upload-cv"));
		uploadCV.sendKeys("C:\\Users\\kaloj\\eclipse-workspace\\MusalaTestingProject\\data\\CV_Kaloyan_Dzhongov.pdf");
		
		driver.findElement(By.cssSelector("input[value=\"Send\"]")).click();
	}
	
	@Test()
	public void testCase4() throws InterruptedException {

		// 1. Visit http://www.musala.com/
		driver.get(url);
		driver.manage().window().maximize();

		// 2. Go to Careers
		WebElement careers = driver.findElement(By.cssSelector("#navbar #menu .menu-item-478 a"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", careers);
		
		//3. Click ‘Check our open positions’ button
		driver.findElement(By.cssSelector("[data-alt=\"Check our open positions\"]")).click();
		
		//4. Filter the available positions by available cities in the dropdown ‘Select location’ (Sofia and Skopje).

		driver.findElement(By.name("get_location")).click();
		driver.findElement(By.cssSelector("[value=\"Sofia\"]")).click();
		// No results
		driver.findElement(By.cssSelector("[value=\"Skopje\"]")).click();
		// No results
		driver.findElement(By.cssSelector("[value=\"Bulgaria\"]")).click();
		
		//5. Print in the console the list with available positions by city in the following format:
		int jobs_numb = driver.findElements(By.cssSelector(".card-jobsHot")).size();
		for(int i=1;i<=jobs_numb;i++) {
			String location =driver.findElement(By.cssSelector("article.card-jobsHot:nth-of-type("+i+") .card-jobsHot__location")).getText();
			String position =driver.findElement(By.cssSelector("article.card-jobsHot:nth-of-type("+i+") .card-jobsHot__title")).getText();
			String info =driver.findElement(By.cssSelector("article.card-jobsHot:nth-of-type("+i+")  .card-jobsHot__info")).getText();
			System.out.println(location+"\n");
			System.out.println("Position: "+position+"\n");
			System.out.println("Info: "+info+"\n");
		}
	}
	
	
	
	
	@AfterMethod(alwaysRun = true)
    public void afterClass() {
        driver.quit();
    }

}
