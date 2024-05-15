package com.srinivasan.contacts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.srinivasan.contacts.R

@Composable
fun ContactImage(
    modifier: Modifier,
    name: String,
    textSize: TextUnit = 20.sp,
    imageUrl: String?
) {

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        imageUrl?.let { thumbnailUri ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                model = thumbnailUri,
                contentDescription = stringResource(id = R.string.contact_image_thumbnail),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.ic_people)
            )
        } ?: run {
            Text(
                text = name.first().toString(),
                style = MaterialTheme.typography
                    .titleLarge.copy(fontSize = textSize),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

    }

}

@Composable
fun ProfileImage(
    modifier: Modifier,
    iconSize: Dp,
    photoUri: String?,
    onChange: () -> Unit,
    onRemove: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            photoUri?.let { uri ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    model = uri,
                    contentDescription = stringResource(id = R.string.contact_image_thumbnail),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_people)
                )
            } ?: run {
                Icon(
                    modifier = Modifier
                        .size(iconSize),
                    imageVector = Icons.Outlined.AddPhotoAlternate,
                    contentDescription = stringResource(id = R.string.add_photo_icon)
                )
            }

        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onChange
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(id = R.string.change_photo_icon)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = stringResource(id = R.string.change),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }

            TextButton(
                onClick = onRemove
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.remove_photo_icon)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = stringResource(id = R.string.remove),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }

        }

    }

}