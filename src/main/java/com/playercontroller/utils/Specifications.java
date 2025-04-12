package com.playercontroller.utils;

import com.playercontroller.config.ConfigLoader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {

    private static final ThreadLocal<RequestSpecification> threadSpec = new ThreadLocal<>();

    public static void initSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigLoader.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new AllureLoggingFilter())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL));

        threadSpec.set(builder.build());
    }

    public static RequestSpecification getSpec() {
        RequestSpecification spec = threadSpec.get();
        if (spec == null) {
            throw new IllegalStateException("RequestSpecification is not initialized. Did you forget to call Specifications.initSpec()?");
        }
        return spec;
    }

    public static void removeSpec() {
        threadSpec.remove();
    }
}
