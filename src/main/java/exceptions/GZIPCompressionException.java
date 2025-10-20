package exceptions;

public class GZIPCompressionException extends RuntimeException {

  public GZIPCompressionException(String message) {
    super("GZIP compression failed for string: " + message);
  }

  public GZIPCompressionException(byte[] in) {
    super("GZIP compression failed for byte array: " + (in != null ? in.length : 0));
  }
}
