package kg.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson45.Lesson45Server;
import kg.attractor.java.model.EmployeeAuth;
import kg.attractor.java.server.Cookie;

import java.io.IOException;
import java.util.*;

public class Lesson46Server extends Lesson45Server {

    private final Map<String, EmployeeAuth> sessions = new HashMap<>();

    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/profile", this::profileGet46);
        registerGet("/logout",  this::logoutGet);
    }

    @Override
    protected void loginPost(HttpExchange ex){
        super.loginPost(ex);

        if (currentUser == null)
            return;

        String sid = UUID.randomUUID().toString();
        sessions.put(sid, currentUser);

        Cookie sidCookie = Cookie.of("SID", sid)
                .maxAge(600)
                .httpOnly();

        setCookie(ex, sidCookie);
    }

    private void profileGet46(HttpExchange ex){
        Map<String,Object> data = new HashMap<>();

        EmployeeAuth u = sessions.get(readSid(ex));
        if (u != null){
            data.put("email",    u.getEmail());
            data.put("fullname", u.getFullName());
        }else{
            data.put("email",    "anonymous@office");
            data.put("fullname", "Некий пользователь");
        }
        renderTemplate(ex, "profile.ftlh", data);
    }

    private void logoutGet(HttpExchange ex){
        String sid = readSid(ex);
        if (sid != null) sessions.remove(sid);

        setCookie(ex, Cookie.of("SID", "deleted").maxAge(0).httpOnly());
        redirect303(ex, "/login");
    }

    private String readSid(HttpExchange ex){
        return Cookie.parse(getCookies(ex)).get("SID");
    }
}