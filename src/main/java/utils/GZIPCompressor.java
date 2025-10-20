package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class GZIPCompressor {
  public static byte[] compress(String str) {
    if (str == null || str.isEmpty()) {
      return new byte[0];
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
      gzos.write(str.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    return baos.toByteArray();
  }

  public static byte[] compress(byte[] in) {
    if (in == null || in.length == 0) {
      return new byte[0];
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
      gzos.write(in);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    return baos.toByteArray();
  }
}
