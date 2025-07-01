package kg.attractor.java.server;

import kg.attractor.java.utils.Utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class Cookie<V> {

  private final String name;
  private final V value;
  private Integer maxAge;
  private boolean httpOnly;


  public Cookie(String name, V value) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(value);
    this.name = name.strip();
    this.value = value;
  }

  public static <V> Cookie<V> of(String name, V value) {
    return new Cookie<>(name, value);
  }

  public Cookie<V> maxAge(int seconds) {
    this.maxAge = seconds;
    return this;
  }
  public Cookie<V> httpOnly() {
    this.httpOnly = true;
    return this;
  }


  public Integer getMaxAge()      { return maxAge; }
  public String  getName()        { return name;   }
  public V       getValue()       { return value;  }
  public boolean isHttpOnly()     { return httpOnly; }


  public static Map<String,String> parse(String cookieString) {
    return Utils.parseUrlEncoded(cookieString, ";");
  }

  @Override public String toString() {
    String encName  = URLEncoder.encode(name.strip(),  StandardCharsets.UTF_8);
    String encValue = URLEncoder.encode(value.toString(), StandardCharsets.UTF_8);

    StringBuilder sb = new StringBuilder(encName + '=' + encValue);
    if (maxAge != null) sb.append("; Max-Age=").append(maxAge);
    if (httpOnly)       sb.append("; HttpOnly");
    return sb.toString();
  }
}