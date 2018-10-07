package com.keyroy.util.json;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { FIELD, TYPE, METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAn {
	public String value() default "";

	public boolean primaryKey() default false;

	public boolean skip() default false;

}
