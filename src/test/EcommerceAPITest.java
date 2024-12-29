package test;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

public class EcommerceAPITest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();
		
		LoginRequest loginReqObj = new LoginRequest();
		loginReqObj.setUserEmail("api99@gmail.com");
		loginReqObj.setUserPassword("Sais@279");
		
		RequestSpecification reqLogin = given().log().all().spec(req).body(loginReqObj);
		
		LoginResponse loginRes = reqLogin.when().post("api/ecom/auth/login").then().log().all().extract().response().as(LoginResponse.class);
		String token = loginRes.getToken();
		String userID = loginRes.getUserId();
		System.out.println("token: " + token);
		System.out.println("userID: " + userID);
		
		
		
		//Spec builder for Add Product Req
		RequestSpecification addProdBaseReq =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.addHeader("Authorization", token).build();
		
		RequestSpecification reqAddProduct =  given().log().all().spec(addProdBaseReq)
		.param("productName", "LaptopTest1")
		.param("productAddedBy", userID)
		.param("productCategory", "Electronics")
		.param("productSubCategory", "Handy")
		.param("productPrice", "18000")
		.param("productDescription", "Software Goods")
		.param("productFor", "all")
		.multiPart("productImage", new File("C:\\Users\\000AA7744\\Pictures\\sai3.png"));
		
		String addProductResp =  reqAddProduct.when().post("api/ecom/product/add-product").then().log().all()
		.extract().response().asString();
		
		JsonPath js = new JsonPath(addProductResp);
		String productId = js.get("productId");
		
		//Create Order
		RequestSpecification createOrderBaseReq =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).addHeader("Authorization", token).build();
				
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		
		List<OrderDetail> OrderDetailList = new ArrayList<OrderDetail>();
		OrderDetailList.add(orderDetail);
		
		Orders order = new Orders();
		order.setOrders(OrderDetailList);
		
		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(order);
		
		String createOrderResp =  createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
		System.out.println(createOrderResp);
		
		//Delete Product
		RequestSpecification deleteProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
		.addHeader("Authorization", token).build();
		
		RequestSpecification deleteProductReq =  given().spec(deleteProductBaseReq).pathParam("productId", productId);
		
		String deleteProductResp = deleteProductReq.when().delete("api/ecom/product/delete-product/{productId}")
		.then().log().all().extract().response().asString();	
		
		JsonPath js1 = new JsonPath(deleteProductResp);
		String productDeleteMsg = js1.get("message");
		System.out.println("Meesage on Product Delete: " + productDeleteMsg );
		Assert.assertTrue(productDeleteMsg.equalsIgnoreCase("Product Deleted Successfully"));
		
		
		
		
	}

}
