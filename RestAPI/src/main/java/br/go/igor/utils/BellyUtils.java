package br.go.igor.utils;

import io.restassured.RestAssured;

public class BellyUtils {

	public static Integer getIdAccountByName(String name) {
		return RestAssured.get("/contas?nome"+name).then().extract().path("id[0]");
	}
}
