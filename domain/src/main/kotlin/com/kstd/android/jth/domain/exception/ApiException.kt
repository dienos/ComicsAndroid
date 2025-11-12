package com.kstd.android.jth.domain.exception

import java.io.IOException

open class ApiException(message: String) : IOException(message)

class IncorrectQueryRequestException(message: String) : ApiException(message)
class InvalidDisplayValueException(message: String) : ApiException(message)
class InvalidStartValueException(message: String) : ApiException(message)
class InvalidSortValueException(message: String) : ApiException(message)
class MalformedEncodingException(message: String) : ApiException(message)

class InvalidSearchApiException(message: String) : ApiException(message)

class SystemErrorException(message: String) : ApiException(message)

class UnknownApiException(message: String) : ApiException(message)
