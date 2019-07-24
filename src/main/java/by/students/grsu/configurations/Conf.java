package by.students.grsu.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

@Configuration
public class Conf {
    @Bean
    @Description("Thymeleaf Template Resolver")
    public ServletContextTemplateResolver templateResolver(final ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setPrefix("/WEB-INF/jsp/");
        templateResolver.setSuffix(".jsp");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }

    @Bean
    @Description("Thymeleaf Template Engine")
    public SpringTemplateEngine templateEngine(final ServletContext servletContext) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver(servletContext));
        return templateEngine;
    }
}
