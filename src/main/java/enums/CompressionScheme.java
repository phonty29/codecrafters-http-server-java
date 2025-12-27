package enums;

import java.util.Optional;

public enum CompressionScheme {
  GZIP("gzip");

  private final String name;

  CompressionScheme(String name) {
    this.name = name;
  }

  public static boolean supports(String name) {
    for (var scheme : values()) {
      if (scheme.name.contentEquals(name)) {
        return true;
      }
    }

    return false;
  }

  public static Optional<CompressionScheme> fromName(String name) {
    for (var scheme : values()) {
      if (scheme.name.contentEquals(name)) {
        return Optional.of(scheme);
      }
    }
    return Optional.empty();
  }

  public String getName() {
    return this.name;
  }
}
