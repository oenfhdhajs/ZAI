package cn.z.zai.config.ratelimiter;

import java.lang.annotation.*;

/**
 * @Description: ignore selection method
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessInterceptor {


    /** key */
    String key() default "all";

    /** request Per Second */
    double permitsPerSecond();

    /** black  0:not limit */
    double blacklistCount() default 0;

    /** black cool seconds */
    long blacklistDurationSeconds() default 24 * 3600; //

    /** desc */
    String desc() default "Request rate limited.";

}
