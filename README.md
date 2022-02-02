
# HEB Coding Challenge
Coding project for HEB

## Tech
[Javalin](https://javalin.io/) Simple lightweight web framework  

[OkHttp](https://square.github.io/okhttp/) HTTP Client  

[Moshi](https://github.com/square/moshi) JSON library  

[Imagga](https://imagga.com/) for image recognition  

[SQLite](www.sqlite.org)

## Things I would like to still do:
* Tests
* SQL optimizations
* Switch to prepared statements
* Input sanitization
* Better error propagation and handling
* Add Logging
* Secrets
* Write again, in Kotlin


## Available routes
### Get
```
/images
/images?objects="dog,cat"
/images/{imageId}
```

### Post
```/images```

Body example
```json
{
	"url": "https://i.imgur.com/BBcy6Wc.jpeg",
	"label": "label",
	"detection": true
}
```
