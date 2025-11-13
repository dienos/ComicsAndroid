package com.kstd.android.jth.data.repository

import com.kstd.android.jth.data.datasource.local.entity.BookmarkEntity
import com.kstd.android.jth.data.datasource.local.source.ComicsLocalSource
import com.kstd.android.jth.data.datasource.remote.source.ComicsRemoteSource
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ComicsRepositoryImplTest {

    private lateinit var repository: ComicsRepositoryImpl
    private lateinit var fakeLocalSource: FakeComicsLocalSource
    private lateinit var fakeRemoteSource: FakeComicsRemoteSource

    @Before
    fun setUp() {
        fakeLocalSource = FakeComicsLocalSource()
        fakeRemoteSource = FakeComicsRemoteSource()
        repository = ComicsRepositoryImpl(fakeLocalSource, fakeRemoteSource)
    }

    @Test
    fun `addBookmark saves data to local source and getBookmark retrieves it`() = runTest {
        // Given
        val bookmarkItems = listOf(
            BookmarkItem(title = "Test Title", link = "test_link_1", thumbnail = "", sizeHeight = "", sizeWidth = "")
        )

        // When
        repository.addBookmark(bookmarkItems)
        val bookmarksFlow = repository.getBookmark()

        // Then
        val retrievedBookmarks = bookmarksFlow.first()
        assertEquals(1, retrievedBookmarks.size)
        assertEquals("Test Title", retrievedBookmarks[0].title)
        assertEquals("test_link_1", retrievedBookmarks[0].link)
    }
}

class FakeComicsLocalSource : ComicsLocalSource {
    private val bookmarks = mutableListOf<BookmarkEntity>()

    override fun getBookmarks(): Flow<List<BookmarkItem>> {
        val bookmarkItems = bookmarks.map { entity ->
            BookmarkItem(
                title = entity.title,
                link = entity.link,
                thumbnail = entity.thumbnail,
                sizeWidth = entity.sizeWidth,
                sizeHeight = entity.sizeHeight
            )
        }
        return flowOf(bookmarkItems)
    }

    override suspend fun addBookmark(bookmarks: List<BookmarkEntity>) {
        this@FakeComicsLocalSource.bookmarks.addAll(bookmarks)
    }

    override suspend fun deleteBookmark(bookmarks: List<BookmarkEntity>) {
        this@FakeComicsLocalSource.bookmarks.removeAll(bookmarks)
    }

    override suspend fun isHomeGuideShown(key: String): Boolean = true
    override suspend fun setHomeGuideShown(key: String) {}
    override suspend fun isBookMarkGuideShown(key: String): Boolean = true
    override suspend fun setBookMarkGuideShown(key: String) {}
}

class FakeComicsRemoteSource : ComicsRemoteSource {
    override suspend fun fetchComics(page: Int, size: Int?, filter: String?): ComicsResponse {
        return ComicsResponse(lastBuildDate = "", total = 0, start = 0, display = 0, items = emptyList())
    }
}