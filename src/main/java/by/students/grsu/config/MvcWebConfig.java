package by.students.grsu.config;

import by.students.grsu.entities.dao.*;
import by.students.grsu.entities.services.*;
import by.students.grsu.websocket.UserSessionService;
import by.students.grsu.websocket.WebSocketHandler;
import com.mysql.cj.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@EnableWebMvc
@ComponentScan("by.students.grsu.controller")
public class MvcWebConfig implements WebMvcConfigurer {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /*
     * STEP 1 - Create SpringResourceTemplateResolver
     * */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/scripts/**","/css/**")
                .addResourceLocations("/WEB-INF/scripts/","/WEB-INF/css/");
    }

    @Bean
    public FollowedAuctionService followedAuctionService(FollowedAuctionDao followedAuctionDao){
        return new FollowedAuctionService(followedAuctionDao);
    }
    @Bean
    public AuctionService auctionService(AuctionDao auctionDao, ItemService itemService/*, LotService lotService*/){
        return new AuctionService(auctionDao, itemService/*, lotService*/);
    }
    @Bean
    public ItemService itemService(ItemDao itemDao){
        return new ItemService(itemDao);
    }
    @Bean
    public LotService lotService(LotDao lotDao,AuctionService auctionService,ItemService itemService){
        return new LotService(lotDao,auctionService,itemService);
    }
    @Bean
    public AuctionConfiguration auctionConfiguration(){
        return new AuctionConfiguration();
    }
    @Bean
    public UserService userService(UserDao userDao){
        return new UserService(userDao);
    }
    @Bean
    public SoldLotService soldLotService(SoldLotDao soldLotDao, LotService lotService){
        return new SoldLotService(soldLotDao,lotService);
    }
    @Bean
    public AuctionPlatform auctionPlatform(AuctionService auctionService, SoldLotService soldLotService, LotService lotService, WebSocketHandler handler){
        return new AuctionPlatform(auctionService,soldLotService,lotService,handler);
    }
    @Bean
    public UserSessionService getUserSessionService(){
        return new UserSessionService();
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)///////////////////////////////////////////////////////////////
    public Statement statement(AuctionConfiguration configuration) throws Exception {
        try {
            DriverManager.registerDriver(new Driver());
            Statement statement =
                    DriverManager.getConnection("jdbc:mysql://"+configuration.getDaoLocation(),configuration.getDaoUser(),configuration.getDaoPassword()).createStatement();
            //statement.execute("use datchauction");
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new Exception("Connection with database failed");
    }
    @Bean
    public AuctionDao auctionDao(Statement statement,LotDao lotDao){
        return new AuctionDao(statement,lotDao);
    }
    @Bean
    public ItemDao itemDao(Statement statement){
        return new ItemDao(statement);
    }
    @Bean
    public LotDao lotDao(Statement statement,ItemDao itemDao){
        return new LotDao(statement,itemDao);
    }
    @Bean
    public UserDao userDao(Statement statement){
        return new UserDao(statement);
    }
    @Bean
    public SoldLotDao soldLotDao(Statement statement){return new SoldLotDao(statement);}
    @Bean
    public FollowedAuctionDao followedAuctionDao(Statement statement){return new FollowedAuctionDao(statement);}

    /*
     * STEP 2 - Create SpringTemplateEngine
     * */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addDialect(new SpringSecurityDialect());
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /*
     * STEP 3 - Register ThymeleafViewResolver
     * */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
}