# JavaTemplateString

解决Java语言没有多行长字符串的问题。

```Java
MultiTemplateString mts = new MultiTemplateString("${var-A}\n${template-B}\n${template-C}");
mts.setVariable("var-A", "(Variable A)");
mts.addSubSimpleTemplate("template-B", "[Template B: ${var-A}]");
mts.addSubMultiTemplate("template-C", "{Template C: ${template-B}}");
System.out.println(mts);
// (Variable A)
// [Template B: (Variable A)]
// {Template C: [Template B: (Variable A)]}
```
