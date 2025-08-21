package ir.alirezaivaz.kartam.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.AddCardActivity
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.ui.screens.HomeScreen
import ir.alirezaivaz.kartam.ui.sheets.ChangelogSheet
import ir.alirezaivaz.kartam.ui.screens.SettingsScreen
import ir.alirezaivaz.kartam.ui.screens.SupportedBanksScreen
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.viewmodel.MainViewModel
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.utils.KartamDatabase
import ir.alirezaivaz.kartam.utils.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity() {
    val activityMain = this@ActivityMain
    val db by lazy { KartamDatabase.getInstance(activityMain) }
    val viewModel by lazy { MainViewModel.getInstance(db) }
    private val addEditCardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.loadCards(isRefreshing = true)
                }
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            val scope = rememberCoroutineScope()
            val toaster = rememberToasterState()
            val navController = rememberNavController()
            val context = LocalContext.current
            val loadingState by viewModel.loadingState.collectAsState()
            var currentDestination by remember { mutableStateOf(Destination.DEFAULT_DESTINATION) }
            var showChangelogSheet by remember { mutableStateOf(SettingsManager.isAppUpdated()) }
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                currentDestination = Destination.findBy(destination.route)
            }

            KartamTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = stringResource(currentDestination.label)
                                )
                            },
                            actions = {
                                AnimatedVisibility(
                                    visible = currentDestination == Destination.HOME
                                ) {
                                    IconButton(
                                        onClick = {
                                            scope.launch(Dispatchers.IO) {
                                                viewModel.loadCards(isRefreshing = loadingState == LoadingState.LOADED)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_reload),
                                            contentDescription = stringResource(R.string.action_reload)
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                        )
                    },
                    bottomBar = {
                        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                            Destination.entries.forEach {
                                NavigationBarItem(
                                    selected = currentDestination == it,
                                    onClick = {
                                        navController.navigate(route = it.route)
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                if (currentDestination == it) {
                                                    it.filledIcon
                                                } else {
                                                    it.icon
                                                }
                                            ),
                                            contentDescription = stringResource(it.label)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(it.label)
                                        )
                                    }
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = stringResource(R.string.action_add_card)
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.action_add_card)
                                )
                            },
                            onClick = {
                                val intent = Intent(context, AddCardActivity::class.java)
                                addEditCardLauncher.launch(intent)
                            }
                        )
                    }
                ) { contentPadding ->
                    KartamToaster(state = toaster)
                    if (showChangelogSheet) {
                        ChangelogSheet(
                            onDismissRequest = {
                                SettingsManager.setLastVersion()
                                showChangelogSheet = false
                            }
                        )
                    }
                    NavHost(
                        navController = navController,
                        startDestination = Destination.DEFAULT_DESTINATION.route,
                        modifier = Modifier.padding(contentPadding)
                    ) {
                        composable(Destination.HOME.route) { backStackEntry ->
                            HomeScreen(
                                toaster = toaster,
                                viewModel = viewModel,
                                onEditRequest = {
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("id", it)
                                    addEditCardLauncher.launch(intent)
                                }
                            )
                        }
                        composable(Destination.SUPPORTED_BANKS.route) {
                            SupportedBanksScreen()
                        }
                        composable(Destination.SETTINGS.route) {
                            SettingsScreen(
                                toaster = toaster,
                                onChangelogRequest = {
                                    showChangelogSheet = true
                                },
                                onThemeChangedRequest = { item ->
                                    scope.launch {
                                        SettingsManager.setTheme(item)
                                    }
                                },
                                onLanguageChangedRequest = { item ->
                                    scope.launch {
                                        SettingsManager.setLanguage(item)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Destination(
    val route: String,
    @param:StringRes
    val label: Int,
    @param:DrawableRes
    val icon: Int,
    @param:DrawableRes
    val filledIcon: Int,
) {
    HOME(
        route = "home",
        label = R.string.action_home,
        icon = R.drawable.ic_home,
        filledIcon = R.drawable.ic_home_filled,
    ),
    SUPPORTED_BANKS(
        route = "supported_banks",
        label = R.string.supported_banks,
        icon = R.drawable.ic_building_bank,
        filledIcon = R.drawable.ic_building_bank,
    ),
    SETTINGS(
        route = "settings",
        label = R.string.action_settings,
        icon = R.drawable.ic_settings,
        filledIcon = R.drawable.ic_settings_filled
    );

    companion object {
        val DEFAULT_DESTINATION = HOME
        fun findBy(route: String?): Destination {
            return entries.find { it.route == route } ?: DEFAULT_DESTINATION
        }
    }
}
