package enums;

import java.util.EnumSet;
import java.util.Set;

public enum HttpVersion {
  HTTP_0_9("HTTP/0.9", EnumSet.of(HttpMethod.GET)),
  HTTP_1("HTTP/1.0", EnumSet.of(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST)),
  HTTP_1_1("HTTP/1.1", EnumSet.allOf(HttpMethod.class)),
  HTTP_2("HTTP/2", EnumSet.allOf(HttpMethod.class)),
  HTTP_3("HTTP/3", EnumSet.allOf(HttpMethod.class));


  private final String version;
  private final Set<HttpMethod> methods;

  HttpVersion(String version, Set<HttpMethod> methods) {
    this.version = version;
    this.methods = methods;
  }

  public static HttpVersion fromValue(String version) {
    for (var val : HttpVersion.values()) {
      if (val.version.contentEquals(version)) {
        return val;
      }
    }
    throw new IllegalArgumentException();
  }

  public String toString() {
    return this.version;
  }

  public boolean supportsMethod(HttpMethod method) {
    System.out.println("Log: does not support method: %s" + method);
    System.out.println("Log: supported methods: %s" + this.methods);
    return this.methods.contains(method);
  }
}
