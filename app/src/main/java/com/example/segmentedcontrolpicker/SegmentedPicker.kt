package com.example.segmentedcontrolpicker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.segmentedcontrolpicker.ui.theme.PickerCyan


@Composable
fun <T> SegmentedPicker(
    items: List<T>,
    modifier: Modifier = Modifier,
    selectedItem: MutableState<T>,
    content: @Composable LazyItemScope.(item: T) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    LazyRow(
        modifier = modifier.wrapContentWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (selectedItem.value == null)
            selectedItem.value = items.first()

        itemsIndexed(items) { index, item ->
            val isSelected = (selectedItem.value == item)
            val transition = updateTransition(isSelected, label = "selected state")
            val indexTransition = updateTransition(selectedIndex, label = "selected index")

            Box(
                modifier = modifier
                    .height(intrinsicSize = IntrinsicSize.Max)
                    .width(intrinsicSize = IntrinsicSize.Max)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        selectedItem.value = item
                        selectedIndex = index
                    },
                contentAlignment = Alignment.Center
            ) {
                transition.AnimatedContent(
                    transitionSpec = {
                        if (indexTransition.currentState < indexTransition.targetState) {
                            ContentTransform(
                                targetContentEnter = slideInHorizontally(
                                    animationSpec = tween(200),
                                ) + fadeIn(),
                                initialContentExit = slideOutHorizontally(
                                    animationSpec = tween(150),
                                    targetOffsetX = { fullWidth -> fullWidth }
                                ) + fadeOut()
                            )
                        } else {
                            ContentTransform(
                                targetContentEnter = slideInHorizontally(
                                    animationSpec = tween(200),
                                    initialOffsetX = { fullWidth -> fullWidth }
                                ) + fadeIn(),
                                initialContentExit = slideOutHorizontally(
                                    animationSpec = tween(150),
                                ) + fadeOut()
                            )
                        }
                    }
                ) { targetState ->
                    Canvas(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        drawRoundRect(
                            color = if (targetState) PickerCyan else Color.Transparent,
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(20.dp.toPx())
                        )
                    }
                }
                Row(modifier = Modifier.padding(16.dp, 8.dp)) {
                    content(item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedPickerPreview() {
    val booksList = listOf("Atomic Habits", "Start With Why", "Think Like A Monk", "Limitless")
    val books by remember { mutableStateOf(booksList) }
    val selectedBook = remember { mutableStateOf("Atomic Habits") }

    SegmentedPicker(
        items = books,
        selectedItem = selectedBook
    ) {
        val color = if (it == selectedBook.value) Color.White else Color.Black
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = it,
                color = color
            )
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = "Book icon",
                tint = color
            )
        }
    }
}