package br.go.igor.rest.tests.refac.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.go.igor.rest.core.BaseTest;
import br.go.igor.rest.tests.refac.AccountsTests;
import br.go.igor.rest.tests.refac.AuthTests;
import br.go.igor.rest.tests.refac.BalanceTests;
import br.go.igor.rest.tests.refac.TransactionsTests;
import io.restassured.RestAssured;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	AccountsTests.class,
	TransactionsTests.class,
	BalanceTests.class,
	AuthTests.class
})

public class Suite extends BaseTest {
	
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

}
