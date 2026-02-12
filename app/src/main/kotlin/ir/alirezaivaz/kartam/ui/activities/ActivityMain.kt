package ir.alirezaivaz.kartam.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.screens.ListScreen
import ir.alirezaivaz.kartam.ui.screens.ManageDataScreen
import ir.alirezaivaz.kartam.ui.screens.SettingsScreen
import ir.alirezaivaz.kartam.ui.screens.SupportedBanksScreen
import ir.alirezaivaz.kartam.ui.sheets.ChangelogSheet
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.viewmodel.MainViewModel
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.utils.SettingsManager
import kotlinx.coroutines.launch

class ActivityMain : KartamActivity() {
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
            var currentDestination by remember { mutableStateOf(Destination.DEFAULT_DESTINATION) }
            var showChangelogSheet by remember { mutableStateOf(SettingsManager.isAppUpdated()) }
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                currentDestination = Destination.findBy(destination.route)
            }

            KartamTheme {
                Scaffold(
                    modifier = Modifier.imePadding(),
                    contentWindowInsets = WindowInsets(0),
                    bottomBar = {
                        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                            Destination.entries.forEach {
                                NavigationBarItem(
                                    modifier = Modifier.handPointerIcon(),
                                    selected = currentDestination == it,
                                    onClick = {
                                        if (currentDestination != it) {
                                            navController.navigate(route = it.route)
                                        }
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
                        route = Destination.GRAPH_ROUTE,
                        navController = navController,
                        startDestination = Destination.DEFAULT_DESTINATION.route,
                        modifier = Modifier.padding(contentPadding)
                    ) {
                        composable(Destination.MY_CARDS.route) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(Destination.GRAPH_ROUTE)
                            }
                            val viewModel: MainViewModel = viewModel(parentEntry)
                            val ownedCards by viewModel.ownedCards.collectAsState()
                            ListScreen(
                                cards = ownedCards,
                                isOwned = true,
                                toaster = toaster,
                                viewModel = viewModel,
                                onEditRequest = { launcher, id ->
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("id", id)
                                    launcher.launch(intent)
                                },
                                onAddCardClick = { launcher ->
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("owned", true)
                                    launcher.launch(intent)
                                }
                            )
                        }
                        composable(Destination.OTHERS_CARDS.route) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(Destination.GRAPH_ROUTE)
                            }
                            val viewModel: MainViewModel = viewModel(parentEntry)
                            val othersCards by viewModel.othersCards.collectAsState()
                            ListScreen(
                                cards = othersCards,
                                isOwned = false,
                                toaster = toaster,
                                viewModel = viewModel,
                                onEditRequest = { launcher, id ->
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("id", id)
                                    launcher.launch(intent)
                                },
                                onAddCardClick = { launcher ->
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("owned", false)
                                    launcher.launch(intent)
                                }
                            )
                        }
                        composable(Destination.SUPPORTED_BANKS.route) {
                            SupportedBanksScreen()
                        }
                        composable(Destination.MANAGE_DATA.route) {
                            ManageDataScreen(toaster = toaster)
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
    MY_CARDS(
        route = "my_cards",
        label = R.string.label_mine,
        icon = R.drawable.ic_credit_card,
        filledIcon = R.drawable.ic_credit_card_filled,
    ),
    OTHERS_CARDS(
        route = "others_cards",
        label = R.string.label_others,
        icon = R.drawable.ic_cards,
        filledIcon = R.drawable.ic_cards_filled,
    ),
    SUPPORTED_BANKS(
        route = "supported_banks",
        label = R.string.action_banks,
        icon = R.drawable.ic_building_bank,
        filledIcon = R.drawable.ic_building_bank,
    ),
    MANAGE_DATA(
        route = "manage_data",
        label = R.string.action_data,
        icon = R.drawable.ic_archive,
        filledIcon = R.drawable.ic_archive_filled
    ),
    SETTINGS(
        route = "settings",
        label = R.string.action_settings,
        icon = R.drawable.ic_settings,
        filledIcon = R.drawable.ic_settings_filled
    );

    companion object {
        val GRAPH_ROUTE = "kartam_graph"
        val DEFAULT_DESTINATION = MY_CARDS
        fun findBy(route: String?): Destination {
            return entries.find { it.route == route } ?: DEFAULT_DESTINATION
        }
    }
}
