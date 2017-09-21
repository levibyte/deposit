package com.interview.app;


	import java.lang.*;
	import java.util.*;
	import static com.jayway.restassured.RestAssured.given;
	import static com.jayway.restassured.RestAssured.*;
	

	import static org.hamcrest.Matchers.*;
		
	import org.junit.Assert;
	import org.junit.*;



	public class RestTest { 


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

		
		@BeforeClass
		public static void setup() throws Exception{
				com.jayway.restassured.RestAssured.baseURI  = "http://85.93.17.135:9000";

		}
		

		
		public void REST_postNewUser(String name) {
				given().
						params("user.name", name, "user.email", name+"@2l.11","user.password","11111111", "confirmationPassword","11111111").
						
				when().
						post("/user/save").
				then().
						statusCode(302);		
			
		}
		
		@Test
		public void REST_tryPostNewUserAndGetAllUsersJson() {
				String mystr = rand_str();
				REST_postNewUser(mystr);
				
				given().when().get("/user/all/json").then().statusCode(200);
				//and check some values
		}
		

		@Test
		public void REST_tryDeleteAllUsers() {
				given().when().delete("/user/all").then().statusCode(200);//and().assertThat().body("",hasSize(2));
				
				//try to delete second time
				given().when().delete("/user/all").then().statusCode(200);//
		}

	}
