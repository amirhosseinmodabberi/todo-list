package com.example.taskcloud.ui.screens.tasklist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.taskcloud.model.Task
import com.example.taskcloud.ui.theme.TaskCloudTheme
import java.time.LocalDate

@Composable
fun TaskListScreen(
    navigateToTaskEditor: (Task?) -> Unit,
) {
    val viewModel: TaskListViewModel = hiltViewModel()

    val context = LocalContext.current

    val tasksMap by viewModel.tasksMap.collectAsState()

    TaskListScreen(
        tasksMap = tasksMap,
        onUpdate = { viewModel.updateTask(it) },
        onDelete = { viewModel.deleteTask(it) },
        navigateToEditTask = { navigateToTaskEditor(it) },
        navigateToAddTask = { navigateToTaskEditor(null) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListScreen(
    tasksMap: Map<LocalDate?, List<Task>>,
    onUpdate: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    navigateToEditTask: (Task) -> Unit,
    navigateToAddTask: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Task List")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddTask() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            tasksMap.forEach { (date, tasks) ->

                stickyHeader { DateHeader(date) }

                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onIsCompletedChange = { isCompleted ->
                            onUpdate(task.copy(isCompleted = isCompleted))
                        },
                        onDelete = { onDelete(task) },
                        onEdit = { navigateToEditTask(task) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateHeader(
    dueDate: LocalDate?
) {
    val today = remember { LocalDate.now() }

    val backColor = if (dueDate != null && dueDate < today) {
        MaterialTheme.colorScheme.error
    } else {
        Color(0xFF2196F3)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backColor, MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = dueDate?.toString() ?: "No date",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onIsCompletedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val today = remember { LocalDate.now() }

    val dueDateColor = if (task.dueDate != null && task.dueDate < today) {
        MaterialTheme.colorScheme.error
    } else {
        Color(0xFF666666)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onIsCompletedChange(it) },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = task.task,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = task.dueDate?.toString() ?: "No date",
                style = MaterialTheme.typography.bodyMedium,
                color = dueDateColor
            )
            Text(
                text = "${task.dueHour ?: "No time set"}:${task.dueMinute ?: "No time set"}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More menu"
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                containerColor = MaterialTheme.colorScheme.background
            ) {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Task",
                                tint = Color(0xFF2196F3)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Edit")
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onEdit()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Task",
                                tint = Color(0xFFF44336)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Delete")
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onDelete()
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TaskListScreenPreview() {
    TaskCloudTheme {
        val tasksMap = mapOf<LocalDate?, List<Task>>(
            null to listOf(
                Task(task = "Go to gym"),
                Task(task = "Read book"),
                Task(task = "Eat dinner")
            )
        )
        TaskListScreen(
            tasksMap = tasksMap,
            navigateToAddTask = {},
            onUpdate = {},
            onDelete = {},
            navigateToEditTask = {}
        )
    }
}
