package utils;

import enums.CompressionScheme;
import java.util.EnumMap;
import java.util.Map;

public class CompressorService {
  private static final Map<CompressionScheme, Compressor> compressorMap = new EnumMap<>(CompressionScheme.class);
  static {
    compressorMap.put(CompressionScheme.GZIP, new GZIPCompressor());
  }

  public static byte[] compress(CompressionScheme scheme, String input) {
    Compressor compressor = compressorMap.get(scheme);
    if (compressor == null) {
      throw new IllegalArgumentException("Unsupported compression scheme: " + scheme);
    }
    return compressor.compress(input);
  }
}
