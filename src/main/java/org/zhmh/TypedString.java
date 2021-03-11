package org.zhmh;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypedString {
    public enum TypedStringTypes {
        TYPED_STRING,
        TYPED_EL, // Expression Language
    }
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    public String value;
    public TypedStringTypes type;
    public TypedString() { }
    public TypedString(String value, TypedStringTypes type) {
        this.value = value;
        this.type = type;
    }
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    public static List<TypedString> getTemplate(String s) throws UnsupportedOperationException {
        final LinkedList<TypedString> listBuffer = new LinkedList<>();
        final StringBuilder stringBuffer = new StringBuilder(s.length());

        // \? 或 ${...}
        final Pattern pattern = Pattern.compile("\\\\.|\\$\\{.+?}");
        final Matcher matcher = pattern.matcher(s);
        // ================================
        matcher.reset(); {
            int lastPosition = 0;
            boolean result = matcher.find();
            while (result) {
                stringBuffer.append(s, lastPosition, matcher.start());
                String group = matcher.group();
                // --------------------------------
                if (group.startsWith("\\")) {
                    char ch = group.charAt(1);
                    switch (ch) {
                        case '\\': stringBuffer.append('\\'); break;
                        case 'n':  stringBuffer.append('\n'); break;
                        case 'r':  stringBuffer.append('\r'); break;
                        case 't':  stringBuffer.append('\t'); break;
                        default:   stringBuffer.append(ch);   break;
                    }
                } else if (group.startsWith("$")) {
                    if (stringBuffer.length() > 0) {
                        listBuffer.add(
                                new TypedString(
                                        stringBuffer.toString(),
                                        TypedStringTypes.TYPED_STRING)
                        );
                        stringBuffer.setLength(0);
                    }
                    listBuffer.add(
                            new TypedString(
                                    group.substring(2, group.length() - 1).trim(),
                                    TypedStringTypes.TYPED_EL)
                    );
                } else {
                    throw new UnsupportedOperationException(
                            String.format("Unexpected characters '%s'.", group));
                } // group
                // --------------------------------
                lastPosition = matcher.end();
                result = matcher.find();
            } // result
            // --------------------------------
            // final
            stringBuffer.append(s, lastPosition, s.length());
            if (stringBuffer.length() > 0) {
                listBuffer.add(
                        new TypedString(
                                stringBuffer.toString(),
                                TypedStringTypes.TYPED_STRING)
                );
            } // final
        } // matcher
        // ================================
        return listBuffer;
    }
}

// #define TemplateString ArrayList<TypedString>