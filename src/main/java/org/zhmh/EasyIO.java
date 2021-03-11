package org.zhmh;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EasyIO {
    public static String readTextAsUTF8(String fileName) throws IOException {
        return readTextAsUTF8(new FileInputStream(fileName));
    }
    public static String readTextAsUTF8(InputStream is) throws IOException {
        byte[] buff = new byte[is.available()];
        int size = is.read(buff);
        return new String(buff, 0, size, StandardCharsets.UTF_8);
    }
}
