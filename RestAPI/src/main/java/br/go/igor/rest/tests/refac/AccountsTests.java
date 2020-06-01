package br.go.igor.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.go.igor.rest.core.BaseTest;
import io.restassured.RestAssured;

public class AccountsTests extends BaseTest {
	
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
	public void shouldInsertSuccesAccount() {
	        given()
	       .body("{\"nome\": \"Conta Inserida\"}")
		.when()
		   .post("/contas")
		.then()
		   .statusCode(201)
		   .extract().path("id")
		;
	
	}
	
	@Test
	public void shouldChangeDataAccount() {
		Integer CONTA_ID = getIdAccountByName("Conta para alterar");
		
	     given()
	       .body("{\"nome\": \"Conta alterada\"}")
	       .pathParam("id", CONTA_ID )
		.when()
		   .put("/contas/{id}")
		.then()
		   .statusCode(200)
		   .body("nome", is("Conta alterada"));
		;
	
	}
	
	@Test
	public void shouldntInsertAccountWithSameData() {
	     given()
	       .body("{\"nome\": \"Conta mesmo nome\"}")
		.when()
		   .post("/contas")
		.then()
		  .statusCode(400)
		  //.body("error", is("Já exite uma conta com esse nome!"));
		    ;
	}
	
	public Integer getIdAccountByName(String name) {
		return RestAssured.get("/contas?nome"+name).then().extract().path("id[0]");
	}

}
