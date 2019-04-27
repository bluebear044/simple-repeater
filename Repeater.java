import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
 
public class Repeater {

	public static void main(String[] args) {

		String loginID = "아이디";
		String loginPW = "패스워드";
		String floor = "F80090125";
		String section = "S80092503";
		String seatNO = "s40";
 
		Repeater instance = new Repeater();
		instance.repeat(loginID, loginPW, floor, section, seatNO);
		
	}

	//Properties
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "D:/chromedriver.exe";

	//WebDriver
	private WebDriver driver;
	private WebDriverWait wait;
	private WebElement webElement;
	private String base_url;

	public Repeater() {

		//System Property SetUp
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
			
		//Driver SetUp
		ChromeOptions options = new ChromeOptions();
		options.setCapability("ignoreProtectedModeSettings", true);
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, 30);
		
		base_url = "http://sor.lotte.com";
		
	}
 
	public void repeat(String loginID, String loginPW, String floor, String section, String seatNO) {
 
		try {

			driver.get(base_url);
			
			webElement = driver.findElement(By.id("txtCommonID"));
			webElement.sendKeys(loginID);
			
			webElement = driver.findElement(By.id("txtCommonPwd"));
			webElement.sendKeys(loginPW);

			driver.findElement(By.xpath("//*[@id='lnkDoLogin']")).click();
			
			//System.out.println(driver.getPageSource());

			if(driver.findElements(By.xpath("//*[@id='body_divResCancelBtn']/div/button")).size() != 0){
				
				System.out.println("[LOG] Already reserved");

			}else {
				
				Select floorSelect = new Select(driver.findElement(By.xpath("//*[@id='body_ddlSch_floor']")));
				floorSelect.selectByValue(floor);

				System.out.println("[LOG] Selected floor");

				//add Explicit Wait
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='body_ddlSch_section']/option[@value='" + section + "']")));
				Select sectionSelect = new Select(driver.findElement(By.xpath("//*[@id='body_ddlSch_section']")));
				sectionSelect.selectByValue(section);

				System.out.println("[LOG] Selected section");

				//add Explicit Wait
				//wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='" + seatNO + "']")));
				//driver.findElement(By.xpath("//*[@id='" + seatNO + "']")).click();

				//System.out.println("[LOG] Selected seat");

				wait.until(ExpectedConditions.elementToBeClickable(By.id("s40")));
				driver.findElement(By.id("s40")).click();
			    driver.findElement(By.cssSelector("div:nth-child(1) > button:nth-child(3)")).click();
			    driver.findElement(By.cssSelector(".w3-button")).click();

				//wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='body_divResApprBtn']/div/button")));
				//driver.findElement(By.xpath("//*[@id='body_divResApprBtn']/div/button")).click();
				
			}

			Thread.sleep(10000);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}

	}

}