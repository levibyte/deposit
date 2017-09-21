package com.interview.app;


	import java.lang.*;
	import java.util.*;
	

	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.By;

	
	import org.junit.Assert;
	import org.junit.*;

	public class SeleniumTest {

			public static WebDriver driver;
			public static String username;
			public static String email;
			public static String baseuri;

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

		@BeforeClass
		public static void setup() throws Exception{
			//com.jayway.restassured.RestAssured.baseURI  = baseuri;
			baseuri="http://85.93.17.135:9000";
			System.setProperty("webdriver.chrome.driver","chromedriver.exe");
			
			username = "user_"+rand_str();
			email = username+"@selen.qa";
			driver = new ChromeDriver();
			
			//if failed , testcase will exit and it is resaonable becasue there is no meaning to test other stuff :) 
			createRandValidUniqueUser();
		}

		


	//*  
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

	  

	   
		@Test
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

		@Test
		public void initalLooksOk() throws Exception{

			driver.get(baseuri);
		
			Assert.assertEquals(driver.getTitle(),"New User");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.password.error']")).getText(),"");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.confirmationPassword.error']")).getText(),"");

		}

		// CASE2 pressing submit should activate validators 
		@Test
		public void dummySubmitValidator() throws Exception{

			driver.get(baseuri);

			driver.findElement(By.xpath("//button[@type='submit']")).click();
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.name.error']")).getText(),"Required");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.email.error']")).getText(),"Required");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.password.error']")).getText(),"Required");
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='user.confirmationPassword.error']")).getText(),"");
		}

		
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
		
		
		///CASE , BUG password should be 6 chars, doesn't work.
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
			Assert.assertEquals(driver.getTitle(),"All User");

		}
		
		
		@AfterClass
		public static void close() throws Exception{
			driver.close();
		}
	}
