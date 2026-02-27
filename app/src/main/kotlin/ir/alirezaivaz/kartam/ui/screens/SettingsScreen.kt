package ir.alirezaivaz.kartam.ui.screens

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.App
import ir.alirezaivaz.kartam.BuildConfig
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.Theme
import ir.alirezaivaz.kartam.dto.isDynamicColorsSupported
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.activities.ActivityLockSetup
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.KartamTopBar
import ir.alirezaivaz.kartam.ui.widgets.SegmentedButton
import ir.alirezaivaz.kartam.ui.widgets.SwitchItem
import ir.alirezaivaz.kartam.ui.widgets.TooltipHelpRow
import ir.alirezaivaz.kartam.utils.BiometricHelper
import ir.alirezaivaz.kartam.utils.SettingsManager

@Composable
fun SettingsScreen(
    toaster: ToasterState,
    onChangelogRequest: () -> Unit,
    onThemeChangedRequest: (theme: Theme) -> Unit,
    onLanguageChangedRequest: (language: Language) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val uriHandler = LocalUriHandler.current
    val theme by SettingsManager.theme.collectAsState()
    val language by SettingsManager.language.collectAsState()
    val authType by SettingsManager.authType.collectAsState()
    val themes = Theme.entries.filter { it.isAvailable }.map { it }
    val languages = Language.entries.map { it }
    val authTypes = AuthType.entries.map { it }
    var selectedThemeIndex by remember { mutableIntStateOf(themes.indexOf(theme)) }
    var selectedLanguageIndex by remember { mutableIntStateOf(languages.indexOf(language)) }
    var selectedAuthTypeIndex by remember { mutableIntStateOf(authTypes.indexOf(authType)) }
    val isDynamicColors by SettingsManager.isDynamicColors.collectAsState()
    val isShowShabaNumberInCard by SettingsManager.isShowShabaNumberInCard.collectAsState()
    val isShowFullExpirationDate by SettingsManager.isShowFullExpirationDate.collectAsState()
    val isShowReverseExpirationDate by SettingsManager.isShowReverseExpirationDate.collectAsState()
    val isSecretCvv2List by SettingsManager.isSecretCvv2InList.collectAsState()
    val isSecretCvv2Details by SettingsManager.isSecretCvv2InDetails.collectAsState()
    val isLockOnStart by SettingsManager.isLockOnStart.collectAsState()
    val isUnlockWithBiometric by SettingsManager.isUnlockWithBiometric.collectAsState()
    val isAuthOwnedCardDetails by SettingsManager.isAuthOwnedCardDetails.collectAsState()
    val isAuthSecretDetails by SettingsManager.isAuthSecretData.collectAsState()
    val isAuthBeforeEdit by SettingsManager.isAuthBeforeEdit.collectAsState()
    val isAuthBeforeDelete by SettingsManager.isAuthBeforeDelete.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            KartamTopBar(
                title = stringResource(R.string.action_settings)
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
            Text(
                text = stringResource(R.string.settings_theme),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
            ) {
                themes.forEachIndexed { index, item ->
                    SegmentedButton(
                        index = index,
                        count = themes.size,
                        label = item.title,
                        icon = item.icon,
                        iconSelected = item.iconFilled,
                        isSelected = index == selectedThemeIndex,
                        onClick = {
                            selectedThemeIndex = index
                            onThemeChangedRequest(item)
                        }
                    )
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            Text(
                text = stringResource(R.string.settings_language),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
            ) {
                languages.forEachIndexed { index, item ->
                    SegmentedButton(
                        index = index,
                        count = languages.size,
                        label = item.title,
                        isSelected = index == selectedLanguageIndex,
                        onClick = {
                            selectedLanguageIndex = index
                            onLanguageChangedRequest(item)
                        }
                    )
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            if (isDynamicColorsSupported) {
                SwitchItem(
                    title = stringResource(R.string.settings_dynamic_colors),
                    isChecked = isDynamicColors,
                    onCheckedChanged = {
                        SettingsManager.setDynamicColors(it)
                    }
                )
            }
            SwitchItem(
                title = stringResource(R.string.settings_show_shaba_number_in_card),
                isChecked = isShowShabaNumberInCard,
                onCheckedChanged = {
                    SettingsManager.setShowShabaNumberInCard(it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.settings_show_full_exp_date),
                isChecked = isShowFullExpirationDate,
                onCheckedChanged = {
                    SettingsManager.setShowFullExpirationDate(it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.settings_show_reverse_exp_date),
                isChecked = isShowReverseExpirationDate,
                onCheckedChanged = {
                    SettingsManager.setShowReverseExpirationDate(it)
                }
            )
            TooltipHelpRow(
                title = stringResource(R.string.settings_hide_cvv2),
                helpText = stringResource(R.string.settings_hide_cvv2_description)
            )
            SwitchItem(
                title = stringResource(R.string.settings_hide_cvv2_list),
                isChecked = isSecretCvv2List,
                onCheckedChanged = {
                    SettingsManager.setSecretCvv2List(it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.settings_hide_cvv2_details),
                isChecked = isSecretCvv2Details,
                onCheckedChanged = {
                    SettingsManager.setSecretCvv2Details(it)
                }
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            SwitchItem(
                title = stringResource(R.string.settings_lock_on_start),
                isChecked = isLockOnStart,
                onCheckedChanged = {
                    if (isLockOnStart) {
                        SettingsManager.setLockOnStart(false)
                    } else {
                        activity?.startActivity(
                            Intent(
                                activity,
                                ActivityLockSetup::class.java
                            )
                        )
                    }
                }
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            SwitchItem(
                title = stringResource(R.string.settings_unlock_with_biometric),
                isChecked = isUnlockWithBiometric,
                isEnabled = isLockOnStart && authType in AuthType.biometricsOnly(),
                onCheckedChanged = { value ->
                    BiometricHelper(
                        activity = activity as FragmentActivity,
                        authType = authType,
                        onResult = {
                            if (it) {
                                SettingsManager.setUnlockWithBiometric(value)
                            } else {
                                toaster.show(
                                    message = resources.getString(R.string.error_authentication_failed),
                                    type = ToastType.Error
                                )
                            }
                        }
                    ).authenticate()
                }
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            TooltipHelpRow(
                title = stringResource(R.string.settings_auth_type),
                helpText = stringResource(R.string.settings_auth_type_description)
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            LazyRow {
                item {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .handPointerIcon()
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    ) {
                        authTypes.forEachIndexed { index, item ->
                            SegmentedButton(
                                selected = index == selectedAuthTypeIndex,
                                icon = {
                                    Icon(
                                        painter = painterResource(item.icon),
                                        contentDescription = stringResource(item.title)
                                    )
                                },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = authTypes.size
                                ),
                                onClick = {
                                    if (authType != item) {
                                        BiometricHelper(
                                            activity = activity as FragmentActivity,
                                            authType = item.takeUnless { it == AuthType.None } ?: authType,
                                            onResult = {
                                                if (it) {
                                                    selectedAuthTypeIndex = index
                                                    SettingsManager.setAuthType(item)
                                                } else {
                                                    toaster.show(
                                                        message = resources.getString(R.string.error_authentication_failed),
                                                        type = ToastType.Error
                                                    )
                                                }
                                            }
                                        ).authenticate()
                                    }
                                },
                                label = {
                                    Text(
                                        text = stringResource(item.title)
                                    )
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            SwitchItem(
                title = stringResource(R.string.settings_auth_owned_card_details),
                isChecked = isAuthOwnedCardDetails,
                isEnabled = authType != AuthType.None,
                onCheckedChanged = {
                    SettingsManager.setAuthOwnedCardDetails(it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.settings_auth_secret_data),
                isChecked = isAuthSecretDetails,
                isEnabled = authType != AuthType.None,
                onCheckedChanged = {
                    SettingsManager.setAuthSecretData(it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.settings_auth_before_edit),
                isChecked = isAuthBeforeEdit,
                isEnabled = authType != AuthType.None,
                onCheckedChanged = {
                    SettingsManager.setAuthBeforeEdit(it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.settings_auth_before_delete),
                isChecked = isAuthBeforeDelete,
                isEnabled = authType != AuthType.None,
                onCheckedChanged = {
                    SettingsManager.setAuthBeforeDelete(it)
                }
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
            HorizontalDivider(Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal)))
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
            ) {
                FilledTonalButton(
                    modifier = Modifier
                        .handPointerIcon()
                        .weight(1f),
                    onClick = {
                        try {
                            @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
                            if (BuildConfig.FLAVOR == "telegram") {
                                uriHandler.openUri(BuildConfig.RATE_URL)
                            } else {
                                val intentAction = if (BuildConfig.FLAVOR == "cafebazaar") {
                                    Intent.ACTION_EDIT
                                } else {
                                    Intent.ACTION_VIEW
                                }
                                context.startActivity(
                                    Intent(
                                        intentAction,
                                        BuildConfig.RATE_URL.toUri()
                                    )
                                )
                            }
                        } catch (_: Exception) {
                            toaster.show(
                                message = resources.getString(R.string.error_request_run_failed),
                                type = ToastType.Error
                            )
                        }
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_message),
                            contentDescription = stringResource(R.string.action_rate)
                        )
                        Text(
                            text = stringResource(R.string.action_rate)
                        )
                    }
                }
                FilledTonalButton(
                    modifier = Modifier
                        .handPointerIcon()
                        .weight(1f),
                    onClick = {
                        try {
                            @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
                            if (BuildConfig.FLAVOR == "telegram") {
                                uriHandler.openUri(BuildConfig.APPS_URL)
                            } else {
                                val intentAction = Intent.ACTION_VIEW
                                context.startActivity(
                                    Intent(
                                        intentAction,
                                        BuildConfig.APPS_URL.toUri()
                                    )
                                )
                            }
                        } catch (_: Exception) {
                            toaster.show(
                                message = resources.getString(R.string.error_request_run_failed),
                                type = ToastType.Error
                            )
                        }
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_apps),
                            contentDescription = stringResource(R.string.action_more_apps)
                        )
                        Text(
                            text = stringResource(R.string.action_more_apps)
                        )
                    }
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
            ) {
                FilledTonalButton(
                    modifier = Modifier
                        .handPointerIcon()
                        .weight(1f),
                    onClick = {
                        uriHandler.openUri(App.TELEGRAM_CHANNEL_URL)
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_telegram),
                            contentDescription = stringResource(R.string.action_telegram_channel)
                        )
                        Text(
                            text = stringResource(R.string.action_telegram_channel)
                        )
                    }
                }
                FilledTonalButton(
                    modifier = Modifier
                        .handPointerIcon()
                        .weight(1f),
                    onClick = onChangelogRequest,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_list),
                            contentDescription = stringResource(R.string.changelog)
                        )
                        Text(
                            text = stringResource(R.string.changelog)
                        )
                    }
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
            Text(
                text = stringResource(R.string.settings_app_version).format(
                    stringResource(R.string.app_name),
                    BuildConfig.VERSION_NAME
                ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
            )
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Preview
@Composable
fun SettingsSheetPreview() {
    KartamTheme {
        SettingsScreen(
            toaster = rememberToasterState(),
            onChangelogRequest = {},
            onThemeChangedRequest = {},
            onLanguageChangedRequest = {}
        )
    }
}
