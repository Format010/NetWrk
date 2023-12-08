

sealed class NetworkError(val message: String) {
    data class ServerError (val requestId: String, val error: String?) : NetworkError("Ошибка взаимодействия с сервером для запроса: id = $requestId. Сообщение об ошибке: $error")
    data class NoData (val requestId: String) : NetworkError("Для запроса: id = $requestId нет данных")
    data class NoInternet (var requestId: String) : NetworkError("Нет подключения к интернету.")
}
// реализуйте sealed класс NetworkError, который будет принимать на вход поле message типа String
// у класса NetworkError должно быть 3 наследника
// ServerError -> принимает на вход 2 параметра: requestId: String и message: String?. Должен передавать в родительский класс NetworkError сообщение: "Ошибка взаимодействия с сервером для запроса: id = $requestId. Сообщение об ошибке: $message"
// NoData -> на вход принимает только requestId: String. Передаёт сообщение в конструктор родителя: "Для запроса: id = $requestId нет данных"
// NoInternet -> на вход принимает только requestId: String. Поле должно быть доступно за пределами класса, т. е. должно быть объявлено как val
// Передаёт сообщение в конструктор родителя: "Нет подключения к интернету."

class ErrorHandler {

    fun handleError(error: NetworkError) {
        when(error) {
            is NetworkError.ServerError -> showErrorMessage(error.message)
            is NetworkError.NoData -> showEmptyContent()
            is NetworkError.NoInternet -> reloadRequest(error.requestId)
        }
        // в этот метод будут приходить ошибки, полученные при выполнении запросов
        // обработайте ошибки:
        // если это ошибка сервера (ServerError) - просто покажите сообщение с ошибкой
        // если данные не получены (NoData) - покажите пустой экран
        // при отсутствии интернета (NoInternet) - показать ошибку пользователю и заново выполнить запрос (метод reloadRequest())
    }

    private fun showErrorMessage(message: String) {
        println(message)
    }

    private fun showEmptyContent() {
        println("Показываем пустой экран")
    }

    private fun reloadRequest(requestId: String) {
        println("При появлении подключения к интернету перезапускаем запрос: id = $requestId")
    }
}

class Network {

    fun onNetworkError(code: Int?, requestId: String, error: String?): NetworkError {
        return when (code) {
            null -> NetworkError.NoInternet(requestId)
            200 -> NetworkError.NoData(requestId)
            else -> NetworkError.ServerError(requestId, error)
        } // метод будет вызываться программой всякий раз, когда будет получена ошибка
        // возвращать ошибку NoInternet, если code = null
        // возвращать ошибку NoData, если code = 200
        // возвращать ошибку ServerError во всех остальных случаях
    }
}