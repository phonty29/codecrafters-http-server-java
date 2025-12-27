package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZIPCompressor implements Compressor {

  public byte[] compress(String str) {
    if (str == null || str.isEmpty()) {
      return new byte[0];
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (
        GZIPOutputStream gzos = new GZIPOutputStream(baos)
    ) {
      gzos.write(str.getBytes());
      baos.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    return baos.toByteArray();
  }
}
