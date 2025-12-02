# Core Data: как пользоваться
## Подключение
```kotlin
// settings.gradle.kts уже включает core-data
dependencies {
    implementation(project(":core-data"))
}
```
Никаких дополнительных зависимостей тянуть не нужно — Retrofit/OkHttp/serialization идут транзитивно.

## Инициализация клиента
```kotlin
val backend = TTravelsBackend.create(
    baseUrl = "https://api.myserver/v1/",        // опционально, по умолчанию http://localhost:8081/api/v1/
    tokensStore = PersistentTokensStore.create(appContext), // или InMemoryTokensStore() для временного хранения
    json = defaultJson(),                        // можно передать свой Json
    configureClient = { /* при необходимости добавьте свои интерсепторы */ }
)
```
- Все методы `suspend`, вызывайте из корутины (`viewModelScope`, `runBlocking` в тестах и т.д.).
- `backend.rawApi` — доступ к чистому Retrofit-интерфейсу, если нужна кастомная обработка.

## Как обрабатывать ответы
Все публичные методы `TTravelsBackend` возвращают `NetworkResult<T>`:
- `Success(data)` — успешный ответ.
- `HttpError(code, error: ErrorResponse?)` — сервер вернул код != 2xx, тело ошибки пытается распарсить в `ErrorResponse`.
- `NetworkError(IOException)` — проблемы сети/таймаут.
- `SerializationError` — не распарсили тело.
- `UnknownError` — все остальное.

Пример обработки:
```kotlin
when (val res = backend.login(AuthLoginRequest(phone, pass))) {
    is NetworkResult.Success -> {/* токены уже сохранены */ }
    is NetworkResult.HttpError -> showError(res.error?.message ?: "Ошибка ${res.code}")
    is NetworkResult.NetworkError -> showError("Сеть недоступна")
    else -> showError("Что-то пошло не так")
}
```
Есть helper `map`: `backend.getMyTravels().map { it.travels }`.

## Токены и refresh
- `tokensStore` обязателен для авторизованных запросов. Используйте:
  - `PersistentTokensStore.create(context)` — хранение в SharedPreferences (боевые/долгоживущие токены).
  - `InMemoryTokensStore()` — временно (тесты, предпросмотр).
- Превентивный refresh: `AuthorizationInterceptor` обновит access-токен за 60 секунд до истечения (`NetworkDefaults.ACCESS_REFRESH_THRESHOLD_MS`).
- Реактивный refresh: `RefreshAuthenticator` перезапросит токен при 401 и повторит оригинальный запрос.
- `logout()` очищает хранилище (через `alsoClearTokens`).

## Даты
Сервер принимает строки. Для пользовательского ввода `dd.MM.yyyy` есть утилита:
```kotlin
normalizeDateToOffsetString("31.12.2025") // вернет ISO OffsetDateTime в UTC, иначе оставит строку как есть
```
Расположена в `core-data/src/main/java/com/example/core_data/util/DateFormatUtils.kt`.

## Доступные вызовы
Почти все методы доступны в `TTravelsBackend` и напрямую отражают `TTravelsApi`:
- **Auth/Аккаунт**: `register`, `login`, `refresh`, `changePassword`, `logout`, `getCurrentUser`, `getInvites`, `respondToInvite`.
- **Путешествия** (`TravelModels.kt`): `getMyTravels`, `createTravel`, `getTravel`, `editTravel`, `deleteTravel`, `closeTravel`, `reopenTravel`.
- **Участники** (`TravelMembersModels.kt`): `getTravelMembers`, `inviteMembers`, `leaveTravel`, `kickMember`.
- **Категории и траты** (`CategoryModels.kt`, `ExpenseModels.kt`): `getCategories`, `createCategory`, `editCategory`, `getTravelExpenses`, `createExpense`, `updateExpense`, `deleteExpense`, `addParticipantsToExpense`, `removeParticipantsFromExpense`.
- **Переводы/аналитика** (`TransferModels.kt`): `getTransfers`, `createTransfer`, `editTransfer`; отчеты `getExpenseReport` (`TravelExpenseAnalyticsDTO`).

## Минимальный рабочий пример
```kotlin
suspend fun loadData() {
    val login = backend.login(AuthLoginRequest("+79990000000", "pass"))
    if (login is NetworkResult.Success) {
        // токены сохранились, можно грузить данные
        when (val travels = backend.getMyTravels()) {
            is NetworkResult.Success -> showTravels(travels.data.travels)
            else -> showError("Не удалось загрузить путешествия")
        }
    } else {
        showError("Авторизация не удалась")
    }
}
```

## Кастомизация HTTP-клиента
Передайте `configureClient` в `TTravelsBackend.create`, чтобы добавить свои интерсепторы, трейсинг или настройки SSL:
```kotlin
TTravelsBackend.create(configureClient = {
    addInterceptor(MyMetricsInterceptor())
    callTimeout(20, TimeUnit.SECONDS)
})
```

Этого достаточно, чтобы быстро поднять клиент, авторизоваться, вызывать API и обрабатывать ошибки в фронтенд-потоке. Настройки по умолчанию покрывают refresh токенов, логирование и парсинг ошибок, так что достаточно подключить модуль и вызвать `TTravelsBackend.create`.
