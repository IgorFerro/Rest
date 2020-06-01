package br.go.igor.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.go.igor.rest.core.BaseTest;
import br.go.igor.utils.BellyUtils;

public class BalanceTests extends BaseTest {
	

	 @Test
		public void shouldCalculateAccountBalance() {
		 Integer CONTA_ID = BellyUtils.getIdAccountByName("Conta para saldo");
		 
	     given()
		.when()
		   .get("/saldo")
		.then()
		   .statusCode(200)
		   .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00"))
		;
	}
	
}
