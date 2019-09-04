package by.students.grsu.config;
import by.students.grsu.websocket.ActiveAuctionWebSocketHandler;
import by.students.grsu.websocket.UserManagerWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
@ComponentScan("by.students.grsu.websocket")
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ActiveAuctionWebSocketHandler activeAuctionWebSocketHandler;
    @Autowired
    private UserManagerWebSocketHandler userManagerWebSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(activeAuctionWebSocketHandler, "/activeAuction").
                addHandler(userManagerWebSocketHandler, "/usersManager");
    }

}