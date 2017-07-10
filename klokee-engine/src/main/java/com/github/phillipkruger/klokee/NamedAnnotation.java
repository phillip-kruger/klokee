package com.github.phillipkruger.klokee;

/**
 * Allow us to dynamically look up Named CDI beans
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 * @see https://stackoverflow.com/questions/24798529/how-to-programmatically-inject-a-java-cdi-managed-bean-into-a-local-variable-in
 */
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class NamedAnnotation extends AnnotationLiteral<Named> implements Named {

     private final String value;

     public NamedAnnotation(final String value) {
         this.value = value;
     }

     public String value() {
        return value;
    }
}