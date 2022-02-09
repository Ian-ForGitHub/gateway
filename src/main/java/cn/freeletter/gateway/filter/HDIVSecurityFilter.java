package cn.freeletter.gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.hdiv.config.validations.DefaultValidationParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class HDIVSecurityFilter implements GlobalFilter, Ordered {

    @Value("${hdiv.validation:false}")
    private boolean hdivValidation;

    private final List<Pattern> hdivRules = new ArrayList<>();

    public HDIVSecurityFilter() {
        DefaultValidationParser parser = new DefaultValidationParser();
        parser.readDefaultValidations();
        List<Map<DefaultValidationParser.ValidationParam, String>> validations = parser.getValidations();
        validations.forEach(validationMap -> {
                    String regex = validationMap.get(DefaultValidationParser.ValidationParam.REGEX);
                    Pattern pattern = Pattern.compile(regex);
                    hdivRules.add(pattern);
                }
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (hdivValidation) {
            return null;
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
