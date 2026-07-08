package br.com.sea.tecnologia.desafio.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TelefoneValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TelefoneValido {
    String message() default "Número de telefone inválido para o tipo informado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}