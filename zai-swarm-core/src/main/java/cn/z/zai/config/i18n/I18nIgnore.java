package cn.z.zai.config.i18n;

import java.lang.annotation.*;

/**
 * @Description: ignore selection method
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nIgnore {}
