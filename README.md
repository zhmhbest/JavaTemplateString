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

// 模板覆盖
mts.setVariable("template-B", "(Variable B)");
System.out.println(mts);
// (Variable A)
// (Variable B)
// {Template C: (Variable B)}

// 覆盖解除
mts.setVariable("template-B", null);
System.out.println(mts);
// (Variable A)
// [Template B: (Variable A)]
// {Template C: [Template B: (Variable A)]}

// 修改变量
mts.setVariable("var-A", "((Super Variable AAA))");
System.out.println(mts);
// ((Super Variable AAA))
// [Template B: ((Super Variable AAA))]
// {Template C: [Template B: ((Super Variable AAA))]}
```
