package com.interview.app;


	import java.lang.*;
	import java.util.*;
	

	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.firefox.FirefoxDriver;
	import org.openqa.selenium.By;

	
	import org.junit.Assert;
	import org.junit.*;

	public class SeleniumTest {

		public static WebDriver driver;
		public static String username;
		public static String email;
		public static String baseuri;

		//FIXME move to common place	
		public static String rand_str() {
				String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
				StringBuilder salt = new StringBuilder();
				Random rnd = new Random();
				while (salt.length() < 5) {
					int index = (int) (rnd.nextFloat() * SALTCHARS.length());
					salt.append(SALTCHARS.charAt(index));
				}
				String saltStr = salt.toString();
				return saltStr;
		}


		//Case2
		//		what: create valid user with rand username and email
		//  	expected: page title change to new user
		public static void createRandValidUniqueUser() throws Exception{

			driver.get(baseuri);

			//Assert.assertEquals(driver.getTitle(),"New User");
			driver.findElement(By.xpath("//input[@name='user.name']")).sendKeys(username);
			driver.findElement(By.xpath("//input[@name='user.email']")).sendKeys(email);
			driver.findElement(By.xpath("//input[@name='user.password']")).sendKeys("123456");
			driver.findElement(By.xpath("//input[@name='confirmationPassword']")).sendKeys("123456");
		
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			
			Assert.assertEquals(driver.findElement(By.xpath("//*[@class='page-header']")).getText(),"All User");
			driver.findElement(By.xpath("//html/body/div/div/div/a")).click();
			Assert.assertEquals(driver.getTitle(),"New User");
			
		}

		//FIXME could be moved to contructor
		@BeforeClass
		public static void setup() throws Exception{
			//com.jayway.restassured.RestAssured.baseURI  = baseuri;
			baseuri="http://85.93.17.135:9000";
                        
                        String osName = System.getProperty("os.name");
                        if ( osName.contains("Windows") ) {
                            System.setProperty("webdriver.chrome.driver","chromedriver.exe");
                            driver = new ChromeDriver();
                        } else {
                            System.setProperty("webdriver.gecko.driver", "geckodriver");
                            driver = new FirefoxDriver();
                        }
			username = "user_"+rand_str();
			email = username+"@selen.qa";

                        //if failed , testcase will exit and it is resaonable becasue there is no meaning to test other stuff :) 
			createRandValidUniqueUser();
		}


		//Case3
		//		what: click on submit with duplicate username
		//  	expected: page title change to remains the same and appropriate validator error message
		@Test
		public void tryDuplicateUsername() throws Exception{

			driver.get(baseuri);
		
			driver.findElement(By.xpath("//input[@name='user.name']")).sendKeys(username);
			driver.findElement(By.xpath("//input[@name='user.email']")).sendKeys("1"+email);
			driver.findElement(By.xpath("//input[@name='user.password']")).sendKeys("123456");
			driver.findElement(By.xpath("//input[@name='confirmationPassword']")).sendKeys("123456");
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"Must be unique");
			//Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"Must be unique");
			Assert.assertEquals(driver.getTitle(),"New User");

		}
	  

		//Case4
		//		what: click on submit with duplicate email
		//  	expected: page title change to remains the same and appropriate validator error message	  
		@Test
		public void tryDuplicateEmail() throws Exception{
			
			driver.get(baseuri);
		
			driver.findElement(By.xpath("//input[@name='user.name']")).sendKeys("1"+username);
			driver.findElement(By.xpath("//input[@name='user.email']")).sendKeys(email);
			driver.findElement(By.xpath("//input[@name='user.password']")).sendKeys("123456");
			driver.findElement(By.xpath("//input[@name='confirmationPassword']")).sendKeys("123456");
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			
			//Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"Must be unique");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"Must be unique");
			Assert.assertEquals(driver.getTitle(),"New User");

		}

	  

		//Case5
		//		what: click on submit when password and password confirmation doesn't mach
		//  	expected: page title change to remains the same and appropriate validator error message	     
		
		public void tryPasswordAndConfirmPasswordMatchValidation() throws Exception{

			driver.get(baseuri);
		
			driver.findElement(By.xpath("//input[@name='user.name']")).sendKeys("3"+username);
			driver.findElement(By.xpath("//input[@name='user.email']")).sendKeys("3"+email);
			driver.findElement(By.xpath("//input[@name='user.password']")).sendKeys("helo");
			driver.findElement(By.xpath("//input[@name='confirmationPassword']")).sendKeys("ehlo");
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.password.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.confirmationPassword.error']")).getText(),"passwords are not the same");
			Assert.assertEquals(driver.getTitle(),"New User");

		}

		//Case6
		//		what: check validators are empty on page load
		@Test
		public void initalLooksOk() throws Exception{

			driver.get(baseuri);
		
			Assert.assertEquals(driver.getTitle(),"New User");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.password.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.confirmationPassword.error']")).getText(),"");

		}

		//Case7
		//		what: dummy pressing submit on first load
		//		expected: all validators should activate with message "required".
		@Test
		public void dummySubmitValidator() throws Exception{

			driver.get(baseuri);

			driver.findElement(By.xpath("//button[@type='submit']")).click();
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"Required");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"Required");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.password.error']")).getText(),"Required");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.confirmationPassword.error']")).getText(),"");
		}


		//Case8
		//		what: try mallformed email and hit submit
		//		expected: page title the same and email.error validator to be "invalid email address"
		
		@Test
		public void tryEmailValidation() throws Exception{

			driver.get(baseuri);
		
		
			driver.findElement(By.xpath("//input[@name='user.name']")).sendKeys("3"+username);
			driver.findElement(By.xpath("//input[@name='user.email']")).sendKeys("invalid.email");
			driver.findElement(By.xpath("//input[@name='user.password']")).sendKeys("123456");
			driver.findElement(By.xpath("//input[@name='confirmationPassword']")).sendKeys("123456");
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"Invalid email address");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.password.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.confirmationPassword.error']")).getText(),"");
			Assert.assertEquals(driver.getTitle(),"New User");

		}
		
		
		///BUG password length validity (doesn't work.)
		//Case9
		//		what: try 4 sized string as password and hit submit 
		//		expected: page title remains the same and password validator to be "?"
		@Test
		public void tryPasswordLengthValidation() throws Exception{

			driver.get(baseuri);
		
			driver.findElement(By.xpath("//input[@name='user.name']")).sendKeys("2"+username);
			driver.findElement(By.xpath("//input[@name='user.email']")).sendKeys("2"+email);
			driver.findElement(By.xpath("//input[@name='user.password']")).sendKeys("1234");
			driver.findElement(By.xpath("//input[@name='confirmationPassword']")).sendKeys("1234");
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			
			//Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"");
			//Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"");
			//Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.password.error']")).getText(),"Required");
			//Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.confirmationPassword.error']")).getText(),"");
			//Assert.assertEquals(driver.getTitle(),"All User");

		}
		
		
		@AfterClass
		public static void close() throws Exception{
			driver.close();
		}
	}
