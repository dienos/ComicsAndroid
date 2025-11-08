package com.kstd.android.jth.domain.exception

import java.io.IOException

open class ApiException(message: String) : IOException(message)

// 400 Bad Request
class IncorrectQueryRequestException(message: String) : ApiException(message) // SE01
class InvalidDisplayValueException(message: String) : ApiException(message)     // SE02
class InvalidStartValueException(message: String) : ApiException(message)       // SE03
class InvalidSortValueException(message: String) : ApiException(message)        // SE04
class MalformedEncodingException(message: String) : ApiException(message)       // SE06

// 404 Not Found
class InvalidSearchApiException(message: String) : ApiException(message)        // SE05

// 500 Internal Server Error
class SystemErrorException(message: String) : ApiException(message)             // SE99

// 기타 알 수 없는 에러
class UnknownApiException(message: String) : ApiException(message)