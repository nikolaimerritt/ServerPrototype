import java.io.File

fun main(args: Array<String>)
{
	val server = Server("http://musicmanager.duckdns.org/ServerPrototype/index.php", File("data.txt"))
	server.write("test")
	println(server.read())
}