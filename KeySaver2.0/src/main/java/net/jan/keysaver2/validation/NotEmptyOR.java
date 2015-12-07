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
import net.jan.keysaver2.validator.NotEmptyOrValidator;

/**
 *
 * @author janhorak
 */
@Constraint( validatedBy = NotEmptyOrValidator.class )
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface NotEmptyOR
{
  String message() default "Both are / aren't empty!";
  String[] fields();
  String depedencyField();
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
