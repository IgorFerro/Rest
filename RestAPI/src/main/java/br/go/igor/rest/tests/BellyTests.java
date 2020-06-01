package br.go.igor.rest.tests;

import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;


import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.*;


import br.go.igor.rest.core.BaseTest;



public class BellyTests extends BaseTest {
	
	public static String TOKEN;
	
	@Before
	public void login() {
		Map<String, String> login = new HashMap<>();
		login.put("email", "werttest345@gmail.com");
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
		   .get("/addconta")
		.then()
		   .statusCode(401)
		;
		
	}
	
	@Test
	public void shouldInsertSuccesAccount() {
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body("{\"nome\": \"conta qualquer igor 66699\"}")
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
		 Transactions tran = getValidTransaction();
		 
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body(tran)
		.when()
		   .put("/transacoes")
		.then()
		   .statusCode(201)
		;
	}
	 
	 @Test
		public void shouldCheckMandatoryFielsTransactions() { 
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body("{}")
		.when()
		   .put("/transacoes")
		.then()
		   .statusCode(400)
		   .body("$", hasSize(8))
		   .body("msg", hasItems("Data da Movimentação é obrigatório",
				   "Data do pagamento é obrigatório",
				   "Descrição é obrigatório",
				   "Interessado é obrigatório",
				   "Valor é obrigatório",
				   "Valor deve ser um número"))
		;
	}
	 
	 @Test
		public void shouldInsertSuccesfullTransactionsWithoutFutereDate() {
		 Transactions tran = getValidTransaction();
		 tran.setTransaction_date("31/1/2019");
		 
	     given()
	       .header("Authorization", "JWT" + TOKEN)
	       .body(tran)
		.when()
		   .put("/transacoes")
		.then()
		   .statusCode(400)
		   .body("$", hasSize(1))
		   .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
		;
	}
	 
	 
	 @Test
		public void shouldNotRemoceAccountWithTransactions() {
	     given()
	       .header("Authorization", "JWT" + TOKEN)
		.when()
		   .delete("/contas/1785")
		.then()
		   .statusCode(500)
		   .body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	 
	 private Transactions getValidTransaction() {
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
		 return tran;
	 }
	 
	 
	
	
	
	
}
