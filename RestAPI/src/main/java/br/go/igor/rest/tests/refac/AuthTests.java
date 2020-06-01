package br.go.igor.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.go.igor.rest.core.BaseTest;
import br.go.igor.rest.tests.Transactions;
import br.go.igor.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTests extends BaseTest {
	
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<>();
		login.put("email", "werttest345@gmail.com");
		login.put("senha", "123456");
		
		
	String TOKEN = given()
		    .body(login)
		.when()
		   .post("/signin")
		.then()
		   .statusCode(200)
		   .extract().path("token");
	 
	 RestAssured.requestSpecification.header("Authorization", "JWT" + TOKEN);
	 
	 RestAssured.get("/reset").then().statusCode(200);
	 
	}
	
	@Test
	public void shouldntAcessAPIWithoutToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		
		given()
		.when()
		   .get("/addconta")
		.then()
		   .statusCode(401)
		;
		
	}
	

}
