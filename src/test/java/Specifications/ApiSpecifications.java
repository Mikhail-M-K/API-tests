package Specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static Utils.ConfProperties.getProperty;


public class ApiSpecifications {
    public static final RequestSpecification REQUEST_SPECIFICATION_R_A_M = new RequestSpecBuilder()
                .setBaseUri(getProperty("pageRickAndMortyApi"))
                .build();
}
