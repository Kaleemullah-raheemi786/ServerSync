package com.example.server_sync.ui.theme.others

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.server_sync.network.model.AppItem
import androidx.compose.foundation.layout.size
import androidx.compose.ui.tooling.preview.Preview
import com.example.server_sync.ui.theme.AptoideAppTheme

@Composable
fun AppItemCard(app: AppItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = app.icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = app.name, fontWeight = FontWeight.Bold)
                Text(text = "Rating: ${app.rating}")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppItemCardPreview() {
    AptoideAppTheme {
        AppItemCard(
            AppItem(
                id = 1L,
                name = "Sample App",
                packageName = "com.example.sample",
                store_id = 100,
                store_name = "Sample Store",
                vername = "1.0.0",
                vercode = 1,
                md5sum = "abc123def456",
                apk_tags = listOf("Games", "Action"),
                size = 12345678L,
                downloads = 1000,
                pdownloads = 500,
                added = "2024-07-01",
                modified = "2024-07-05",
                updated = "2024-07-10",
                rating = 85.0,
                icon = "https://via.placeholder.com/150",
                graphic = null,
                uptype = "latest"
            ),
            onClick = {}
        )
    }
}

