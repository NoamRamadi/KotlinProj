sealed class ChatDataState {
    class Success(val data: MutableList<MessageClass>) : ChatDataState()
    class Failure(val message: String) : ChatDataState()
    object Loading : ChatDataState()
    object Empty : ChatDataState()
}