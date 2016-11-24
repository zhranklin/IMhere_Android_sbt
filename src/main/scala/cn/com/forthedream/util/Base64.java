package cn.com.forthedream.util;

import java.io.UnsupportedEncodingException;

public class Base64 {

  public static String encodeToString(byte[] input) {
    try {
      int tailLen = 0;
      int count = -1;

      final byte[] alphabet = new byte[]{
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/',
      };
      int output_len = input.length / 3 * 4;

      if (input.length % 3 > 0) {
        output_len += 4;
      }

      byte[] output = new byte[output_len];
      int len = input.length;
      int op = 0;
      int p = 0;
      int v = -1;

      switch (tailLen) {
        case 0:
          break;

        case 1:
          if (p + 2 <= len) {
            v = ((input[p++] & 0xff) << 8) |
              (input[p++] & 0xff);
            tailLen = 0;
          }
          ;
          break;

        case 2:
          if (p + 1 <= len) {
            v = input[p++] & 0xff;
            tailLen = 0;
          }
          break;
      }

      while (p + 3 <= len) {
        v = ((input[p] & 0xff) << 16) |
          ((input[p + 1] & 0xff) << 8) |
          (input[p + 2] & 0xff);
        output[op] = alphabet[(v >> 18) & 0x3f];
        output[op + 1] = alphabet[(v >> 12) & 0x3f];
        output[op + 2] = alphabet[(v >> 6) & 0x3f];
        output[op + 3] = alphabet[v & 0x3f];
        p += 3;
        op += 4;
        if (--count == 0) {
          output[op++] = '\n';
          count = 19;
        }
      }

      if (p - tailLen == len - 1) {
        int t = 0;
        v = (input[p++] & 0xff) << 4;
        tailLen -= t;
        output[op++] = alphabet[(v >> 6) & 0x3f];
        output[op++] = alphabet[v & 0x3f];
        output[op++] = '=';
        output[op++] = '=';
      } else if (p - tailLen == len - 2) {
        int t = 0;
        v = (input[p++] & 0xff) << 10 | (input[p++] & 0xff) << 2;
        tailLen -= t;
        output[op++] = alphabet[(v >> 12) & 0x3f];
        output[op++] = alphabet[(v >> 6) & 0x3f];
        output[op++] = alphabet[v & 0x3f];
        output[op++] = '=';
      }

      return new String(output, "US-ASCII");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }
}
