package test;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured.*;

public class AuthCodeTest {

	public static void main(String args[]) {
		String response = "hi";
		System.out.println("HIiiiii");
		System.out.println(response);
		
		int i=2; int j=3;
		System.out.println(i+j);
		
		  response = given().queryParam("access_token", "").log().all().
		  when().get("https://rahulshettyacademy.com/getCourse.php").then().log().all().extract().asString();
		 
		 System.out.println("HIiiiii");
		System.out.println(response);
		System.out.println(i+j);
		
		
	}
	
	
}
