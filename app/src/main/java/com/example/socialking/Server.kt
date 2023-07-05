import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int) {

    private lateinit var serverSocket: ServerSocket
    private var isRunning: Boolean = false
    private val clients: MutableList<ClientHandler> = mutableListOf()

    fun start() {
        serverSocket = ServerSocket(port)
        isRunning = true
        println("Server started on port $port")

        while (isRunning) {
            val clientSocket = serverSocket.accept()
            println("Client connected: ${clientSocket.inetAddress.hostAddress}")

            val clientHandler = ClientHandler(clientSocket)
            clients.add(clientHandler)
            clientHandler.start()
        }
    }

    fun stop() {
        isRunning = false
        serverSocket.close()
    }

    fun broadcastMessage(message: String, sender: ClientHandler) {
        for (client in clients) {
            if (client != sender) {
                client.sendMessage(message)
            }
        }
    }

    inner class ClientHandler(private val clientSocket: Socket) : Thread() {
        private lateinit var reader: BufferedReader
        private lateinit var writer: PrintWriter

        override fun run() {
            reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            writer = PrintWriter(clientSocket.getOutputStream(), true)

            var message: String?
            while (true) {
                message = reader.readLine()
                if (message != null) {
                    println("Received message from client: $message")
                    broadcastMessage(message, this)
                }
            }
        }

        fun sendMessage(message: String) {
            writer.println(message)
        }
    }
}

fun main() {
    val server = Server(3000)
    server.start()
}
