# ExtraList

## 功能
方便用户将皮肤站加入CustomSkinLoader加载列表  
将会在CustomSkinLoader 14.4中支持  
*原名ListAddition已被弃用*

## 要求
- 为兼容下载器可更改文件名，只需保证文件为 `*.txt` 或 `*.json` 即可  
- 编码: UTF-8
- 尽量直接点击即下载，不建议要求另存为/以文本方式直接打开
- 皮肤站尽量附上教程(请自定义)  
  
## 教程示例
- 使用CustomSkinLoader 14.4及以上
- 下载文件(附上下载链接)
- 将文件放入 `.minecraft/CustomSkinLoader/ExtraList` 
- 启动Minecraft  
  
## 文件格式
同 `CustomSkinLoader.json` loadlist中的格式  
实例: `Example.json`  
```
{  
  "name":"Example",  
  "type":"CustomSkinAPI",  
  "root":"http://www.example.com/"  
}  
```
  
## 其他
CustomSkinLoader会自动检测皮肤站是否重复，无需担心这一点。  
