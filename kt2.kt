import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

fun main() {
    val websites = listOf(
         "https://www.google.com",
        "https://www.facebook.com",
        "https://www.github.com",
        "https://www.twitter.com",
        "https://www.instagram.com",
        "https://vk.com",
        "https://www.youtube.com",
        "https://habr.com",
        "https://gitlab.com",
        "https://etu.ru",
    )

    runBlocking {
        val results = mutableListOf<Pair<String, Boolean>>()

        // Launch coroutines to check website availability
        val jobs = websites.map { url ->
            async {
                val isAvailable = checkWebsite(url)
                results.add(Pair(url, isAvailable))
            }
        }

        // Wait for all coroutines to complete
        jobs.awaitAll()

        // Print results
        results.forEach { (url, isAvailable) ->
            println("Сайт $url ${if (isAvailable) "доступен" else "недоступен"}")
        }
    }
}

suspend fun checkWebsite(url: String) = try {
        val connection = withContext(Dispatchers.IO) {
            URL(url).openConnection()
        } as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.responseCode == HttpURLConnection.HTTP_OK
    } catch (e: Exception) {
        false
    }