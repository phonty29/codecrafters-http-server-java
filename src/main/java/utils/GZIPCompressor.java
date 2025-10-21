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
      gzos.flush();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    return baos.toByteArray();
  }
}
