package exceptions;

import enums.HttpVersion;
import java.util.Arrays;

public class HttpVersionNotSupported extends RuntimeException {

  public HttpVersionNotSupported(String version) {
    super(String.format("HTTP version %s not supported. Supported versions are: [%s]", version,
        Arrays.toString(HttpVersion.values())));
  }
}
