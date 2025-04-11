package com.playercontroller.base;

import com.playercontroller.config.ConfigLoader;
import com.playercontroller.utils.AllureLoggingFilter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    protected RequestSpecification spec;

    @BeforeClass
    public void setup() {
        RestAssured.defaultParser = Parser.JSON;
        spec = new RequestSpecBuilder()
                .setBaseUri(ConfigLoader.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new AllureLoggingFilter())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }
}