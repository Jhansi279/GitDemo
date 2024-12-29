package test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.Payload;

public class Basics {
	public static void main(String args[]) throws IOException {

		// Validate Add Place
		// given
		// when
		// then
		//Welcome to REST Assured Learning
		System.out.println("Welcome to REST Assured Learning");
		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		
		  String response = given().log().all().queryParam("key",
		  "qaclick123").header("Content-Type","application/json")
		  .body(Payload.addPlace()) .post("maps/api/place/add/json")
		  .then().log().all().assertThat().statusCode(200) .body("scope",
		  equalTo("APP")) .header("Server", "Apache/2.4.52 (Ubuntu)")
		  .extract().response().asString();
		  
		 
		// take json body from an external file - Used when we have static body
		/*
		 * String response = given().log().all().queryParam("key",
		 * "qaclick123").header("Content-Type", "application/json") .body(new
		 * String(Files.readAllBytes(Paths.get(
		 * "C:\\Users\\000AA7744\\Desktop\\API\\addPlace.json"))))
		 * .post("maps/api/place/add/json").then().log().all().assertThat().statusCode(
		 * 200) .body("scope", equalTo("APP")).header("Server",
		 * "Apache/2.4.52 (Ubuntu)").extract().response() .asString();
		 */
		System.out.println("response: " + response);

		// Get place_id value from response
		JsonPath js = new JsonPath(response);// For parsing Json
		String place_id = js.getString("place_id");

		System.out.println("place_id: " + place_id);

		// Validate Update Place (with new address) - put
		String newAddr = "Summer Walk, Africa";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + place_id + "\",\r\n" + "\"address\":\"" + newAddr + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}\r\n")
				.when().put("maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));

		// Validate if address is updated successfully - GET
		String get_resp = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_id).when()
				.get("maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract().response()
				.asString();

		System.out.println("get_resp: " + get_resp);
		JsonPath js1 = ReusableMethods.rawToJson(get_resp);
		String actual_addr = js.getString("address");
		Assert.assertEquals(actual_addr, newAddr);

		/*
		 * if(js.getString("address").equals(newAddr))
		 * System.out.println("Address Updated Successfully");
		 */

	}

}
