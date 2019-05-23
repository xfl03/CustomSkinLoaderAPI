# CustomSkinAPI
#### Revision 2
## API 意义

- 可有效减少网络请求
- 可有效保证皮肤缓存的有效性
- 更方便改变皮肤模型

- 更可使皮肤加载流程更加容易理解

## API 应用

您可以在任何项目中实现基于这一 API 的皮肤加载。  

CustomSkinLoader 已经在 13.1 版本支持CustomSkinAPI R1  
CustomSkinLoader 已经在 14.5 版本支持CustomSkinAPI R2 
  
## API 定义

### API 请求规范

不带任何参数的 GET 请求

### API 响应规范
可使用 GZIP 与 重定向  

尽可能响应 `If-Modified-Since`   
尽可能返回正确有效的 `Content-Length` 头与 `Last-Modified` 头，其他 HTTP 头均为可选。  

可响应`Cache-Control`或`Expires`头(任选其一)，CustomSkinLoader将以此来确定内容有效期。    

### 根地址

根地址定义了可以响应 CustomSkinAPI 的 URL。

根地址可以是一个域名，例如 `http://localhost/` ，也可以带上端口 例如  `http://localhost:8080/`。也可以带有子文件夹，例如 `http://localhost/csl/`。

**请注意** 根地址必须以 `/` 结尾 。

以下内容中根地址将使用 `{ROOT}` 代替。

### 玩家信息

请尽量生成容易阅读的的排版过的 JSON，压缩过的 JSON 也可以接受

#### JSON 格式
```
{
    "username": "{字符串，大小写正确的玩家名}",
    "textures": {材质字典}
}
```
以上是通常的JSON格式，也可以使用以下缩略写法，此时皮肤将会使用`default`模型：  
```
{
    "username" : "{字符串，大小写正确的玩家名}",
    "skin" : "{皮肤的资源唯一标识符}",
    "cape" : "{披风的资源唯一标识符}",
    "elytra" : "{鞘翅的资源唯一标识符}"
}
``` 

项目重复时以`textures`中的为准。    
所有的项目都是可选的，如果没有，可以直接不返回，也可以留空，也可以使用`null`   
例如 `"cape" :  ""` 或 `"cape" :  null`

#### 材质字典
```
{
    "{第一模型}" : "{资源唯一标识符}",
    "{第二模型}" : "{资源唯一标识符}"
}
```
##### 可用模型
- `(,1.7.10]` `default` `cape`  
- `[1.8,1.8.9]` `default` `slim` `cape` 
- `[1.9,)` `default` `slim` `cape` `elytra`
##### 皮肤模型
材质字典中的皮肤的顺序应与用户的偏好顺序一致。  
在支持 `slim`/`default` 双模型的客户端中，将会根据材质字典的顺序决定加载的皮肤与模型。  
在支持`default`单一模型的客户端中，将会直接寻找`default`模型对应的材质进行加载。  
此时，如果只设定了`slim`模型，那么将采用`default`模型 + `slim`皮肤，**这会导致手臂渲染错误**   

#### JSON 范例
完整的用户信息 JSON
```
{
    "username" : "test",
    "textures" : {
        "default" : "6dc40bc8af6a48861b914d36dc1437446a977b644ab7f9c4942f79173d315b30",
        "slim" : "b2c4ef891f01c5a8e2dc8a832bc3a89c32b59ee3dadc1c4de6e357f997d2dbaf",
        "cape" : "aed8c3fc67aae4906b72fa74c27e15866c89752f0838f6b2a1c44bb4d59cec1e",
        "elytra" : "b6a865cc67aae4906b72fa74c27e15866c895f270838f6b2a1c44bb4d5954ca8"
    }
}
```
缩略写法：
```
{
    "username" : "test",
    "skin" : "b2c4ef891f01c5a8e2dc8a832bc3a89c32b59ee3dadc1c4de6e357f997d2dbaf",
    "cape" : "aed8c3fc67aae4906b72fa74c27e15866c89752f0838f6b2a1c44bb4d59cec1e",
    "elytra" : "b6a865cc67aae4906b72fa74c27e15866c895f270838f6b2a1c44bb4d5954ca8"
}
```
## API 接口
### 获取玩家信息
请求 URL：`{ROOT}/{USERNAME}.json`

其中 `{USERNAME}` 大小写可随意。

响应：
 - 200 找到玩家，返回玩家信息
 - 404 未找到玩家

### 获得资源文件
请求URL： `{ROOT}/textures/{资源唯一标识符}`

响应：
 - 200，找到资源并返回资源
 - 404，资源未找到

`{资源唯一标识符}` 可以自定义为任意的唯一字符串，推荐使用SHA-256。
