
#CustomSkinLoader API

## Why Shall We Use This

- Reduction of network requests
- Ensure the availability of skin cache
- Make it easy to change your skin model
- The process of skin loading won't stuck you anymore

## Application of API

You can implement skin loading base on this API in any project.

The API will be available in CustomSkinLoader 13.1

## Definition of API

### Request Standard

GET requests without any parameters.

### Response Standard

HTTP head `Content-Length` and `Last-Modified` is required.

### The Root

The root is a definition of URL which is able to respond CustomSkinLoader API.

The root is almost a domain, like `http://localhost/`, domain with a port is also permitted. In some case, you can also use a sub-directory like `http://localhost/csl/`.

**Note** that the root should end with a slash `/`.

In the remaining of document, the root is replaced with `{ROOT}` mark.

### Get User Profile

The URL of user profile request should be the root end with username with json extension: `{ROOT}/{USERNAME}.json`

The `{USERNAME}` here is not case-sensitive.

Response:

 - 200 Found. Return user profile in JSON format.
 - 404 Not Found. No such user.

### User Profile

#### Attentions

Pretty-printed JSON is recommend, but compressed JSON also works.

#### JSON Format

```
{
    "player_name" : "{ String, player name in correct case }",
    "skins" : { Map, map of model name to texture hash },
    "cape" : "{ Hash of cape texture }"
}
```

Formal JSON format is just like the sample above.

But in case that only one skin texture is uploaded, and the default model is applied, you can also format the user profile like this (*unrecommended*):

```
{
    "player_name" : "{ player name }",
    "skin" : "{ Hash of skin texture }",
    "cape" : "{ Hash of cape texture }"
}
```

If both `skins` and `skin` field is given, `skin` will be ignored.

All the fields are optional. Empty value is also allowed `"cape" :  ""`.

#### Map of skins

Map of skins should be ordered by user model preference.

The client supporting both slim & default model will load skins & models according to the order of skins map.

The client that only support default model will load the default field of skins map directly. If only `slim` field is given, the client will load slim texture with steve model, **which could cause a shitty render error**.

```
{
    "{ User model preference }" : "{ hash of skin texture fit on this model }",
    "{ Secondary model }" : "{ hash of the model }"
}
```
#### Samples of JSON

Complete JSON string:

```
{
    "username" : "test",
    "skins" : {
        "default" : "6dc40bc8af6a48861b914d36dc1437446a977b644ab7f9c4942f79173d315b30",
        "slim" : "b2c4ef891f01c5a8e2dc8a832bc3a89c32b59ee3dadc1c4de6e357f997d2dbaf"
    },
    "cape" : "aed8c3fc67aae4906b72fa74c27e15866c89752f0838f6b2a1c44bb4d59cec1e"
}
```

Case without any skin:

```
{
    "username" : "test",
    "cape":"aed8c3fc67aae4906b72fa74c27e15866c89752f0838f6b2a1c44bb4d59cec1e"
}
```

No cloaks is uploaded:

```
{
    "username" : "test",
    "skins" : {
        "default" : "6dc40bc8af6a48861b914d36dc1437446a977b644ab7f9c4942f79173d315b30",
        "slim" : "b2c4ef891f01c5a8e2dc8a832bc3a89c32b59ee3dadc1c4de6e357f997d2dbaf"
    }
}
```

Only one skin texture, and default model is applied:

```
{
    "username" : "test",
    "skin" : "6dc40bc8af6a48861b914d36dc1437446a977b644ab7f9c4942f79173d315b30"
}
```

### Get textures

Requested URL: `{ROOT}/textures/{ unique identifier to the file }`

Response:

 - 200 Found. Return the texture.
 - 404 Not Found.

The unique identifier to the file could be customized to any unique string, SHA-256 is recommend.
