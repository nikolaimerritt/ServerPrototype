import java.io.File
import java.nio.charset.Charset

class FileConn(private val file: File) // Will add more extensive interaction with a proper database
{
	fun read() = file.readText(Charset.defaultCharset())

	fun write(text: String) = file.writeText(text)
}