package ir.alirezaivaz.kartam.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dokar.sonner.ToastType
import com.dokar.sonner.listen
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.extensions.formattedMonth
import ir.alirezaivaz.kartam.extensions.formattedYear
import ir.alirezaivaz.kartam.extensions.isValidAccountNumber
import ir.alirezaivaz.kartam.extensions.isValidCardNumber
import ir.alirezaivaz.kartam.extensions.isValidCvv2
import ir.alirezaivaz.kartam.extensions.isValidMonth
import ir.alirezaivaz.kartam.extensions.isValidName
import ir.alirezaivaz.kartam.extensions.isValidShabaNumber
import ir.alirezaivaz.kartam.extensions.isValidYear
import ir.alirezaivaz.kartam.ui.dialogs.CardAddedDialog
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.viewmodel.AddCardViewModel
import ir.alirezaivaz.kartam.ui.viewmodel.AddCardViewModelFactory
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.ui.widgets.ErrorView
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.utils.AppDatabase
import ir.mehrafzoon.composedatepicker.core.component.rememberDialogDatePicker
import ir.mehrafzoon.composedatepicker.sheet.DatePickerModalBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(cardId: Int) {
    val scope = rememberCoroutineScope()
    val toaster = rememberToasterState()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val focusManager = LocalFocusManager.current
    val db = AppDatabase.getInstance(context)
    val factory = remember { AddCardViewModelFactory(db, cardId) }
    val viewModel: AddCardViewModel = viewModel(factory = factory)
    val scrollState = rememberScrollState()
    val datePickerController = rememberDialogDatePicker()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardAddedDialog by remember { mutableStateOf(false) }

    val isEdit by viewModel.isEdit.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val cardNumber by viewModel.cardNumber.collectAsState()
    val ownerName by viewModel.ownerName.collectAsState()
    val ownerEnglishName by viewModel.ownerEnglishName.collectAsState()
    val bank by viewModel.bank.collectAsState()
    val shabaNumber by viewModel.shabaNumber.collectAsState()
    val accountNumber by viewModel.accountNumber.collectAsState()
    val expirationMonth by viewModel.expirationMonth.collectAsState()
    val expirationYear by viewModel.expirationYear.collectAsState()
    val cvv2 by viewModel.cvv2.collectAsState()

//    LaunchedEffect(Unit) {
//        scope.launch(Dispatchers.IO) {
//            viewModel.loadCardDetails(
//                cardId = cardId,
//                onCardLoaded = {
//                    toaster.show(
//                        message = "Card loaded!",
//                        type = ToastType.Success
//                    )
//                },
//                onCardLoadFailed = {
//                    toaster.show(
//                        message = "Card not found to edit!",
//                        type = ToastType.Error
//                    )
//                }
//            )
//        }
//    }

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
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.action_back)
                            )
                        }
                    },
                    actions = {
                        AnimatedVisibility(
                            visible = loadingState != LoadingState.LOADING,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        if (isEdit) {
                                            val result = viewModel.updateCard()
                                            if (result.isSuccess) {
                                                toaster.show(
                                                    message = context.getString(R.string.message_card_updated),
                                                    type = ToastType.Success
                                                )
                                            } else if (result.errorCode != null) {
                                                toaster.show(
                                                    message = context.getString(result.errorCode.message),
                                                    type = ToastType.Error
                                                )
                                            }
                                        } else {
                                            val result = viewModel.addCard()
                                            if (result.isSuccess) {
                                                showCardAddedDialog = true
                                            } else if (result.errorCode != null) {
                                                toaster.show(
                                                    message = context.getString(result.errorCode.message),
                                                    type = ToastType.Error
                                                )
                                            }
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
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
            if (bottomSheetState.isVisible) {
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
                    titleBottomSheet = stringResource(R.string.action_choose_exp)
                )
            }
            AnimatedContent(
                targetState = loadingState,
                modifier = Modifier.padding(innerPadding)
            ) { state ->
                when (state) {
                    LoadingState.LOADING -> {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    LoadingState.EMPTY -> {
                        ErrorView(
                            icon = painterResource(R.drawable.vector_credit_card_payment),
                            title = stringResource(R.string.message_went_wrong),
                            description = stringResource(R.string.message_went_wrong_description)
                        )
                    }

                    LoadingState.LOADED -> {
                        Column(
                            modifier = Modifier
//                                .imePadding()
                                .padding(
                                    horizontal = dimensionResource(R.dimen.padding_horizontal),
//                                    vertical = dimensionResource(R.dimen.padding_vertical),
                                )
                                .verticalScroll(scrollState)
                        ) {
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
                            CardItem(
                                card = CardInfo(
                                    name = ownerName.text,
                                    englishName = ownerEnglishName.text,
                                    number = cardNumber.text,
                                    shabaNumber = shabaNumber.text,
                                    accountNumber = accountNumber.text,
                                    expirationMonth = expirationMonth.text.toIntOrNull(),
                                    expirationYear = expirationYear.text.toIntOrNull(),
                                    cvv2 = cvv2.text.toIntOrNull(),
                                    bank = bank
                                ),
                                isCvv2VisibleByDefault = true
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                            TextField(
                                value = cardNumber,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = !cardNumber.text.isValidCardNumber(),
                                label = {
                                    Text(
                                        text = stringResource(R.string.label_card_number),
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                onValueChange = {
                                    if (it.text.length <= 16) {
                                        viewModel.updateCardNumber(it)
                                    }
                                }
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                            TextField(
                                value = ownerName,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = !ownerName.text.isValidName(),
                                label = {
                                    Text(
                                        text = stringResource(R.string.label_card_owner),
                                    )
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
                            TextField(
                                value = ownerEnglishName,
                                modifier = Modifier.fillMaxWidth(),
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
                            TextField(
                                value = shabaNumber,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = shabaNumber.text.isNotEmpty() && !shabaNumber.text.isValidShabaNumber(),
                                label = {
                                    Text(
                                        text = stringResource(R.string.label_shaba_number_placeholder),
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                onValueChange = {
                                    val shaba = if (it.text.startsWith("IR")) {
                                        it.copy(it.text.drop(2))
                                    } else {
                                        it
                                    }
                                    if (shaba.text.length <= 24) {
                                        viewModel.updateShabaNumber(shaba)
                                    }
                                }
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                            TextField(
                                value = accountNumber,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = accountNumber.text.isNotEmpty() && !accountNumber.text.isValidAccountNumber(),
                                label = {
                                    Text(
                                        text = stringResource(R.string.label_account_number_placeholder),
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                onValueChange = {
                                    if (it.text.length <= 24) {
                                        viewModel.updateAccountNumber(it)
                                    }
                                }
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                            TextField(
                                value = cvv2,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = cvv2.text.isNotEmpty() && !cvv2.text.isValidCvv2(),
                                label = {
                                    Text(
                                        text = stringResource(R.string.label_cvv2_placeholder),
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                onValueChange = {
                                    if (it.text.length <= 4) {
                                        viewModel.updateCvv2(it)
                                    }
                                }
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                            ) {
                                TextField(
                                    value = expirationMonth,
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    isError = !expirationMonth.text.isValidMonth(),
                                    label = {
                                        Text(
                                            text = stringResource(R.string.label_exp_month)
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    onValueChange = {
                                        if (it.text.length <= 2) {
                                            viewModel.updateExpirationMonth(it)
                                        }
                                    }
                                )
                                TextField(
                                    value = expirationYear,
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    isError = !expirationYear.text.isValidYear(),
                                    label = {
                                        Text(
                                            text = stringResource(R.string.label_exp_year)
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    onValueChange = {
                                        if (it.text.length <= 2) {
                                            viewModel.updateExpirationYear(it)
                                        }
                                    }
                                )
                                FloatingActionButton(
                                    content = {
                                        Icon(
                                            Icons.Default.DateRange,
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
                            Spacer(Modifier.height(80.dp))
                        }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddCardScreenPreview() {
    AddCardScreen(-1)
}
