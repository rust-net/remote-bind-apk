package app.remote_bind.ui

import android.os.Process
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.remote_bind.Instance
import app.remote_bind.Server
import app.remote_bind.getConfigs
import kotlinx.coroutines.launch

private val titles = listOf("配置列表", "服务器列表")

val configs by lazy {
    mutableStateOf( getConfigs() )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppUI(
//    configs: Pair<List<Instance>, List<Server>>,
) {
    val pagerState = rememberPagerState()
    val showDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = { AppTopBar(titles[pagerState.currentPage]) },
        content = { innerPadding ->
            AppContent(
                pagerState,
                innerPadding,
                configs.value,
            )
        },
        bottomBar = { AppBottomBar(pagerState) },
        floatingActionButton = {
           IconButton(onClick = {
               showDialog.value = true
           }, modifier = Modifier.size(40.dp)) {
               Icon(Icons.Filled.AddCircle, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.fillMaxSize())
           }
        },
        floatingActionButtonPosition = FabPosition.End,
    )
    if (showDialog.value) {
        ConfigDialog(
            showDialog,
            instance = if (titles[pagerState.currentPage] == "配置列表") { Instance.default() } else { null },
            server = if (titles[pagerState.currentPage] == "服务器列表") { Server.default() } else { null },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    var expand by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Menu, null, tint = MaterialTheme.colorScheme.primary)
            }
        },
        actions = {
            IconButton(onClick = { expand = true }) {
                Icon(Icons.Filled.MoreVert, null, tint = MaterialTheme.colorScheme.primary)
            }
            DropdownMenu(expanded = expand, onDismissRequest = { expand = false }) {
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(Icons.Filled.ExitToApp, null, tint = MaterialTheme.colorScheme.background)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("退出App")
                        }
                    },
                    onClick = {
                        expand = false
                        Process.killProcess(Process.myPid())
                    },
                )
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
    val containerColor = MaterialTheme.colorScheme.background
    BottomAppBar(
        contentPadding = PaddingValues(0.dp),
        containerColor = containerColor,
        modifier = Modifier.height(height)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                )
            },
            containerColor = containerColor,
            modifier = Modifier.height(height)
        ) {
            titles.forEachIndexed { index, value ->
                Tab(
                    text = { Text(text = value, color = MaterialTheme.colorScheme.primary) },
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
fun AppContent(
    pagerState: PagerState,
    innerPadding: PaddingValues,
    configs:  Pair<List<Instance>, List<Server>>,
) {
    val (insts, servs) = configs
    HorizontalPager(
        state = pagerState, pageCount = titles.size, modifier = Modifier
    ) { pageIndex ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                if (titles[pageIndex] == "配置列表") {
                    items(count = insts.size) {
                        InstanceItem(value = insts[it])
                    }
                } else {
                    items(count = servs.size) {
                        ServerItem(value = servs[it])
                    }
                }
            }
        }
    }
}