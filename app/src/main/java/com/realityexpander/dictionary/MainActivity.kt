package com.realityexpander.dictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.realityexpander.dictionary.feature_dictionary.presentation.WordInfoItem
import com.realityexpander.dictionary.feature_dictionary.presentation.WordInfoState
import com.realityexpander.dictionary.feature_dictionary.presentation.WordInfoViewModel
import com.realityexpander.dictionary.ui.theme.DictionaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DictionaryTheme {
                val viewModel: WordInfoViewModel = hiltViewModel()
                val state = viewModel.state.value
                val snackbarHostState = remember { SnackbarHostState() }
                val focusManager = LocalFocusManager.current
                val focusRequester = remember { FocusRequester() }
                val firstTime = remember { mutableStateOf<Boolean?>(null) }

                CollectUIEvents(viewModel, snackbarHostState)

                FocusTextFieldEntryOnActivityStart(firstTime, focusRequester)

                Scaffold(
                    scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
                ) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            focusManager.clearFocus()
                                        }
                                    )
                                }
                        ) {
                            ShowSearchQuery(viewModel, focusRequester, focusManager)

                            ShowError(state)

                            ShowLogo(state)

                            ShowDefinition(state, viewModel)
                        }

                        // Show Loading indicator
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                    }
                }
            }
        }
    }

    @Composable
    private fun ShowDefinition(
        state: WordInfoState,
        viewModel: WordInfoViewModel
    ) {
        // Show Definition
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                state.wordInfoItems.size,

                ) { i ->
                val wordInfo = state.wordInfoItems[i]
                if (i > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                WordInfoItem(wordInfo = wordInfo, viewModel = viewModel)
                if (i < state.wordInfoItems.size - 1) {
                    Divider()
                }
            }
        }
    }

    @Composable
    private fun ShowLogo(state: WordInfoState) {
        // Show logo only if there are no wordInfos from the database & api
        if (!state.isLoading && !state.isError) {
            if (state.wordInfoItems.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(
                        painter = painterResource(R.drawable.dict_logo),
                        contentDescription = "Dictionary Logo",
                        modifier = Modifier.fillMaxWidth(), // important
                        contentScale = ContentScale.Crop, // important
                        alignment = Alignment.Center,
                    )
                }
            }
        }
    }

    @Composable
    private fun ShowSearchQuery(
        viewModel: WordInfoViewModel,
        focusRequester: FocusRequester,
        focusManager: FocusManager
    ) {
        TextField(
            value = viewModel.searchQuery.value,
            onValueChange = viewModel::onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = {
                Text(text = "Enter American English slang word...")
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    @Composable
    private fun ShowError(state: WordInfoState) {
        // Show error only if there are no wordInfos from the database & api
        if (state.isError &&
            state.errorMessage != null &&
            state.wordInfoItems.isEmpty()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.errorMessage,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h5
            )
        }
    }

    @Composable
    private fun FocusTextFieldEntryOnActivityStart(
        firstTime: MutableState<Boolean?>,
        focusRequester: FocusRequester
    ) {
        // Focus the TextField when the activity starts
        LaunchedEffect(firstTime.value) {
            if (firstTime.value == null) {
                firstTime.value = true
            }
        }
        SideEffect {
            if (firstTime.value == true) {
                firstTime.value = false
                focusRequester.requestFocus()
            }
        }
    }

    @Composable
    private fun CollectUIEvents(
        viewModel: WordInfoViewModel,
        snackbarHostState: SnackbarHostState
    ) {
        // Collect UI events from viewModel eventFlow
        LaunchedEffect(key1 = true) {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is WordInfoViewModel.UIEvent.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }
}