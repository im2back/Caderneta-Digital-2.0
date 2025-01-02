package im2back.com.github.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

	@Bean
	RouteLocator routes(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/products/**").uri("lb://stockms"))
				.route(r -> r.path("/customers/**").uri("lb://customerms"))
				.route(r -> r.path("/orchestrator/**").uri("lb://orchestratorms"))
				.route(r -> r.path("/validations/**").uri("lb://validation"))
				.build();
	}

}
