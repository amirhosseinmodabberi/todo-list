package com.example.taskcloud.ui.screens.taskeditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskcloud.model.Task
import com.example.taskcloud.ui.theme.TaskCloudTheme

@Composable
fun TaskEditorScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: TaskEditorViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TaskEditorScreen(
        uiState = uiState,
        onAddTask = {
            viewModel.addTask(it)
            onBackClick()
        },
        onUpdateTask = {
            viewModel.updateTask(it)
            onBackClick()
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    uiState: TaskEditorUiState,
    onAddTask: (Task) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onBackClick: () -> Unit,
) {
    when (uiState) {
        TaskEditorUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TaskEditorUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            if (uiState.taskToEdit != null) {
                                Text("Edit Task")
                            } else {
                                Text("Add Task")
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { onBackClick() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                AddTaskContent(
                    taskToEdit = uiState.taskToEdit,
                    onAddTask = { onAddTask(it) },
                    onEditTask = { onUpdateTask(it) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskContent(
    taskToEdit: Task?,
    onAddTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var taskName by remember(taskToEdit) { mutableStateOf(taskToEdit?.task ?: "") }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    var selectedDate by remember(taskToEdit) { mutableStateOf(taskToEdit?.dueDate) }

    var showTimePickerDialog by remember(taskToEdit) {
        mutableStateOf(false)
    }

    var selectedHour by remember(taskToEdit) {
        mutableStateOf(taskToEdit?.dueHour)
    }

    var selectedMinute by remember(taskToEdit) {
        mutableStateOf(taskToEdit?.dueMinute)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = taskName,
            onValueChange = { taskName = it },
            placeholder = {
                Text(text = "Enter your task")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { showDatePickerDialog = true }
        ) {
            Text(
                text = selectedDate?.toString() ?: "Select task date"
            )
        }

        OutlinedButton(
            onClick = { showTimePickerDialog = true }
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Select time"
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (selectedHour != null && selectedMinute != null) {
                    "%02d:%02d".format(selectedHour, selectedMinute)
                } else {
                    "Select task time"
                }
            )
        }

        Button(
            onClick = {
                if (taskToEdit != null) {
                    onEditTask(
                        taskToEdit.copy(
                            task = taskName,
                            dueDate = selectedDate,
                            dueHour = selectedHour,
                            dueMinute = selectedMinute
                        )
                    )
                } else {
                    onAddTask(
                        Task(
                            task = taskName,
                            dueDate = selectedDate,
                            dueHour = selectedHour,
                            dueMinute = selectedMinute
                        )
                    )
                }
            },
            enabled = taskName.isNotBlank(),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (taskToEdit != null) {
                Text("Edit Task")
            } else {
                Text("Add new task")
            }
        }
    }

    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(initialSelectedDate = selectedDate)

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = datePickerState.getSelectedDate()
                        showDatePickerDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.error)
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
    if (showTimePickerDialog) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour ?: 9,
            initialMinute = selectedMinute ?: 0,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = {
                showTimePickerDialog = false
            },
            title = {
                Text("Select task time")
            },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        showTimePickerDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePickerDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview
@Composable
private fun TaskEditorScreenPreview() {
    TaskCloudTheme {
        TaskEditorScreen(
            uiState = TaskEditorUiState.Success(),
            onAddTask = {},
            onUpdateTask = {},
            onBackClick = {},
        )
    }
}
