package br.go.igor.rest.tests;

import org.junit.Test;
import static io.restassured.RestAssured.given;

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
}
