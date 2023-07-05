import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int) {

    private lateinit var serverSocket: ServerSocket
    private var clientSocket: Socket? = null
    private var isRunning: Boolean = false

    fun start(messageHandler: (String) -> Unit) {
        serverSocket = ServerSocket(port)
        isRunning = true

        while (isRunning) {
            clientSocket = serverSocket.accept()
            println("Client connected: ${clientSocket?.inetAddress?.hostAddress}")

            val reader = clientSocket?.getInputStream()?.bufferedReader()
            reader?.use {
                var message: String?
                while (isRunning) {
                    message = reader.readLine()
                    if (message != null) {
                        messageHandler.invoke(message)
                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        val writer = clientSocket?.getOutputStream()?.bufferedWriter()
        writer?.use {
            it.write(message)
            it.newLine()
            it.flush()
        }
    }

    fun stop() {
        isRunning = false
        clientSocket?.close()
        serverSocket.close()
    }
}
