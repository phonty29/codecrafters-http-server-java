package enums;

import java.util.EnumSet;
import java.util.Set;

public enum HttpVersion {
  HTTP_0_9("HTTP/0.9", EnumSet.of(HttpMethod.GET), 0),
  HTTP_1("HTTP/1.0", EnumSet.of(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST), 1),
  HTTP_1_1("HTTP/1.1", EnumSet.allOf(HttpMethod.class), 2),
  HTTP_2("HTTP/2", EnumSet.allOf(HttpMethod.class), 3),
  HTTP_3("HTTP/3", EnumSet.allOf(HttpMethod.class), 4);


  private final String version;
  private final Set<HttpMethod> methods;
  private final int hierarchy;

  HttpVersion(String version, Set<HttpMethod> methods, int hierarchy) {
    this.version = version;
    this.methods = methods;
    this.hierarchy = hierarchy;
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
    return this.methods.contains(method);
  }

  public boolean isGreaterOrEquals(HttpVersion version) {
    return this.hierarchy >= version.hierarchy;
  }
}
