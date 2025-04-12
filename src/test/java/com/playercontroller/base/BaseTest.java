package com.playercontroller.base;

import com.playercontroller.config.ConfigLoader;
import com.playercontroller.utils.AllureLoggingFilter;
import com.playercontroller.utils.Specifications;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setup() {
        RestAssured.defaultParser = Parser.JSON;
        Specifications.initSpec();
    }

    @AfterMethod
    public void tearDown() {
        Specifications.removeSpec();
    }

}