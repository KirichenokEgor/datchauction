package by.students.grsu.config;

import by.students.grsu.entities.dao.implementations.*;
import by.students.grsu.entities.dao.interfaces.*;
import by.students.grsu.entities.services.implementations.*;
import by.students.grsu.entities.services.interfaces.*;
import by.students.grsu.entities.services.interfaces.followersAndObservers.DealsFollower;
import by.students.grsu.websocket.ActiveAuctionWebSocketHandler;
import by.students.grsu.websocket.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan("by.students.grsu.controller")
public class MvcWebConfig implements WebMvcConfigurer {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
       // slr.setDefaultLocale(Locale.ENGLISH);
        slr.setDefaultLocale(new Locale("ru"));
        return slr;
    }
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename("messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
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
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/scripts/**","/css/**")
                .addResourceLocations("/WEB-INF/scripts/","/WEB-INF/css/");
    }

    /*SERVICES*/

    @Bean
    public FollowedAuctionService followedAuctionService(FollowedAuctionDao followedAuctionDao){
        return new DefaultFollowedAuctionService(followedAuctionDao);
    }
    @Bean
    public AuctionService auctionService(AuctionDao auctionDao, ItemService itemService){
        return new DefaultAuctionService(auctionDao, itemService);
    }
    @Bean
    public ItemService itemService(ItemDao itemDao){
        return new DefaultItemService(itemDao);
    }
    @Bean
    public LotService lotService(LotDao lotDao, AuctionService auctionService, ItemService itemService){
        return new DefaultLotService(lotDao,auctionService,itemService);
    }
//    @Bean
//    public AuctionConfiguration auctionConfiguration(){
//        return new AuctionConfiguration();
//    }
    @Bean
    public UserService userService(UserDao userDao){
        return new DefaultUserService(userDao);
    }
    @Bean
    public SoldLotService soldLotService(SoldLotDao soldLotDao, DealsFollower lotService){
        return new DefaultSoldLotService(soldLotDao, lotService);
    }
    @Bean
    public AuctionPlatform auctionPlatform(AuctionService auctionService, SoldLotService soldLotService, LotService lotService, FollowedAuctionService followedAuctionService, ActiveAuctionWebSocketHandler handler){
        return new DefaultAuctionPlatform(auctionService, soldLotService, lotService, followedAuctionService, handler);
    }
    @Bean
    public UserSessionService getUserSessionService(){
        return new UserSessionService();
    }

    /*DAOS*/

//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)///////////////////////////////////////////////////////////////
//    public Statement statement(AuctionConfiguration configuration) throws Exception {
//        try {
//            DriverManager.registerDriver(new Driver());
//            Statement statement =
//                    DriverManager.getConnection("jdbc:mysql://"+configuration.getDaoLocation(),configuration.getDaoUser(),configuration.getDaoPassword()).createStatement();
//            //statement.execute("use datchauction");
//            return statement;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        throw new Exception("Connection with database failed");
//    }
    @Bean
    public AuctionDao auctionDao(JdbcTemplate jdbcTemplate){
        return new MySqlAuctionDao(jdbcTemplate);
    }
    @Bean
    public ItemDao itemDao(JdbcTemplate jdbcTemplate){
        return new MySqlItemDao(jdbcTemplate);
    }
    @Bean
    public LotDao lotDao(JdbcTemplate jdbcTemplate){
        return new MySqlLotDao(jdbcTemplate);
    }
    @Bean
    public UserDao userDao(JdbcTemplate jdbcTemplate){
        return new MySqlUserDao(jdbcTemplate);
    }
    @Bean
    public SoldLotDao soldLotDao(JdbcTemplate jdbcTemplate){return new MySqlSoldLotDao(jdbcTemplate);}
    @Bean
    public FollowedAuctionDao followedAuctionDao(JdbcTemplate jdbcTemplate){return new MySqlFollowedAuctionDao(jdbcTemplate);}

    /*DATASOURCE*/

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");//"com.mysql.jdbc.Driver"
        dataSource.setUrl("jdbc:mysql://localhost/dutchAuction");
        dataSource.setUsername("defaultDao");
        dataSource.setPassword("1111");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

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
        /* Браузер загружает страницу в windows-1252 и кириллица ломается.
           Если вручную в браузере поставить UTF-8, то всё работает нормально.
           Вот эта строка заставляет браузер изначально ставить кодировку на UTF-8,
           но кириллица всё равно не работает!!!     */
        resolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(resolver);
    }

}