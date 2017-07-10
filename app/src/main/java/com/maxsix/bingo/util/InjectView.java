package com.maxsix.bingo.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kchang
 * @E-mail:kchang@163.com
 */
@Target(ElementType.FIELD)//属性
@Retention(RetentionPolicy.RUNTIME)//运行时执行
@Documented
public @interface InjectView {
    public int value() default -1;
}
