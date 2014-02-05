/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import net.jan.keysaver.validator.UniqueInCategoryListValidator;

/**
 *
 * @author janhorak
 */
@Constraint( validatedBy = UniqueInCategoryListValidator.class )
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.TYPE})
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface UniqueInCategoryList
{
  String message() default "Not Unique!";
  String uniqueField();
  String iconField();
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
