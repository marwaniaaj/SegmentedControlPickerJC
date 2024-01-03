package com.example.segmentedcontrolpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.segmentedcontrolpicker.ui.theme.PickerCyan
import com.example.segmentedcontrolpicker.ui.theme.SegmentedControlPickerTheme
import java.util.UUID

data class Book(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val author: String = "",
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SegmentedControlPickerTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val booksList = listOf(
        Book(title = "Atomic Habits", author = "James Clear"),
        Book(title = "Start With Why", author = "Simon Sinek"),
        Book(title = "Think Like A Monk", author = "Jay Shetty"),
        Book(title = "Limitless", author = "Jim Kwik")
    )
    val books by remember { mutableStateOf(booksList) }
    val selectedBook = remember { mutableStateOf<Book?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Segmented Picker") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
                .fillMaxSize()
                .wrapContentSize(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SegmentedPicker(
                items = books,
                selectedItem = selectedBook
            ) { book ->
                Text(
                    text = book?.title ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    softWrap = false,
                    maxLines = 1,
                    color = if (book == selectedBook.value) Color.White else Color.Black
                )
            }

            selectedBook.value?.let {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${it.title} by ",
                        fontSize = 20.sp,
                    )
                    Text(
                        text = it.author,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PickerCyan
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SegmentedControlPickerTheme {
        MainScreen()
    }
}