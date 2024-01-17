package com.argent.acelerometrocompose.ktor


object KtorService {
    // Localhost
//    private const val BASE_URL = "http://192.168.1.65:5038/api/1.0"
    // Droplet
    private const val BASE_URL = "http://64.23.146.15:5038/api/1.0"

    const val TOOLS = "${BASE_URL}/tools"


    const val USERS = "${BASE_URL}/users"
    const val USERS_LOGIN = "${BASE_URL}/users/authenticate"
    const val USERS_CODE = "${BASE_URL}/users/id"

    const val FILES = "${BASE_URL}/datasets"
}

object HttpRoutes {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    const val POSTS = "$BASE_URL/posts"
}