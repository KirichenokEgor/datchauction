package by.students.grsu.config;

import by.students.grsu.entities.dao.AuctionDao;
import by.students.grsu.entities.dao.ItemDao;
import by.students.grsu.entities.dao.LotDao;
import by.students.grsu.entities.dao.UserDao;
import by.students.grsu.entities.services.*;
import com.mysql.cj.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@EnableWebMvc
@ComponentScan("by.students.grsu.controller")
public class MvcWebConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

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
//    @Bean
//    public AuctionService auctionService(){
//        return new AuctionService();
//    }
//    @Bean
//    public ItemService itemService(){
//        return new ItemService();
//    }
//    @Bean
//    public LotService lotService(){
//        return new LotService();
//    }
//    @Bean
//    public UserService userService(){
//        return new UserService();
//    }
//    @Bean
//    public AuctionPlatform auctionPlatform(){
//        return new AuctionPlatform();
//    }
//    @Bean
//    public AuctionDao auctionDao(){
//        return new AuctionDao();
//    }
//    @Bean
//    public ItemDao itemDao(){
//        return new ItemDao();
//    }
//    @Bean
//    public LotDao lotDao(){
//        return new LotDao();
//    }
//    @Bean
//    public UserDao userDao(){
//        return new UserDao();
//    }
//    @Bean
//    public Statement st() throws AuctionException {
//        Statement st = null;
//        //"localhost","datchDBManager","ineedyourbase"
//        String address = "localhost";
//        String username = "datchDBManager";
//        String password = "ineedyourbase";
//        try{
//            DriverManager.registerDriver(new Driver());
//            Connection connection = DriverManager.getConnection("jdbc:mysql://"+address,username,password);
//            st = connection.createStatement();
//            st.execute("use datchauction");}
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//            throw new AuctionException("Internal error",0);
//        }
//        return st;
//    }
    @Bean
    public AuctionService auctionService(AuctionDao auctionDao){
    return new AuctionService(auctionDao);
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
    public AuctionPlatform auctionPlatform(){
        return new AuctionPlatform();
    }
    @Bean
    public Statement statement(AuctionConfiguration configuration) throws Exception {
        try {
            DriverManager.registerDriver(new Driver());
            Statement statement =
                    DriverManager.getConnection("jdbc:mysql://"+configuration.getDaoLocation(),configuration.getDaoUser(),configuration.getDaoPassword()).createStatement();
            statement.execute("use datchauction");
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


    /*
     * STEP 2 - Create SpringTemplateEngine
     * */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
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