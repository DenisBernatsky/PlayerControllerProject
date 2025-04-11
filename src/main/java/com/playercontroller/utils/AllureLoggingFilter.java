package com.playercontroller.utils;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class AllureLoggingFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        // String for logging full request and response details
        StringBuilder fullLog = new StringBuilder();

        // Log request information
        fullLog.append("Request Method: ").append(requestSpec.getMethod()).append("\n");
        fullLog.append("Request URI: ").append(requestSpec.getURI()).append("\n");
        fullLog.append("Request Headers: ").append(requestSpec.getHeaders()).append("\n");
        fullLog.append("Request Parameters: ").append(requestSpec.getQueryParams()).append("\n");

        // Checking if the request body is present, as it may not always be provided
        String body = Optional.ofNullable(requestSpec.getBody()).map(String::valueOf).orElse("No body content");
        fullLog.append("Request Body: ").append(body).append("\n");
        fullLog.append("-------------------------------------------------\n");

        // Execute the request
        Response response = ctx.next(requestSpec, responseSpec);

        // Log response information
        fullLog.append("Response Status Code: ").append(response.getStatusCode()).append("\n");
        fullLog.append("Response Headers: ").append(response.getHeaders()).append("\n");
        fullLog.append("Response Body: ").append(response.getBody().asString()).append("\n");

        // Add the full log as a single attachment in Allure report
        Allure.addAttachment("Request and Response Log", "text/plain",
                new ByteArrayInputStream(fullLog.toString().getBytes(StandardCharsets.UTF_8)),
                "txt");

        return response;
    }
}
