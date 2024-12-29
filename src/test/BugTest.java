package test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.MultiPartSpecification;

import static io.restassured.RestAssured.*;

import java.io.File;

public class BugTest {
	public static void main(String args[]) {
		
		RestAssured.baseURI = "https://ranijhansig.atlassian.net/";
		
		//create a bug in Jira
		String createIssueResponse = given().log().all()
		.header("Content-Type","application/json")
		.header("Authorization","Basic cmFuaWpoYW5zaS5nQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjA3dlRoZ2tuTjhud0lzM2dod0xLUGNsNUwxaWMtYXQzeTlYZENlSXdHcUZqMkFJbUdLM0VOeVRaRGszTG9EWWpHeTZuaElFNzB5QXlsUEFjOUpVc245T3NMMEthdUNBUnhlOU83UDVKWVpadTBHaWFkRmlnTi1Nd1Yzdnc2STBWYzVVcXh3NlRMYllYRHNrWmVaU19HSjlnZnBMOGJ0RlRmN1dTeGlGbV9KVVU9NDRBRTEyMjk=")
		.body("{\r\n"
				+ "    \"fields\": {\r\n"
				+ "       \"project\":\r\n"
				+ "       {\r\n"
				+ "          \"key\": \"SCRUM\"\r\n"
				+ "       },\r\n"
				+ "       \"summary\": \"Create1 Not Working - From Automation Rest Assured\",\r\n"
				+ "       \"description\": \"In CHM UI - Dropdown Not Working\",\r\n"
				+ "       \"issuetype\": {\r\n"
				+ "          \"name\": \"Bug\"\r\n"
				+ "       }\r\n"
				+ "   }\r\n"
				+ "}\r\n"
				+ "")
		.when().post("rest/api/2/issue")
		.then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(createIssueResponse);
		String issueId = js.getString("id");
		System.out.println("Created Issue ID:" + issueId);
		
		//Add Attachment
		given().log().all().pathParam("key", issueId)
		.header("X-Atlassian-Token","no-check")
		.header("Authorization","Basic cmFuaWpoYW5zaS5nQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjA3dlRoZ2tuTjhud0lzM2dod0xLUGNsNUwxaWMtYXQzeTlYZENlSXdHcUZqMkFJbUdLM0VOeVRaRGszTG9EWWpHeTZuaElFNzB5QXlsUEFjOUpVc245T3NMMEthdUNBUnhlOU83UDVKWVpadTBHaWFkRmlnTi1Nd1Yzdnc2STBWYzVVcXh3NlRMYllYRHNrWmVaU19HSjlnZnBMOGJ0RlRmN1dTeGlGbV9KVVU9NDRBRTEyMjk=")
		.multiPart("file", new File("C:/Users/000AA7744/Pictures/Screenshots/Screenshot 2024-11-28 180839.png"))
		.when()
		.post("rest/api/2/issue/{key}/attachments")
		.then().log().all()
		.assertThat().statusCode(200);
		
		
	}

}
