package br.go.igor.rest.tests.refac;

import static io.restassured.RestAssured.given;
import org.junit.Test;

import br.go.igor.rest.core.BaseTest;

import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTests extends BaseTest {
	
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
