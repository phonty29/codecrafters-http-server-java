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
    try (GZIPOutputStream gzos = new GZIPOutputStream(baos) {
      {
        // Set header timestamp to zero
        def.setLevel(java.util.zip.Deflater.DEFAULT_COMPRESSION);
        this.writeHeader();
      }

      // Override to zero timestamp
      private void writeHeader() throws IOException {
        byte[] header = {
            (byte) 0x1f, (byte) 0x8b, // Magic number
            (byte) 0x08,              // Compression method
            (byte) 0x00,              // Flags
            0, 0, 0, 0,               // mtime = 0
            (byte) 0x00,              // Extra flags
            (byte) 0xff               // OS = unknown
        };
        out.write(header);
      }
    }) {
      gzos.write(str.getBytes());
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
