package com.main.chatter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.main.chatter.database.Message
import com.main.chatter.ui.theme.ChatterTheme
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Renders a single message in a card.
 */

val testMessage = Message(
    author = "Kalle",
    content = "This is a test message",
    timeStamp = System.currentTimeMillis()
)

@Composable
fun MessageCard(msg: Message) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .padding(bottom = 4.dp, top = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
            Arrangement.Start,
            Alignment.CenterVertically
        ) {
            Text(
                text = msg.author,
                Modifier.padding(end = 4.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium,
            )

            val msgDate = Date(msg.timeStamp)

            val currentDate = Date(System.currentTimeMillis())

            var pattern = "dd MMMM | HH:mm"

            var textDate = ""

            if (getDateInstance().format(msgDate) == getDateInstance().format(currentDate)) {
                pattern = "HH:mm"
                textDate = "Today | "
            }

            val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)

            Text(
                text = textDate + sdf.format(msgDate),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Text(
            text = msg.content,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        )
    }
}

@Preview
@Composable

fun PreviewMessage() {
    ChatterTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            MessageCard(msg = testMessage)
        }
    }
}