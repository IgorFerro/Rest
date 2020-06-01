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

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.*;


import br.go.igor.rest.core.BaseTest;
import br.go.igor.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BellyTests extends BaseTest {
	
	
	private static String CONTA_NAME = "Conta " + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
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
	 
	}
	
	
	@Test
	public void t02_shouldInsertSuccesAccount() {
	    CONTA_ID = given()
	       .body("{\"nome\": \""+CONTA_NAME+"\"}")
		.when()
		   .post("/contas")
		.then()
		   .statusCode(201)
		   .extract().path("id")
		;
	
	}
	
	@Test
	public void t03_shouldChangeDataAccount() {
	     given()
	       .body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
	       .pathParam("id", CONTA_ID )
		.when()
		   .put("/contas/{id}")
		.then()
		   .statusCode(200)
		   .body("nome", is(CONTA_NAME+ "alterada"));
		;
	
	}
	
	@Test
	public void t04_shouldntInsertAccountWithSameData() {
	     given()
	       .body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
		.when()
		   .post("/contas")
		.then()
		  .statusCode(400)
		  //.body("error", is("J� exite uma conta com esse nome!"));
		    ;
	}
	
	 @Test
		public void t05_shouldInsertSuccesfullTransactions() {
		 Transactions tran = getValidTransaction();
		 
		 MOV_ID = given()
	       .body(tran)
		.when()
		   .put("/transacoes")
		.then()
		   .statusCode(201)
		   .extract().path("id")
		;
	}
	 
	 @Test
		public void t06_shouldCheckMandatoryFielsTransactions() { 
	     given()
	       .body("{}")
		.when()
		   .put("/transacoes")
		.then()
		   .statusCode(400)
		   .body("$", hasSize(8))
		   .body("msg", hasItems("Data da Movimenta��o � obrigat�rio",
				   "Data do pagamento � obrigat�rio",
				   "Descri��o � obrigat�rio",
				   "Interessado � obrigat�rio",
				   "Valor � obrigat�rio",
				   "Valor deve ser um n�mero"))
		;
	}
	 
	 @Test
		public void t07_shouldInsertSuccesfullTransactionsWithoutFutereDate() {
		 Transactions tran = getValidTransaction();
		 tran.setTransaction_date(DataUtils.getDataDiferencaDias(2));
		 
	     given()
	       .body(tran)
		.when()
		   .put("/transacoes")
		.then()
		   .statusCode(400)
		   .body("$", hasSize(1))
		   .body("msg", hasItem("Data da Movimenta��o deve ser menor ou igual � data atual"))
		;
	}
	 
	 
	 @Test
		public void t08_shouldNotRemoceAccountWithTransactions() {
	     given()
	       .pathParam("id", CONTA_ID)
		.when()
		   .delete("/contas/{id}")
		.then()
		   .statusCode(500)
		   .body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	 
	 @Test
		public void t09_shouldCalculateAccountBalance() {
	     given()
		.when()
		   .get("/saldo")
		.then()
		   .statusCode(200)
		   .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00"))
		;
	}
	 
	 @Test
		public void t10_shouldRemoveAccountTransaction() {
	     given()
	       .pathParam("id", MOV_ID)
		.when()
		   .delete("/transacoes/{id}")
		.then()
		   .statusCode(204)
		   
		;
	}
	 
		@Test
		public void t011_shouldntAcessAPIWithoutToken() {
			FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
			req.removeHeader("Authorization");
			
			given()
			.when()
			   .get("/addconta")
			.then()
			   .statusCode(401)
			;
			
		}
	 
	 private Transactions getValidTransaction() {
		 Transactions tran = new Transactions();
		 tran.setAccount_id(CONTA_ID);
		 //tran.setUser_id(user_id);
		 tran.setDescription("Description Transaction");
		 tran.setInvolved("Envolved Trasaction");
		 tran.setType("REC");
		 tran.setTransaction_date(DataUtils.getDataDiferencaDias(-1));
		 tran.setPayment_date(DataUtils.getDataDiferencaDias(5));
		 tran.setValue(100f);
		 tran.setStatus(true);
		 return tran;
	 }
	 
	 
	
	
	
	
}
