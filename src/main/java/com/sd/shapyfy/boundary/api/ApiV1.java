package com.sd.shapyfy.boundary.api;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//
@RestController
@RequestMapping
public @interface ApiV1 {

    @AliasFor(annotation = RequestMapping.class)
    String value() default "/v1";
}
