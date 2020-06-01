package br.go.igor.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.go.igor.rest.core.BaseTest;
import io.restassured.RestAssured;

public class BalanceTests extends BaseTest {
	
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
		public void shouldCalculateAccountBalance() {
		 Integer CONTA_ID = getIdAccountByName("Conta para salod");
		 
	     given()
		.when()
		   .get("/saldo")
		.then()
		   .statusCode(200)
		   .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00"))
		;
	}
	
	public Integer getIdAccountByName(String name) {
		return RestAssured.get("/contas?nome"+name).then().extract().path("id[0]");
	}

}
