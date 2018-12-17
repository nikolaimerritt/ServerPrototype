import java.io.File
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset

class Server(private val urlString: String, private val file: File)
{
	private val cloudConn = CloudConn(urlString)
	private val offlineBackup = FileConn(file)
	private val syncFlagFile = File(file.toPath().toAbsolutePath().parent.toFile(), "DATABASE_NEEDS_SYNCING")
	// existence of this file tells Server that the backup needs syncing

	init
	{
		Thread {
			while (true) // should run for duriation of program
			{
				if (isOnline() && syncFlagFile.exists())
				{
					syncFileWithCloud()
					syncFlagFile.delete()
				}
				Thread.sleep(1000)
			}
		}.start()
	}

	private fun isOnline(): Boolean
	{
		return try
		{
			URL(urlString).openConnection().connect()
			true
		}
		catch (e: Exception) { false }
	}

	// will parse output here
	fun read() = if (isOnline()) cloudConn.read() else offlineBackup.read()

	// will sanitise String here
	fun write(text: String)
	{
		if (isOnline())
		{
			cloudConn.write(URLEncoder.encode(text, "UTF-8"))
			offlineBackup.write(text) // backing up to offline storage
		}
		else
		{
			offlineBackup.write(text)
			syncFlagFile.createNewFile()
		}
	}

	private fun syncFileWithCloud()
	{
		val text = file.readText(Charset.defaultCharset())
		cloudConn.write(URLEncoder.encode(text, "UTF-8"))
	}
}