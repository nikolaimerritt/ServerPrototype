import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CloudConn(private val urlString: String) // Currently, the database simply writes to / echoes a .txt file
{
	fun read(): String
	{
		val url = URL("$urlString?read=true")
		with(url.openConnection() as HttpURLConnection)
		{
			requestMethod = "GET"
			BufferedReader(InputStreamReader(inputStream)).use {
				val response = StringBuffer()
				var inputLine = it.readLine()
				while (inputLine != null)
				{
					response.append(inputLine)
					inputLine = it.readLine()
				}
				it.close()
				return response.toString()
			}
		}
	}

	fun write(text: String)
	{
		val url = URL("$urlString?write=$text")
		with (url.openConnection() as HttpURLConnection)
		{
			requestMethod = "GET"

			BufferedReader(InputStreamReader(inputStream)).use {
				val response = StringBuffer()
				var inputLine = it.readLine()
				while (inputLine != null)
				{
					response.append(inputLine)
					inputLine = it.readLine()
				}
				it.close()
			}
		}
	}
}