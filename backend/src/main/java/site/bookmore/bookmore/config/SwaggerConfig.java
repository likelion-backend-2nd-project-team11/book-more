package site.bookmore.bookmore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import site.bookmore.bookmore.common.dto.Paging;
import site.bookmore.bookmore.common.support.annotation.Authorized;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(HttpAuthenticationScheme.JWT_BEARER_BUILDER
                        .name("JWT")
                        .description("Bearer를 제외한 토큰(JWT)만 입력해주세요.")
                        .build()))
                .securityContexts(Arrays.asList(securityContext()))
                .alternateTypeRules(alternateTypeRules())
                .select()
                .apis(RequestHandlerSelectors.basePackage("site.bookmore.bookmore"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("BookMore API Document")
                .description("BookMore API 명세서입니다.")
                .version("1.0.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(o -> o.findAnnotation(Authorized.class).isPresent())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        return Arrays.asList(SecurityReference.builder()
                .scopes(new AuthorizationScope[0])
                .reference("JWT")
                .build());
    }

    private AlternateTypeRule alternateTypeRules() {
        return AlternateTypeRules.newRule(Pageable.class, Paging.class);
    }
}
