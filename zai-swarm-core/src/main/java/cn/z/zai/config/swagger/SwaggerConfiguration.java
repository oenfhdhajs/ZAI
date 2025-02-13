package cn.z.zai.config.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 */
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements ApplicationListener<ApplicationEvent> {

    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public Docket createRestApi() {
        if (env.equals("sit")) {
            Docket build = new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("cn.z.zai"))
                    .paths(PathSelectors.any())
                    .build();
            List<Parameter> pars = new ArrayList<>();
            ParameterBuilder tokenPar = new ParameterBuilder();
            tokenPar.name("token").description("auth token").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
            ParameterBuilder languagePar = new ParameterBuilder();
            languagePar.name("lang").description("user choose language").modelRef(new ModelRef("string")).parameterType("header").required(true).build();

            ParameterBuilder nonce = new ParameterBuilder();
            nonce.name("nonce").description("nonce").modelRef(new ModelRef("string")).parameterType("header").required(true).build();

            ParameterBuilder timestamp = new ParameterBuilder();
            timestamp.name("timestamp").description("timestamp").modelRef(new ModelRef("string")).parameterType("header").required(true).build();

            ParameterBuilder aesKey = new ParameterBuilder();
            aesKey.name("aesKey").description("aesKey").modelRef(new ModelRef("string")).parameterType("header").required(true).build();

            ParameterBuilder sign = new ParameterBuilder();
            sign.name("sign").description("sign").modelRef(new ModelRef("string")).parameterType("header").required(true).build();

            ParameterBuilder appVersion = new ParameterBuilder();
            appVersion.name("appVersion").description("appVersion").modelRef(new ModelRef("string")).parameterType("header").required(false).build();


            pars.add(tokenPar.build());
            pars.add(languagePar.build());

            pars.add(nonce.build());
            pars.add(timestamp.build());
            pars.add(aesKey.build());
            pars.add(sign.build());
            pars.add(appVersion.build());

            build.globalOperationParameters(pars);
            return build;
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.none())
                .paths(PathSelectors.none())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Developer", "", "");
        return new ApiInfoBuilder()
                .title("ZAISwarmCore API")
                .description("ZAISwarmCore API Manager")
                .version("1.0")
                .contact(contact)
                .build();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextRefreshedEvent){
            log.info("swagger finished path:[http:127.0.0.1:8080/zai-swarm-core/swagger-ui.html]");
        }
    }
}
