package com.spruhs.screens.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spruhs.R

@Composable
fun UserImage(imageUrl: String?, size: Int) {
    AsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(imageUrl)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .fallback(R.drawable.profile_placeholder)
                .build(),
        contentDescription = "profile picture",
        modifier =
            Modifier
                .size(size.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
        contentScale = ContentScale.Crop
    )
}