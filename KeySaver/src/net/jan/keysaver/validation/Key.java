/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import net.jan.keysaver.validator.KeyValidator;

/**
 *
 * @author janhorak
 */
@Constraint( validatedBy = KeyValidator.class )
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Key
{
  String message() default "Key is invalid!";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}