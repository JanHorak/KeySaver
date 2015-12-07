/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import net.jan.keysaver2.validator.EqualsValidator;


/**
 *
 * @author janhorak
 */
@Constraint( validatedBy = EqualsValidator.class )
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD})
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Equals
{
  String message() default "Not Equals!";
  String first();
  String second();
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
