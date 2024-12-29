package test;

import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class complexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js = new JsonPath((Payload.coursePrice()));
		
		//no. of courses
		int count = js.getInt("courses.size()");
		System.out.println("Courses count: "+count);
		
		//Total amount
		int purAmt = js.getInt("dashboard.purchaseAmount");
		System.out.println("purAmt: " + purAmt);
		
		//title of 1st course
		String titleFirstCourse = js.get("courses[0].title");
		System.out.println("titleFirstCourse: " + titleFirstCourse);
		
		//Print all courses and resp prices
		for(int i=0; i<count; i++ )
		{
			System.out.println( js.getString("courses["+i+"].title"));
			System.out.println(js.getInt("courses["+i+"].price"));
		}

		//Print no of copies sold for RPA
		System.out.println("Print no of copies sold for RPA");
		for(int i=0; i<count; i++ )
		{
			 if(js.getString("courses["+i+"].title").equalsIgnoreCase("RPA"))
				 System.out.println(js.getInt("courses["+i+"].copies"));
			
		}

		//Verify if sum of prices of all courses matches purchaseAmount
		int sum =0;
		for(int i=0; i<count; i++ )
		{
			int price = js.getInt("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			sum = sum + (price * copies);
		}
		System.out.println("Sum of  prices of all courses: " + sum);
		Assert.assertEquals(sum, js.getInt("dashboard.purchaseAmount"));
	}

}
