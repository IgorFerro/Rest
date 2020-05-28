package br.go.igor.rest.tests;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.go.igor.rest.core.BaseTest;

public class BellyTests extends BaseTest {
	
	private String TOKEN;
	
	@Before
	public void login() {
		Map<String, String> login = new HashMap<>();
		login.put("email", "wertyui@gmail.com");
		login.put("senha", "123456");
		
		
	 TOKEN = given()
		    .body(login)
		.when()
		   .post("/signin")
		.then()
		   .statusCode(200)
		   .extract().path("token");
	 
	}
	
	
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
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body("{\"nome\": \"conta qualquer igor 666\"}")
		.when()
		   .post("/contas")
		.then()
		   .statusCode(201)
		;
	
	}
	
	@Test
	public void shouldChangeDataAccount() {
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body("{\"nome\": \"conta qualquer igor 688\"}")
		.when()
		   .put("/contas/17585")
		.then()
		   .statusCode(200)
		;
	
	}
	
	
}
