package br.go.igor.rest.tests;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import br.go.igor.rest.core.BaseTest;

public class BellyTests extends BaseTest {

	@Test
	public void shouldntAcessAPIWithoutToken() {
		given()
		.when()
		   .get("/contas")
		.then()
		   .statusCode(401)
		;
		
	}
	
	@Test
	public void shouldInsertSuccesAccount() {
		Map<String, String> login = new HashMap<>();
		login.put("email", "wertyui@gmail.com");
		login.put("senha", "123456");
		
		
	 String token =	given()
		    .body(login)
		.when()
		   .post("/signin")
		.then()
		   .statusCode(200)
		   .extract().path("token");
	 
	 
	     given()
	       .header("Authorization", "JWT" + token)
	       .body("{\"nome\": \"conta qualquer igor 666\"}")
		.when()
		   .post("/contas")
		.then()
		   .statusCode(201)
		;
	
		
	}
	
	
}
