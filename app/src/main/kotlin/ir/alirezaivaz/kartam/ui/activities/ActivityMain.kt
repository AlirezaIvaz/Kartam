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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.screens.ListScreen
import ir.alirezaivaz.kartam.ui.screens.SettingsScreen
import ir.alirezaivaz.kartam.ui.screens.SupportedBanksScreen
import ir.alirezaivaz.kartam.ui.sheets.ChangelogSheet
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.viewmodel.MainViewModel
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.utils.BackupManager
import ir.alirezaivaz.kartam.utils.KartamDatabase
import ir.alirezaivaz.kartam.utils.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity() {
    val activityMain = this@ActivityMain
    val db by lazy { KartamDatabase.getInstance(activityMain) }
    val backupManager by lazy { BackupManager.getInstance(activityMain.noBackupFilesDir, db) }
    val viewModel by lazy { MainViewModel.getInstance(db, backupManager) }
    private val addEditCardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.loadCards(isRefreshing = true)
                }
            }
        }

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
            val ownedCards by viewModel.ownedCards.collectAsState()
            val othersCards by viewModel.othersCards.collectAsState()
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
                        composable(Destination.MY_CARDS.route) { backStackEntry ->
                            ListScreen(
                                cards = ownedCards,
                                isOwned = true,
                                toaster = toaster,
                                viewModel = viewModel,
                                onEditRequest = {
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("id", it)
                                    addEditCardLauncher.launch(intent)
                                },
                                onAddCardClick = {
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("owned", true)
                                    addEditCardLauncher.launch(intent)
                                }
                            )
                        }
                        composable(Destination.OTHERS_CARDS.route) { backStackEntry ->
                            ListScreen(
                                cards = othersCards,
                                isOwned = false,
                                toaster = toaster,
                                viewModel = viewModel,
                                onEditRequest = {
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("id", it)
                                    addEditCardLauncher.launch(intent)
                                },
                                onAddCardClick = {
                                    val intent = Intent(context, AddCardActivity::class.java)
                                    intent.putExtra("owned", false)
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
    MY_CARDS(
        route = "my_cards",
        label = R.string.action_my_cards,
        icon = R.drawable.ic_credit_card,
        filledIcon = R.drawable.ic_credit_card_filled,
    ),
    OTHERS_CARDS(
        route = "others_cards",
        label = R.string.action_others_cards,
        icon = R.drawable.ic_cards,
        filledIcon = R.drawable.ic_cards_filled,
    ),
    SUPPORTED_BANKS(
        route = "supported_banks",
        label = R.string.action_banks,
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
        val DEFAULT_DESTINATION = MY_CARDS
        fun findBy(route: String?): Destination {
            return entries.find { it.route == route } ?: DEFAULT_DESTINATION
        }
    }
}
