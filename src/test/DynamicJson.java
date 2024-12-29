package test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

public class DynamicJson {
	
	@Test(dataProvider = "getData")
	public void addBook(String isbn, String aisle) {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		String response = given().log().all().header("Content-Type","application/json")
		.body(Payload.addBook(isbn,aisle))
		.when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println("ID: " + id);
	}
	
	@DataProvider(name="getData")
	public Object getData() {
		return new Object[][] {
			{"abcd","1234"},
			{"asdf","2345"},
			{"wert", "3456"}
		};
	}
}
