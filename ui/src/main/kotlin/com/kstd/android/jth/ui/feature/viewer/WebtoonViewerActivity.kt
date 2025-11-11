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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.extension.loadAsWebtoon
import com.kstd.android.jth.ui.theme.ComicsAppTheme
import com.kstd.android.jth.ui.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged


@AndroidEntryPoint
class WebtoonViewerActivity : ComponentActivity() {

    private val viewModel: WebtoonViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedIndex = intent.getIntExtra(Constants.EXTRA_SELECTED_INDEX, 0)

        val comics = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(Constants.EXTRA_COMICS_LIST, ComicsItem::class.java)
        } else {
            intent.getParcelableArrayListExtra(Constants.EXTRA_COMICS_LIST)
        } ?: emptyList<ComicsItem>()

        viewModel.setWebtoonData(comics, selectedIndex)

        setContent {
            ComicsAppTheme {
                WebtoonViewerScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebtoonViewerScreen(viewModel: WebtoonViewerViewModel) {
    val webtoonItems by viewModel.webtoonItems.collectAsState()
    val initialIndex by viewModel.initialIndex.collectAsState()
    val title by viewModel.title.collectAsState()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (index > 0) {
                    viewModel.onVisibleItemsChanged(index)
                }
            }
    }

    LaunchedEffect(key1 = initialIndex) {
        if (webtoonItems.isNotEmpty() && initialIndex >= 0) {
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
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            itemsIndexed(webtoonItems, key = { _, item -> item.link ?: "" }) { _, item ->
                val imageWidth = item.sizeWidth?.toIntOrNull()
                val imageHeight = item.sizeHeight?.toIntOrNull()

                var itemModifier = Modifier.fillMaxWidth()
                if (imageWidth != null && imageHeight != null && imageWidth > 0 && imageHeight > 0) {
                    itemModifier =
                        itemModifier.aspectRatio(imageWidth.toFloat() / imageHeight.toFloat())
                }

                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.FIT_XY
                        }
                    },
                    update = { imageView ->
                        imageView.loadAsWebtoon(url = item.link ?: "")
                    },
                    modifier = itemModifier
                )
            }
        }
    }
}
