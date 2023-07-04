sealed class DataState {

    class Success (val data: MutableList<PostClass>) : DataState()
    class Failure(val message: String) : DataState()
    object Loading : DataState()
    object Empty : DataState()
}