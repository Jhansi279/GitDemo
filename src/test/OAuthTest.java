package test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourses;
import pojo.WebAutomation;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

public class OAuthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] webCourseTitls = {"Selenium Webdriver Java", "Cypress", "Protractor"};

		String resp = given()
				.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W").formParam("grant_type", "client_credentials")
				.formParam("scope", "trust").post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token")
				.then().log().all().extract().asString();

		JsonPath js = new JsonPath(resp);
		String access_token = js.getString("access_token");

		System.out.println("Access Token: " + access_token);

		// parsing using JsonPath
		/*
		 * String course_resp = given() .queryParam("access_token", access_token)
		 * .when().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
		 * .then().extract().asString();
		 */

		//parsing using java object - deserialization. Give the outer class name - here, GetCourses.class
		GetCourses gc = given() .queryParam("access_token", access_token)
		  .when().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
		  .then().extract().as(GetCourses.class);
		 
		//Extract the value of Instructor and LinkedIn
		System.out.println("Instructor: " + gc.getInstructor());
		System.out.println("LinkedIn: " + gc.getLinkedIn());

		//Extract the values of 2nd course title and price under api
		System.out.println("Course Title: " + gc.getCourses().getApi().get(1).getCourseTitle());
		System.out.println("Course Price: " + gc.getCourses().getApi().get(1).getPrice());
		
		//Extract the price of the course whose title is 'SoapUI Webservices testing'
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i=0; i<apiCourses.size(); i++) {
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
				System.out.println("Price of SoapUI Webservices testing: " + apiCourses.get(i).getPrice() );
		}
		
		//Extract the course titles that come under webAutomation
		System.out.println("Courses under Web Automation");
		List<WebAutomation> webCourses = gc.getCourses().getWebAutomation();
		for(int i=0; i<webCourses.size(); i++) {
			System.out.println(webCourses.get(i).getCourseTitle());
		}
		
		
		//Compare the titles with expected title values
		ArrayList<String> a = new ArrayList<String>(); //add titles of web cources to this array list (size is not fixed)
		List<WebAutomation> w = gc.getCourses().getWebAutomation();
		for(int j=0; j<w.size(); j++) {
			a.add(w.get(j).getCourseTitle());
		}
		
		//Convert aaray to array list to comapre 2 array lists
		List<String> expectedTitles = Arrays.asList(webCourseTitls);
		
		//Assert that both array lists are equal.
		Assert.assertTrue(a.equals(expectedTitles));
		
		
	}

}
