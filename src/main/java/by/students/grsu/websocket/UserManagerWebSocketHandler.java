package by.students.grsu.websocket;

import by.students.grsu.entities.services.interfaces.UserService;
import by.students.grsu.entities.users.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserManagerWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private UserService userService;
    private Gson gson = new Gson();
    private Map<WebSocketSession, List<User>> sessions = new HashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session,new ArrayList<>());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String[] messageSplit = message.getPayload().toString().split(" ");
        String request = messageSplit[0];
        String[] parameters = new String[messageSplit.length-1];
        for(int i=1;i<messageSplit.length;i++)
            parameters[i-1]=messageSplit[i];
        switch (request){
            case "search":{
                sessions.replace(session,userService.searchUsers(parameters));
                session.sendMessage(new TextMessage("RC " + sessions.get(session).size()));                        //RC - results count
                break;
            }
            case "getPage":{
                int pageSize = Integer.parseInt(parameters[0]);
                int pageNumber = Integer.parseInt(parameters[1]);
                int listLength = sessions.get(session).size();
                if(pageSize * (pageNumber - 1)>listLength){
                    session.sendMessage(new TextMessage("SE Out of size"));                                         //SE - say error
                    break;
                }
                List<User> usersPage = sessions.get(session).subList(pageSize * (pageNumber - 1),
                        Math.min(pageSize * (pageNumber), listLength));
                session.sendMessage(new TextMessage("RP "+ gson.toJson(usersPage)));                                //RP - render page
                break;
            }
            case "changeRole": {
                String username = parameters[0];
                String newRole = parameters[1];
                if (newRole.equals("admin") || newRole.equals("buyer") || newRole.equals("seller"))
                    try {
                        userService.changeRole(username, newRole);
                        session.sendMessage(new TextMessage("OS"));                                                 //OS - operation success
                    } catch (Exception e) {
                        session.sendMessage(new TextMessage("SE " + e.getMessage()));
                        break;
                    }
                else session.sendMessage(new TextMessage("SE Wrong role name"));
                break;
            }
            case "banUser":{
                String username = parameters[0];
                userService.banUser(username);
                session.sendMessage(new TextMessage("OS"));
                break;
            }
            case "unbanUser":{
                String username = parameters[0];
                userService.unbanUser(username);
                session.sendMessage(new TextMessage("OS"));
                break;
            }
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
