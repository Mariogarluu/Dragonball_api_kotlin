package com.mariogarluu.dragonballapi.ui.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A reusable top app bar for the application.
 *
 * @param title The title to be displayed in the top app bar.
 * @param imageVector The image vector for the navigation icon. If null, no icon is displayed.
 * @param onClick The callback to be invoked when the navigation icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    imageVector: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (imageVector != null && onClick != null) {
                IconButton(onClick = onClick) {
                    Icon(imageVector = imageVector, contentDescription = "Back")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}