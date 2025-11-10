package com.kstd.android.jth.ui.feature.viewer

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.extension.loadAsWebtoon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebtoonViewerActivity : ComponentActivity() {

    private val viewModel: WebtoonViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedIndex = intent.getIntExtra("selectedIndex", 0)

        val comics = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("comics", ComicsItem::class.java)
        } else {
            intent.getParcelableArrayListExtra("comics")
        } ?: emptyList<ComicsItem>()

        viewModel.setWebtoonData(comics, selectedIndex)

        setContent {
            MaterialTheme {
                WebtoonViewerScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebtoonViewerScreen(viewModel: WebtoonViewerViewModel) {
    val webtoonFlow by viewModel.webtoonFlow.collectAsState()
    val initialIndex by viewModel.initialIndex.collectAsState()
    val title by viewModel.title.collectAsState()

    val lazyPagingItems: LazyPagingItems<ComicsItem>? = webtoonFlow?.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = lazyPagingItems?.itemCount) {
        if (lazyPagingItems != null && initialIndex >= 0 && lazyPagingItems.itemCount > initialIndex) {
            lazyListState.scrollToItem(index = initialIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { padding ->
        if (lazyPagingItems != null) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(lazyPagingItems.itemCount) { index ->
                    lazyPagingItems[index]?.let { item ->
                        val imageWidth = item.sizeWidth?.toIntOrNull()
                        val imageHeight = item.sizeHeight?.toIntOrNull()

                        var itemModifier = Modifier.fillMaxWidth()
                        if (imageWidth != null && imageHeight != null && imageWidth > 0 && imageHeight > 0) {
                            itemModifier =
                                itemModifier.aspectRatio(imageWidth.toFloat() / imageHeight.toFloat())
                        }

                        AndroidView(
                            factory = { context -> ImageView(context) },
                            update = { imageView ->
                                imageView.loadAsWebtoon(url = item.link ?: "")
                            },
                            modifier = itemModifier
                        )
                    }
                }
            }
        }
    }
}
