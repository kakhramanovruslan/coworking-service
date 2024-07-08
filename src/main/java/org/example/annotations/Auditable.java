package org.example.annotations;

import org.example.entity.types.ActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods for auditing.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Auditable {

    /**
     * Specifies the action type for auditing.
     *
     * @return ActionType enum value representing the action type.
     */
    ActionType actionType();

    /**
     * Specifies the username associated with the auditing action.
     *
     * @return Username associated with the action (default: "").
     */
    String username() default "";
}