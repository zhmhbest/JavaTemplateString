package org.zhmh;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static org.zhmh.TypedString.getTemplate;

public class MainTest {
    @Test
    public void testTypedString() {
        List<TypedString> arr;
        arr = getTemplate("456${abc}123\\${ppp}qwe");
        Assert.assertEquals(arr.size(), 3);
        Assert.assertEquals(arr.get(1).value, "abc");
        Assert.assertEquals(arr.get(1).type, TypedString.TypedStringTypes.TYPED_EL);
        Assert.assertEquals(arr.get(2).value, "123${ppp}qwe");
        Assert.assertEquals(arr.get(2).type, TypedString.TypedStringTypes.TYPED_STRING);

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

    @Test
    public void testTemplateString() throws IOException {
        InputStream is = MainTest.class.getResourceAsStream("/TemplateString.jsp");
        assert is != null;
        String text = EasyIO.readTextAsUTF8(is);
        TemplateString ts = TemplateString.make(text);

        String str = ts.toString();
        String[] lines = str.split("\\n");
        Assert.assertNotEquals(ts, null);
        Assert.assertEquals(lines[3], "<form action=\"${Undefined variable 'base'}/user/login\">");
        Assert.assertEquals(lines[7], "    <pre>\\\t\\</pre>");
        Assert.assertEquals(lines[8], "    <pre>${abc}</pre>");

        ts.setVariable("base", "localhost");
        ts.setVariable("abc", "123");
        str = ts.toString();
        lines = str.split("\\n");
        Assert.assertEquals(lines[3], "<form action=\"localhost/user/login\">");
        Assert.assertEquals(lines[8], "    <pre>${abc}</pre>");
    }

    @Test
    public void MultiTemplateString() {
        MultiTemplateString mts = new MultiTemplateString("^^^${a}|${b}|${c}$$$");
        mts.addSubSimpleTemplate("a", "[${b}${aa}]");
        Assert.assertEquals(mts.toString(),
                "^^^[${Undefined variable 'b'}${Undefined variable 'aa'}]|${Undefined variable 'b'}|${Undefined variable 'c'}$$$");
        mts.setVariable("aa", "AA");
        mts.addSubMultiTemplate("b", "(${c})");
        mts.addSubMultiTemplate("c", "[${d}]");
        mts.addSubMultiTemplate("d", "{${d1}+${d2}+${d3}}");
        mts.setVariable("d1", "11");
        mts.setVariable("d2", "22");
        mts.setVariable("d3", "33");
        Assert.assertEquals(mts.toString(), "^^^[${Undefined variable 'b'}AA]|([{11+22+33}])|[{11+22+33}]$$$");
    }
}
