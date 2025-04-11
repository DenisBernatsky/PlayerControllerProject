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
        // Строка для логирования полного запроса и ответа
        StringBuilder fullLog = new StringBuilder();

        // Логирование информации о запросе
        fullLog.append("Request Method: ").append(requestSpec.getMethod()).append("\n");
        fullLog.append("Request URI: ").append(requestSpec.getURI()).append("\n");
        fullLog.append("Request Headers: ").append(requestSpec.getHeaders()).append("\n");
        fullLog.append("Request Parameters: ").append(requestSpec.getQueryParams()).append("\n");
        fullLog.append("Request Body: ").append(Optional.ofNullable(requestSpec.getBody())).append("\n");
        fullLog.append("-------------------------------------------------\n");

        // Выполнение запроса
        Response response = ctx.next(requestSpec, responseSpec);

        // Логирование информации о ответе
        fullLog.append("Response Status Code: ").append(response.getStatusCode()).append("\n");
        fullLog.append("Response Headers: ").append(response.getHeaders()).append("\n");
        fullLog.append("Response Body: ").append(response.getBody().asString()).append("\n");

        // Добавляем весь лог в отчет Allure как одно вложение
        Allure.addAttachment("Request and Response Log", "text/plain",
                new ByteArrayInputStream(fullLog.toString().getBytes(StandardCharsets.UTF_8)),
                "txt");

        return response;
    }
}
