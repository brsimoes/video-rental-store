package com.casumo.videorentalstore.configuration;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.time.Clock;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class AppConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(paths())
				.build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Video Rental Store API", "Video Rental Store sample for Casumo code test.", "V1", "",
				new Contact("Bruno Dias", "", "brsimoes@gmail.com"), "Apache 2.0",
				"http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
	}

	@SuppressWarnings("unchecked")
	private Predicate<String> paths() {
		return or(regex("/v1.*"));
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}
