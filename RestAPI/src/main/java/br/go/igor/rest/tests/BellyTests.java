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
	
	@Test
	public void shouldntInsertAccountWithSameData() {
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body("{\"nome\": \"conta qualquer igor 688\"}")
		.when()
		   .post("/contas")
		.then()
		  .statusCode(400)
		  //.body("error", is("Já exite uma conta com esse nome!"));
		    ;
	}
	
	 @Test
		public void shouldInsertSuccesfullTransactions() {
		 Transactions tran = new Transactions();
		 tran.setAccount_id(17585);
		 //tran.setUser_id(user_id);
		 tran.setDescription("Description Transaction");
		 tran.setInvolved("Envolved Trasaction");
		 tran.setType("REC");
		 tran.setTransaction_date("01/01/2020");
		 tran.setPayment_date("10/06/2020");
		 tran.setValue(100f);
		 tran.setStatus(true);
		 
		 
		 
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body(tran)
		.when()
		   .put("/transacoes")
		.then()
		   .statusCode(201)
		;
	
	}
	
	
	
}
