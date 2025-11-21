package com.comics.android.jth.data.mapper

import com.comics.android.jth.data.datasource.remote.dto.ComicsItemDto
import com.comics.android.jth.data.datasource.remote.dto.ComicsResponseDto
import com.comics.android.jth.domain.model.remote.ComicsItem
import com.comics.android.jth.domain.model.remote.ComicsResponse

fun ComicsResponseDto.toComicsResponse(): ComicsResponse =
    ComicsResponse(
        lastBuildDate = lastBuildDate,
        total = total,
        start = start,
        display = display,
        items = items.map { it.toComicsItem() }
    )

fun ComicsItemDto.toComicsItem(): ComicsItem =
    ComicsItem(
        title = title,
        link = link,
        thumbnail = thumbnail,
        sizeHeight = sizeHeight,
        sizeWidth = sizeWidth,
    )