import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

val titles = listOf<String>("配置列表", "服务器列表")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppUI() {
    val pagerState = rememberPagerState()
    Scaffold(
        topBar = { AppTopBar(titles[pagerState.currentPage]) },
        content = { innerPadding -> AppContent(pagerState, innerPadding) },
        bottomBar = { AppBottomBar(pagerState) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        title = {
            Text(
                text = title,
                color = Color.White,
                modifier = Modifier
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Menu, null, tint = Color.White)
            }
        },
        modifier = Modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppBottomBar(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val height = 48.dp
    val containerColor = Color.Black
    BottomAppBar(
        contentPadding = PaddingValues(0.dp),
        containerColor = containerColor,
        modifier = Modifier.height(height)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = Color.Red,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                )
            },
            containerColor = containerColor,
            modifier = Modifier.height(height)
        ) {
            titles.forEachIndexed { index, value ->
                Tab(
                    text = { Text(text = value, color = Color.White) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            // 同步 HorizontalPager 的状态
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    modifier = Modifier
                        .wrapContentHeight()
                        .background(color = containerColor)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppContent(pagerState: PagerState, innerPadding: PaddingValues) {
    HorizontalPager(
        state = pagerState, pageCount = titles.size, modifier = Modifier
    ) { pageIndex ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(text = titles[pageIndex])
        }
    }
}