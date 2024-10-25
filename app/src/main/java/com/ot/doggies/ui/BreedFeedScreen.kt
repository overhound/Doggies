package com.ot.doggies.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ot.doggies.R
import com.ot.doggies.ui.viewmodel.BreedFeedViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BreedFeedScreen(
    modifier: Modifier = Modifier,
    viewmodel: BreedFeedViewModel = koinViewModel(),
    onBreedClick: (String, String) -> Unit
) {
    val breeds = viewmodel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.dog_api_logo),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            contentDescription = stringResource(R.string.content_description_app_logo),
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterHorizontally)
        )

        BreedFeed(breeds.value.breeds) { breed, subBreed ->
            onBreedClick(breed, subBreed)
        }
    }
}

@Composable
fun BreedFeed(
    breeds: Map<String, List<String>>,
    onBreedClick: (String, String) -> Unit,
) {
    val isExpandedMap = remember { mutableStateMapOf<String, Boolean>() }
    val listState = rememberLazyListState()
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        ) {
            val totalBreeds = breeds.size
            breeds.entries.forEachIndexed { index, (breed, subBreeds) ->
                breedSection(
                    index = index,
                    totalItems = totalBreeds,
                    breed = breed,
                    subBreeds = subBreeds,
                    isExpanded = isExpandedMap[breed] == true,
                    onHeaderClick = {
                        isExpandedMap[breed] = isExpandedMap[breed] != true
                    },
                    onBreedClick = { breed, subBreed ->
                        onBreedClick(breed, subBreed)
                    }
                )
            }
        }
    }
}

@Composable
fun BreedHeader(breed: String, onBreedClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onBreedClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = breed,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier

        )
    }
}

@Composable
fun BreedHeaderExpandable(
    breed: String,
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit,
    onBreedClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onBreedClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = breed,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1.0f)
        )
        Icon(
            modifier = Modifier
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onHeaderClicked() },
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.content_description_collapse),
        )
    }
}


@Composable
fun BreedItem(subBreed: String, onSubBreedClick: () -> Unit) {
    Text(
        text = subBreed,
        style = MaterialTheme.typography.bodyMedium,
        fontStyle = FontStyle.Normal,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSubBreedClick() }
            .padding(vertical = 8.dp, horizontal = 42.dp)
    )
}

fun LazyListScope.breedSection(
    index: Int,
    totalItems: Int,
    breed: String,
    subBreeds: List<String>,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    onBreedClick: (String, String) -> Unit
) {

    item {
        if (subBreeds.isEmpty()) {
            BreedHeader(breed) { onBreedClick(breed, "") }
        } else {
            BreedHeaderExpandable(
                breed = breed,
                isExpanded = isExpanded,
                onHeaderClicked = onHeaderClick,
                onBreedClick = { onBreedClick(breed, "") }
            )
        }
        ShowDivider(totalItems, index)
    }

    if (isExpanded) {
        itemsIndexed(subBreeds) { index, subBreed ->
            BreedItem(subBreed = subBreed, onSubBreedClick = { onBreedClick(breed, subBreed) })
            Spacer(modifier = Modifier.size(1.dp))
            ShowDivider(subBreeds.size, index, 42.dp)
        }
    }
}

@Composable
private fun ShowDivider(
    totalItems: Int,
    index: Int,
    padding: Dp = 16.dp
) {
    if (index < totalItems - 1) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.tertiary,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = padding)
        )
    }
}

val sampleBreeds = mapOf(
    "Hound" to listOf("Afghan", "Basset", "Bloodhound"),
    "Akita" to emptyList(),
    "Retriever" to listOf("Golden", "Chesapeake", "Flat-coated")
)

@Preview(showBackground = true)
@Composable
fun PreviewBreedHeader() {
    BreedHeader(breed = sampleBreeds.getValue("Hound").first(), {})
}

@Preview(showBackground = true)
@Composable
fun PreviewBreedItem() {
    BreedItem(subBreed = sampleBreeds.getValue("Hound").first(), {})
}

@Preview(showBackground = true)
@Composable
fun PreviewBreedExpandable() {
    BreedHeaderExpandable(
        breed = sampleBreeds.getValue("Hound").first(),
        isExpanded = true,
        onHeaderClicked = {},
        onBreedClick = {})
}

@Preview
@Composable
fun PreviewBreedFeed() {
    BreedFeed(breeds = sampleBreeds) { _, _ -> }
}