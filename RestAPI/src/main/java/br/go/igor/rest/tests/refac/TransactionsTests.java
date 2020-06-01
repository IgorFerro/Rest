package br.go.igor.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.go.igor.rest.core.BaseTest;
import br.go.igor.rest.tests.Transactions;
import br.go.igor.utils.DataUtils;
import io.restassured.RestAssured;

public class TransactionsTests extends BaseTest {
	
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
		public void  shouldInsertSuccesfullTransactions() {
		 Transactions tran = getValidTransaction();
		 
		    given()
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
		 tran.setTransaction_date(DataUtils.getDataDiferencaDias(2));
		 
	     given()
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
		 Integer CONTA_ID = getIdAccountByName("Conta com movimentacao");
		 
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
		public void shouldRemoveAccountTransaction() {
	     
		 Integer MOV_ID = getIdTransactionByDescription("Movimentacao para exclusao");
		 
		 given()
	       .pathParam("id", MOV_ID)
		.when()
		   .delete("/transacoes/{id}")
		.then()
		   .statusCode(204)
		   
		;
	}
	
	public Integer getIdAccountByName(String name) {
		return RestAssured.get("/contas?nome"+name).then().extract().path("id[0]");
	}
	
	public Integer getIdTransactionByDescription(String desc) {
		return RestAssured.get("/transacoes?descricao"+desc).then().extract().path("id[0]");
	}
	
	
	 private Transactions getValidTransaction() {
		 Transactions tran = new Transactions();
		 tran.setAccount_id(getIdAccountByName("Conta para movimentacoes"));
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
