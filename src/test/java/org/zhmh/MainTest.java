package org.zhmh;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import static org.zhmh.TypedString.getTemplate;

public class MainTest {
    @Test
    public void testTypedString() {
        List<TypedString> arr;
        arr = getTemplate("456${abc}123\\${ppp}qwe");
        Assert.assertEquals(arr.size(), 3);
        Assert.assertEquals(arr.get(1).value, "abc");
        Assert.assertEquals(arr.get(2).value, "123${ppp}qwe");

        arr = getTemplate("123456\\n");
        Assert.assertEquals(arr.size(), 1);
        Assert.assertEquals(arr.get(0).value, "123456\n");

        arr = getTemplate("\\n123456");
        Assert.assertEquals(arr.size(), 1);
        Assert.assertEquals(arr.get(0).value, "\n123456");

        arr = getTemplate("\\A\\n\\t\\r\\B");
        Assert.assertEquals(arr.size(), 1);
        Assert.assertEquals(arr.get(0).value, "A\n\t\rB");

        arr = getTemplate("${22}|${ x-123 }|$");
        Assert.assertEquals(arr.size(), 4);
        Assert.assertEquals(arr.get(0).value, "22");
        Assert.assertEquals(arr.get(1).value, "|");
        Assert.assertEquals(arr.get(2).value, "x-123");
        Assert.assertEquals(arr.get(3).value, "|$");
    }
}
