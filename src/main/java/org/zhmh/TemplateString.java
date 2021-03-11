package org.zhmh;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateString {
    protected List<TypedString> template;
    protected Map<String, String> variable;
    public void setVariable(String name, String value) {
        if (null == value)
            variable.remove(name);
        else
            variable.put(name, value);
    }
    public void clearVariables() {
        variable.clear();
    }
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    public void dumpString(StringBuilder builder) {
        assert null != builder;
        for (TypedString part : template) {
            switch (part.type) {
                case TYPED_STRING:
                    builder.append(part.value);
                    break;
                case TYPED_EL:
                    if (variable.containsKey(part.value)) {
                        builder.append(variable.get(part.value));
                    } else {
                        builder.append("${Undefined variable '")
                                .append(part.value)
                                .append("'}");
                    }
                    break;
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        dumpString(builder);
        return builder.toString();
    }
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    public static TemplateString make(String s) {
        if (null == s) return null;
        List<TypedString> template = TypedString.getTemplate(s);
        if (template.isEmpty()) return null;
        TemplateString t = new TemplateString();
        t.template = template;
        t.variable = new HashMap<>();
        return t;
    }
    public static TemplateString make(InputStream is) {
        String s;
        try {
            s = EasyIO.readTextAsUTF8(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return make(s);
    }
}
