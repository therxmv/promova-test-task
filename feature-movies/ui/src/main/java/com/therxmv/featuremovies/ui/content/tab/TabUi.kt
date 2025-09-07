package com.therxmv.featuremovies.ui.content.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.therxmv.base.ui.theme.PromovaTheme
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem

@Composable
internal fun MovieItem(
    modifier: Modifier = Modifier,
    data: UiMovieItem.Movie,
    onEvent: (MoviesUiEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box {
            Poster(data.posterUrl)

            ScorePill(
                modifier = Modifier.align(Alignment.BottomCenter),
                score = data.averageScore,
            )
        }

        Column(
            modifier = Modifier.padding(PromovaTheme.paddings.main),
        ) {
            Title(text = data.title)
            Spacer(modifier = Modifier.height(PromovaTheme.paddings.halfOfMain))

            Description(text = data.description)

            if (data.actions.isNotEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                ActionIconsRow(
                    items = data.actions,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun ActionIconsRow(
    items: List<UiMovieItem.Movie.Action>,
    onEvent: (MoviesUiEvent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        items.forEach { data ->
            ActionIcon(
                data = data,
                onClick = { onEvent(data.event) }
            )
        }
    }
}

@Composable
private fun ActionIcon(
    data: UiMovieItem.Movie.Action,
    onClick: () -> Unit,
) {
    val colors = if (data.isEnabled) {
        IconButtonDefaults.filledIconButtonColors()
    } else {
        IconButtonDefaults.filledIconButtonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        )
    }

    FilledIconButton(
        onClick = onClick,
        colors = colors,
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = null,
        )
    }
}

@Composable
private fun Poster(
    url: String,
) {
    AsyncImage(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(0.75f),
        model = url,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

@Composable
private fun ScorePill(
    modifier: Modifier = Modifier,
    score: String,
) {
    Text(
        modifier = modifier
            .padding(bottom = 4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 8.dp),
        text = score,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun Title(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun Description(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
internal fun DateSeparator(
    modifier: Modifier = Modifier,
    date: String,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = PromovaTheme.paddings.main),
        text = date,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
internal fun EmptyPlaceholder(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Preview(showBackground = true)
@Composable
private fun MovieItemPreview() {
    PromovaTheme {
        MovieItem(
            data = UiMovieItem.Movie(
                id = 0,
                title = "Top Gun",
                description = LoremIpsum(20).values.joinToString(),
                posterUrl = "url",
                averageScore = "4.8",
                releaseDate = "Jun 2025",
                actions = listOf(
                    UiMovieItem.Movie.Action(
                        icon = Icons.Default.Star,
                        isEnabled = false,
                        event = MoviesUiEvent.ToggleFavoriteMovie(0, true),
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPlaceholderPreview() {
    PromovaTheme {
        EmptyPlaceholder("There are no items to display!")
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthAndYearItemPreview() {
    PromovaTheme {
        DateSeparator(
            date = "Sep 2025",
        )
    }
}