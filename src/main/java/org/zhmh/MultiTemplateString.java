package org.zhmh;

import java.util.*;

public class MultiTemplateString extends TemplateString {
    protected Map<String, TemplateString> subTemplateMap;
    protected Set<String> subMultiTemplateMap;

    public MultiTemplateString(
            List<TypedString> template,
            Map<String, String> variable,
            Map<String, TemplateString> subTemplateMap,
            Set<String> subMultiTemplateMap
    ) {
        this.template = template;
        this.variable = variable;
        this.subTemplateMap = subTemplateMap;
        this.subMultiTemplateMap = subMultiTemplateMap;
    }

    public MultiTemplateString(List<TypedString> template) {
        this.template = template;
        this.variable = new Hashtable<>();
        this.subTemplateMap = new Hashtable<>();
        this.subMultiTemplateMap = new HashSet<>();
    }

    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■

    public boolean addSubTemplate(String name, TemplateString t) {
        if (null == subTemplateMap || null == variable || null == t || null == name) return false;
        // 子模版统一使用主模板的变量表
        t.variable = this.variable;
        this.subTemplateMap.put(name, t);
        return true;
    }
    public boolean addSubSimpleTemplate(String name, String s) {
        return addSubTemplate(
                name,
                TemplateString.newInstance(TypedString.getTemplate(s), null)
        );
    }

    public boolean addSubTemplate(String name, MultiTemplateString t) {
        if (null == subTemplateMap || null == variable || null == t || null == name) return false;
        // 子模版统一使用主模板的变量表
        t.variable = this.variable;
        t.subTemplateMap = this.subTemplateMap;
        t.subMultiTemplateMap = this.subMultiTemplateMap;
        this.subTemplateMap.put(name, t);
        this.subMultiTemplateMap.add(name);
        return true;
    }
    public boolean addSubMultiTemplate(String name, String s) {
        return addSubTemplate(
                name,
                new MultiTemplateString(
                        TypedString.getTemplate(s), null, null, null
                )
        );
    }

    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■

    public void dumpString(StringBuilder builder, int[] depth) {
        // System.out.println(depth[0]);
        if (depth[0] >= 50) {
            throw new StackOverflowError("The process may have fallen into a death loop.");
        }
        for (TypedString part : template) {
            switch (part.type) {
                case TYPED_STRING:
                    builder.append(part.value);
                    break;
                case TYPED_EL:
                    if (variable.containsKey(part.value)) {
                        builder.append(variable.get(part.value));
                    } else if (subTemplateMap.containsKey(part.value)) {
                        depth[0]++;
                        if (subMultiTemplateMap.contains(part.value)) {
                            // Multi
                            ((MultiTemplateString) (subTemplateMap.get(part.value)))
                                    .dumpString(builder, depth);
                        } else {
                            // depth[0]++;
                            // Simple
                            subTemplateMap.get(part.value).dumpString(builder);
                        }
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
    public void dumpString(StringBuilder builder) {
        assert null != builder;
        assert null != variable;
        assert null != subTemplateMap;
        assert null != subMultiTemplateMap;
        int[] depth = {0};
        dumpString(builder, depth);
    }
}
