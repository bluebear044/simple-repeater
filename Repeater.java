import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;
import org.openqa.selenium.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

 
public class Repeater {

	public static void main(String[] args) {

		String loginID = "아이디";
		String loginPW = "패스워드";
		String floor = "F80090125";
		String section = "S80092503";
		String seatNO = "1045";
 
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

			if(driver.findElements(By.id("lblAuthMessage")).size() == 0) {

				System.out.println("[LOG] Login Failed");
				return;

			}else {

				System.out.println("[LOG] Login Success");
				
			}
			
			//System.out.println(driver.getPageSource());

			if(driver.findElements(By.xpath("//*[@id='body_divResCancelBtn']/div/button")).size() != 0){
				
				System.out.println("[LOG] Already reserved");
				return;

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
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-seatno='" + seatNO + "']")));
				WebElement element = driver.findElement(By.xpath("//*[@data-seatno='" + seatNO + "']"));

				if(element.getAttribute("data-flag").equals("true")) {

					System.out.println("[LOG] Seat unavailable");
					return;

				}

				System.out.println(element.getAttribute("data-seatno"));
				System.out.println(element.getAttribute("data-flag"));

				//wait.until(ExpectedConditions.elementSelectionStateToBe(By.id("//*[@id='body_ddlSch_section']/option[3]")));
				//driver.findElement(By.id("s40")).click();
				//System.out.println("[LOG] Selected seat");

				// 로그인 Cookies 얻어 옴
				Set<Cookie> cookies = driver.manage().getCookies();
				Map<String, String> cookieMap = cookies.stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
				for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
				    System.out.println("key: " + entry.getKey() + ", value : " + entry.getValue());
				}

				Document doc = Jsoup.connect("http://sor.lotte.com/htm/web/reservation/res_today.aspx")
					.data("hdfSelectSeat", "1045")
					.data("hdfResSeq", "")
					.data("ddlSch_floor", "F80090125")
					.data("ddlSch_section", "S80092503")
					.data("txtNowDate", "2019-04-28")
					.data("btnResAppr", "")
					.userAgent("Mozilla")
					.cookies(cookieMap) // 로그인 Cookies 세팅
					.post();
				System.out.println(doc);

				//url : http://sor.lotte.com/htm/web/reservation/res_today.aspx
				//ctl00$ScriptManager: ctl00$UpdatePanel|ctl00$body$btnResAppr
				//ctl00$body$hdfSelectSeat: 1045
				//ctl00$body$hdfResSeq: 
				//ctl00$body$ddlSch_floor: F80090125
				//ctl00$body$ddlSch_section: S80092503
				//ctl00$body$txtNowDate: 2019-04-28
				//ctl00$body$btnResAppr:

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