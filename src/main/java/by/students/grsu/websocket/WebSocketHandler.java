package by.students.grsu.websocket;


import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.auction.ActiveAuctionInterface;
import by.students.grsu.entities.users.Follower;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketHandler extends TextWebSocketHandler implements Follower {
    private Gson gson = new Gson();
    private Map<WebSocketSession,Integer> sessions = new HashMap<WebSocketSession,Integer>();
    private Map<WebSocketSession, String> users = new HashMap<WebSocketSession, String>();
    private Map<Integer, ActiveAuctionInterface> auctions = new HashMap<Integer, ActiveAuctionInterface>();
    private UserSessionService sessionService;
    @Autowired
    public void setSessionService(UserSessionService sessionService){
        this.sessionService = sessionService;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.put(session,sessionService.getNextUser());
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        users.remove(session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String[] clientMessage = message.getPayload().split(" ");
        switch (clientMessage[0]) {
            case "joinAuction":
                if(!sessions.containsKey(session)) {
                    sessions.put(session, Integer.parseInt(clientMessage[1]));
                    sendAuction(Integer.parseInt(clientMessage[1]));
                }else session.sendMessage(new TextMessage("SE This session is already registered"));
                break;
            case "getAuction":
                if(sessions.containsKey(session))
                sendAllAuctions();
                else session.sendMessage(new TextMessage("SE This session is not registered"));
                break;
            case "buyLot":
                int lotId = Integer.parseInt(clientMessage[1]);
                try{
                    if(sessions.containsKey(session) && users.containsKey(session)) {
                        //auctions.get(session).buyLot(lotId, users.get(session).getUsername());
                        auctions.get(sessions.get(session)).buyLot(lotId,users.get(session));
                        session.sendMessage(new TextMessage("LB " + lotId));
                    }else session.sendMessage(new TextMessage("SE This session is not registered"));
                    }catch (Exception e){
                        e.printStackTrace();
                }
                break;
            default:
                session.sendMessage(new TextMessage("WC"));
        }
    }
    private void sendAuction(int auctionId){
        for(WebSocketSession session : sessions.keySet()){
            if(sessions.get(session)==auctionId) {
                try {
                    session.sendMessage(new TextMessage("RA " + gson.toJson(auctions.get(auctionId).getAuction())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendAllAuctions(){
        for(WebSocketSession session : sessions.keySet()){
                try {
                    session.sendMessage(new TextMessage("RA "+gson.toJson(auctions.get(sessions.get(session)).getAuction())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    public void auctionStarted(ActiveAuction auction){
        auction.join(this);
        auctions.put(auction.getAuctionId(),auction);
    }
    @Override
    public void tickHappened(int auctionId) {
        sendAuction(auctionId);
    }

    @Override
    public void auctionEnded(int auctionId) {
        for(WebSocketSession session : sessions.keySet()){
            if(sessions.get(session)==auctionId) {
                try {
                    session.sendMessage(new TextMessage("AE"));
                    auctions.remove(auctionId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void lotSold(int auctionId) {
        sendAuction(auctionId);
    }
}