package com.github.phillipkruger.klokee.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import javax.enterprise.util.Nonbinding;

/**
 * Allow one to inject the property value
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Qualifier
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface KlokeeProperty {

    @Nonbinding String value() default "klokee.properties";
    @Nonbinding String key() default "";
    
}