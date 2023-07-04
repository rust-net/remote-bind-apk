import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

val titles = listOf<String>("配置列表", "服务器列表")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppUI() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
        ) {
            titles.forEachIndexed { index, value ->
                Tab(text = { Text(value) }, selected = pagerState.currentPage == index, onClick = {
                    coroutineScope.launch {
                        // 同步 HorizontalPager 的状态
                        pagerState.animateScrollToPage(index)
                    }
                })
            }
        }
        HorizontalPager(state = pagerState, pageCount = titles.size) { page ->
            val text = titles[page % titles.size]
            Column(modifier = Modifier.background(color = Color.Cyan).fillMaxSize()) {
                Text(text)
            }
        }
    }
}