package ir.alirezaivaz.kartam.ui.screens

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dokar.sonner.ToastType
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.getChoosableBanks
import ir.alirezaivaz.kartam.dto.toSensitive
import ir.alirezaivaz.kartam.extensions.extractCardNumber
import ir.alirezaivaz.kartam.extensions.formattedMonth
import ir.alirezaivaz.kartam.extensions.formattedYear
import ir.alirezaivaz.kartam.extensions.isValidAccountNumber
import ir.alirezaivaz.kartam.extensions.isValidCardNumber
import ir.alirezaivaz.kartam.extensions.isValidCvv2
import ir.alirezaivaz.kartam.extensions.isValidFirstCode
import ir.alirezaivaz.kartam.extensions.isValidMonth
import ir.alirezaivaz.kartam.extensions.isValidName
import ir.alirezaivaz.kartam.extensions.isValidShabaNumber
import ir.alirezaivaz.kartam.extensions.isValidYear
import ir.alirezaivaz.kartam.extensions.takeDigits
import ir.alirezaivaz.kartam.ui.dialogs.CardAddedDialog
import ir.alirezaivaz.kartam.ui.sheets.SelectOptionsSheet
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.viewmodel.AddCardViewModel
import ir.alirezaivaz.kartam.ui.viewmodel.AddCardViewModelFactory
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.ui.widgets.FilterChip
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.utils.BackupManager
import ir.alirezaivaz.kartam.utils.KartamDatabase
import ir.huri.jcal.JalaliCalendar
import ir.mehrafzoon.composedatepicker.core.component.rememberDialogDatePicker
import ir.mehrafzoon.composedatepicker.sheet.DatePickerModalBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    cardId: Int,
    isOwned: Boolean
) {
    val scope = rememberCoroutineScope()
    val toaster = rememberToasterState()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val focusManager = LocalFocusManager.current
    val jalaliCalendar = JalaliCalendar()
    val db = KartamDatabase.getInstance(context)
    val backupManager by lazy { BackupManager.getInstance(context.noBackupFilesDir, db) }
    val factory = remember { AddCardViewModelFactory(db, cardId, isOwned, backupManager) }
    val viewModel: AddCardViewModel = viewModel(factory = factory)
    val scrollState = rememberScrollState()
    val datePickerController = rememberDialogDatePicker()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardAddedDialog by remember { mutableStateOf(false) }
    var showChooseAccountTypeSheet by remember { mutableStateOf(false) }

    val isEdit by viewModel.isEdit.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAutoDetectBank by viewModel.isAutoDetectBank.collectAsState()
    val result by viewModel.result.collectAsState()
    val cardNumber by viewModel.cardNumber.collectAsState()
    val ownerName by viewModel.ownerName.collectAsState()
    val ownerEnglishName by viewModel.ownerEnglishName.collectAsState()
    val bank by viewModel.bank.collectAsState()
    val autoDetectedBank by viewModel.autoDetectedBank.collectAsState()
    val accountType by viewModel.accountType.collectAsState()
    val shabaNumber by viewModel.shabaNumber.collectAsState()
    val accountNumber by viewModel.accountNumber.collectAsState()
    val branchCode by viewModel.branchCode.collectAsState()
    val branchName by viewModel.branchName.collectAsState()
    val expirationMonth by viewModel.expirationMonth.collectAsState()
    val expirationYear by viewModel.expirationYear.collectAsState()
    val cvv2 by viewModel.cvv2.collectAsState()
    val firstCode by viewModel.firstCode.collectAsState()
    val comment by viewModel.comment.collectAsState()
    val isOthersCard by viewModel.isOthersCard.collectAsState()
    val initialMonth = jalaliCalendar.month
    val initialYear = jalaliCalendar.year

    LaunchedEffect(result) {
        result?.let {
            if (it.isSuccess) {
                activity?.setResult(Activity.RESULT_OK)
                if (!isEdit) {
                    showCardAddedDialog = true
                } else if (it.message != null) {
                    toaster.show(
                        message = context.getString(it.message),
                        type = ToastType.Success
                    )
                }
            } else if (it.message != null) {
                toaster.show(
                    message = context.getString(it.message),
                    type = ToastType.Error
                )
            }
            viewModel.updateResult(null)
        }
    }

    KartamTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = if (isEdit) {
                                stringResource(R.string.action_edit_card)
                            } else {
                                stringResource(R.string.action_add_card)
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                activity?.finish()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = stringResource(R.string.action_back)
                            )
                        }
                    },
                    actions = {
                        AnimatedVisibility(
                            visible = !isLoading,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        focusManager.clearFocus(true)
                                        if (isEdit) {
                                            viewModel.updateCard()
                                        } else {
                                            viewModel.addCard()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_check),
                                    contentDescription = stringResource(
                                        if (isEdit) {
                                            R.string.action_edit_card
                                        } else {
                                            R.string.action_add_card
                                        }
                                    )
                                )
                            }
                        }
                    }
                )
            },
        ) { innerPadding ->
            KartamToaster(toaster)
            if (showCardAddedDialog) {
                CardAddedDialog(
                    onConfirm = {
                        activity?.finish()
                    },
                    onDismissRequest = {
                        showCardAddedDialog = false
                    }
                )
            }
            if (showChooseAccountTypeSheet) {
                SelectOptionsSheet(
                    title = stringResource(R.string.account_type),
                    items = bank.supportedAccountTypes.map { stringResource(it.title) },
                    values = bank.supportedAccountTypes.map { it.name },
                    selectedItem = accountType?.name,
                    onItemSelectedListener = { value ->
                        viewModel.updateAccountType(value)
                    },
                    onDismissRequest = {
                        showChooseAccountTypeSheet = false
                    }
                )
            }
            if (bottomSheetState.isVisible) {
                val month = expirationMonth.text.toIntOrNull() ?: initialMonth
                val expYear = expirationYear.text.toIntOrNull() ?: initialYear
                val year = expYear.takeIf { it.toString().length == 4 } ?: expYear.let {
                    val prefix = initialYear.toString().dropLast(2).toInt()
                    it.plus(prefix * 100)
                }
                val initialDate = Triple(year, month, 1)
                DatePickerModalBottomSheet(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onSubmitClick = {
                        val month = datePickerController.getPersianMonth().formattedMonth()
                        val year = datePickerController.getPersianYear().formattedYear()
                        viewModel.updateExpirationMonth(TextFieldValue(month))
                        viewModel.updateExpirationYear(TextFieldValue(year))
                    },
                    sheetState = bottomSheetState,
                    controller = datePickerController,
                    onDismissRequest = {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    textButtonStyle = MaterialTheme.typography.bodyMedium,
                    datePickerWithoutDay = true,
                    useInitialDate = true,
                    initialDate = initialDate,
                    titleBottomSheet = stringResource(R.string.action_choose_exp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                AnimatedVisibility(
                    visible = isLoading
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
                    CardItem(
                        card = CardInfo(
                            name = ownerName.text,
                            englishName = ownerEnglishName.text,
                            number = cardNumber.text,
                            shabaNumber = shabaNumber.text,
                            accountNumber = accountNumber.text,
                            branchCode = branchCode.text.toIntOrNull(),
                            branchName = branchName.text,
                            expirationMonth = expirationMonth.text.toIntOrNull(),
                            expirationYear = expirationYear.text.toIntOrNull(),
                            cvv2 = cvv2.text.toSensitive(),
                            bank = bank
                        ),
                        isCvv2VisibleByDefault = true,
                        isAuthenticationRequired = false,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                    ) {
                        Spacer(Modifier.width(dimensionResource(R.dimen.padding_spacing)))
                        FilterChip(
                            icon = R.drawable.ic_check,
                            label = if (autoDetectedBank == Bank.Unknown) {
                                stringResource(R.string.label_auto_detect)
                            } else {
                                stringResource(R.string.label_auto_detected, stringResource(autoDetectedBank.title))
                            },
                            isSelected = isAutoDetectBank,
                            onClick = {
                                viewModel.updateIsAutoDetectBank(true)
                            }
                        )
                        autoDetectedBank.getChoosableBanks().forEach {
                            FilterChip(
                                icon = R.drawable.ic_check,
                                label = stringResource(it.title),
                                isSelected = !isAutoDetectBank && bank == it,
                                onClick = {
                                    if (isAutoDetectBank) {
                                        viewModel.updateIsAutoDetectBank(false)
                                    }
                                    viewModel.updateBank(it)
                                }
                            )
                        }
                        Spacer(Modifier.width(dimensionResource(R.dimen.padding_spacing)))
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    OutlinedTextField(
                        value = cardNumber,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        enabled = !isLoading,
                        singleLine = true,
                        isError = !cardNumber.text.isValidCardNumber(),
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.label_card_number),
                                )
                                Text(
                                    text = stringResource(R.string.label_required),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { input ->
                            val cardNumber = input.text.extractCardNumber()
                            val number = input.copy(cardNumber.number)
                            if (number.text.length <= 16) {
                                viewModel.updateCardNumber(number)
                            }
                            if (!cardNumber.ownerName.isNullOrBlank()) {
                                viewModel.updateOwnerName(TextFieldValue(cardNumber.ownerName))
                            }
                        }
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
                    SwitchItem(
                        title = stringResource(R.string.label_others_card),
                        titleStyle = MaterialTheme.typography.bodyLarge,
                        isEnabled = !isLoading,
                        isChecked = isOthersCard,
                        modifier = Modifier
                            .heightIn(min = 56.dp)
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                            .clip(OutlinedTextFieldDefaults.shape)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(
                                    alpha = if (!isLoading) 1f else 0.12f
                                ),
                                shape = OutlinedTextFieldDefaults.shape
                            ),
                        paddingEnd = dimensionResource(R.dimen.padding_spacing),
                        onCheckedChanged = {
                            viewModel.updateIsOthersCard(it)
                        }
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    OutlinedTextField(
                        value = ownerName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        enabled = !isLoading,
                        singleLine = true,
                        isError = !ownerName.text.isValidName(),
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.label_card_owner),
                                )
                                Text(
                                    text = stringResource(R.string.label_required),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            viewModel.updateOwnerName(it)
                        }
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    OutlinedTextField(
                        value = ownerEnglishName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        enabled = !isLoading,
                        singleLine = true,
                        isError = ownerEnglishName.text.isNotEmpty() && !ownerEnglishName.text.isValidName(),
                        label = {
                            Text(
                                text = stringResource(R.string.label_card_owner_en),
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            viewModel.updateOwnerEnglishName(it)
                        }
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    OutlinedTextField(
                        value = shabaNumber,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        enabled = !isLoading,
                        singleLine = true,
                        isError = shabaNumber.text.isNotEmpty() && !shabaNumber.text.isValidShabaNumber(),
                        label = {
                            Text(
                                text = stringResource(R.string.label_shaba_number),
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { input ->
                            val shaba = input.copy(input.text.takeDigits(24))
                            if (shaba.text.length <= 24) {
                                viewModel.updateShabaNumber(shaba)
                            }
                        }
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    OutlinedTextField(
                        value = accountNumber,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        enabled = !isLoading,
                        singleLine = true,
                        isError = accountNumber.text.isNotEmpty() && !accountNumber.text.isValidAccountNumber(),
                        label = {
                            Text(
                                text = stringResource(R.string.label_account_number),
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { input ->
                            val number = input.copy(input.text.takeDigits(24))
                            if (number.text.length <= 24) {
                                viewModel.updateAccountNumber(number)
                            }
                        }
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing)),
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    ) {
                        OutlinedTextField(
                            value = cvv2,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && !isOthersCard,
                            singleLine = true,
                            isError = cvv2.text.isNotEmpty() && !cvv2.text.isValidCvv2(),
                            label = {
                                Text(
                                    text = stringResource(R.string.label_cvv2),
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { input ->
                                val number = input.copy(input.text.takeDigits(4))
                                if (number.text.length <= 4) {
                                    viewModel.updateCvv2(number)
                                }
                            }
                        )
                        OutlinedTextField(
                            value = firstCode,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && !isOthersCard,
                            singleLine = true,
                            isError = firstCode.text.isNotEmpty() && !firstCode.text.isValidFirstCode(),
                            label = {
                                Text(
                                    text = stringResource(R.string.label_first_code),
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { input ->
                                val number = input.copy(input.text.takeDigits(4))
                                if (number.text.length <= 4) {
                                    viewModel.updateFirstCode(number)
                                }
                            }
                        )
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
                    Card(
                        shape = OutlinedTextFieldDefaults.shape,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(
                                alpha = if (!isLoading) 1f else 0.12f
                            )
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                            .clickable(enabled = !isLoading) {
                                showChooseAccountTypeSheet = true
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                        ) {
                            Text(
                                text = stringResource(R.string.account_type),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = if (!isLoading) 1f else 0.38f
                                ),
                            )
                            Text(
                                text = stringResource(accountType?.title ?: R.string.account_not_selected),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = if (!isLoading) 1f else 0.38f
                                ),
                            )
                        }
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing)),
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    ) {
                        OutlinedTextField(
                            value = branchCode,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && !bank.isNeo,
                            singleLine = true,
                            isError = branchCode.text.isNotEmpty() && !branchCode.text.isDigitsOnly(),
                            label = {
                                Text(
                                    text = stringResource(R.string.label_branch_code)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { input ->
                                val number = input.copy(input.text.takeDigits())
                                viewModel.updateBranchCode(number)
                            }
                        )
                        OutlinedTextField(
                            value = branchName,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && !bank.isNeo,
                            singleLine = true,
                            isError = branchName.text.isNotEmpty() && !branchName.text.isValidName(),
                            label = {
                                Text(
                                    text = stringResource(R.string.label_branch_name)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { input ->
                                viewModel.updateBranchName(input)
                            }
                        )
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing)),
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    ) {
                        OutlinedTextField(
                            value = expirationMonth,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && !isOthersCard,
                            singleLine = true,
                            isError = expirationMonth.text.isNotEmpty() && !expirationMonth.text.isValidMonth(),
                            label = {
                                Text(
                                    text = stringResource(R.string.label_exp_month)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { input ->
                                val number = input.copy(input.text.takeDigits(2))
                                if (number.text.length <= 2) {
                                    viewModel.updateExpirationMonth(number)
                                }
                            }
                        )
                        OutlinedTextField(
                            value = expirationYear,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && !isOthersCard,
                            singleLine = true,
                            isError = expirationYear.text.isNotEmpty() && !expirationYear.text.isValidYear(),
                            label = {
                                Text(
                                    text = stringResource(R.string.label_exp_year)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { input ->
                                val number = input.copy(input.text.takeDigits(2))
                                if (number.text.length <= 2) {
                                    viewModel.updateExpirationYear(number)
                                }
                            }
                        )
                        FilledTonalIconButton(
                            enabled = !isLoading && !isOthersCard,
                            shape = FloatingActionButtonDefaults.shape,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(56.dp),
                            content = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_calendar_time),
                                    contentDescription = stringResource(R.string.action_choose_exp)
                                )
                            },
                            onClick = {
                                scope.launch {
                                    bottomSheetState.show()
                                }
                            }
                        )
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                    OutlinedTextField(
                        value = comment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        enabled = !isLoading,
                        label = {
                            Text(
                                text = stringResource(R.string.label_comment),
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        onValueChange = {
                            viewModel.updateComment(it)
                        }
                    )
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun AddCardScreenPreview() {
    AddCardScreen(-1, true)
}
