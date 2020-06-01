package br.go.igor.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import br.go.igor.rest.core.BaseTest;
import br.go.igor.utils.BellyUtils;


public class AccountsTests extends BaseTest {
		
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
		Integer CONTA_ID = BellyUtils.getIdAccountByName("Conta para alterar");
		
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
	
}
