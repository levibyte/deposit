package com.interview.app;


	import java.lang.*;
	import java.util.*;
	import static com.jayway.restassured.RestAssured.given;
	import static com.jayway.restassured.RestAssured.*;
	//import static com.jayway.restassured.parsing.Parser;

	import static org.hamcrest.Matchers.*;
		
	import org.junit.Assert;
	import org.junit.*;



	public class RestTest { 


		public RestTest() {
				com.jayway.restassured.RestAssured.baseURI  = "http://85.93.17.135:9000";
				//RestAssured.registerParser("text/plain", Parser.JSON);
		}
		
		//fixme , need to move to common place
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

				

		//BUG1 can't POST with json
		//BUG2 redirects disrespectfull to any case
		//Actually is not really test but util that will be called from other tests
		//CaseX 
		//		what: execute POST users/save.
		//  	expected: code 302
		public void REST_postNewUser(String name) {
				given().
						params("user.name", name, "user.email", name+"@2l.11","user.password","11111111", "confirmationPassword","11111111").
						
				when().
						post("/user/save").
				then().
						statusCode(302);		
			
		}

		//Case1 
		//		what: execute GET users/all.
		//  	expected: code 200
		@Test
		public void REST_tryPostNewUserAndGetAllUsers() {
				String mystr = rand_str();
				REST_postNewUser(mystr);
				
				given().when().get("/users/all").then().statusCode(200);
				
		}
		
		
		//Case2
		//		what: execute GET users/all.
		//  	expected: 
		//				1 . code 200 
		//				2   returns JSON
		//				3   recently created user data in json
		@Test
		public void REST_tryPostNewUserAndGetAllUsersJson() {
				String mystr = rand_str();
				REST_postNewUser(mystr);
				
				given().when().get("/user/all/json").then().contentType("application/json; charset=utf-8").and().statusCode(200).and().assertThat().body(containsString(mystr));
				
		}
		

		//Case3
		//		what: execute GET users/all.
		//  	expected: 
		//				1 . code 200 
		//				3   return body is empty ( no parsers for)
		@Test
		public void REST_tryDeleteAllUsers() {
				//FIXME little hack, can't user matchers with text/plain, so expecting no "user" keys to be found therefore list will be empty.
				given().when().delete("/user/all").then().statusCode(200).and().assertThat().body(not(containsString("user")));
		}

		

		//Case4
		//		what: double execute DEL users/all.
		//  	expected: 200 status code two times 
		@Test
		public void REST_tryDoubleDeleteAllUsers() {
				given().when().delete("/user/all").then().statusCode(200);
				given().when().delete("/user/all").then().statusCode(200);
		}

		
		//Case5
		//		what: execute DEL to users.
		//  	expected: 500 status code  
		@Test
		public void REST_tryDeleteOneUser() {
				given().when().delete("/user").then().statusCode(404);
		}

	
	}
	
