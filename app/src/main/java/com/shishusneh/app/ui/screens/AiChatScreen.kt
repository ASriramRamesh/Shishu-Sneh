package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.shishusneh.app.data.model.getAiResponse
import com.shishusneh.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class ChatMessage(val text: String, val isUser: Boolean, val id: Long = System.currentTimeMillis())

private val SUGGESTED_QUESTIONS = listOf(
    "My baby has a fever. What should I do?",
    "When should I start solid foods?",
    "How do I know if my baby is hungry?",
    "Baby is not sleeping at night",
    "Is it normal for baby to spit up?"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(onBack: () -> Unit) {
    var messages by remember {
        mutableStateOf(listOf(
            ChatMessage("Hello! I am your AI health guide. Ask me anything about your baby — fever, feeding, sleep, vaccines, milestones, and more.\n\nNote: Always consult your paediatrician for medical decisions.", false)
        ))
    }
    var input by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun send(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(text, true)
        messages = messages + userMsg
        input = ""
        isTyping = true
        scope.launch {
            listState.animateScrollToItem(messages.lastIndex)
            delay(800)
            val reply = getAiResponse(text)
            messages = messages + ChatMessage(reply, false)
            isTyping = false
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFE3F2FD)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.SmartToy, null, tint = Info, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text("AI Health Guide", style = MaterialTheme.typography.titleMedium)
                            Text("Keyword-based • Offline", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background,
        bottomBar = {
            Column(modifier = Modifier.background(Color.White).navigationBarsPadding()) {
                HorizontalDivider(color = Border)
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        placeholder = { Text("Ask a health question...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = { send(input) }),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                    )
                    FloatingActionButton(
                        onClick = { send(input) },
                        containerColor = Primary,
                        contentColor = Color.White,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(msg)
            }
            if (isTyping) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(start = 8.dp)) {
                        repeat(3) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Primary.copy(alpha = 0.4f)))
                        }
                    }
                }
            }
            if (messages.size == 1) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Suggested questions", style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary))
                        SUGGESTED_QUESTIONS.forEach { q ->
                            SuggestionChip(
                                onClick = { send(q) },
                                label = { Text(q, style = MaterialTheme.typography.bodySmall) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isUser) {
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE3F2FD)).align(Alignment.Bottom), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.SmartToy, null, tint = Info, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(8.dp))
        }
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp,
                    bottomStart = if (msg.isUser) 16.dp else 4.dp,
                    bottomEnd = if (msg.isUser) 4.dp else 16.dp
                ))
                .background(if (msg.isUser) Primary else Color.White)
                .padding(12.dp)
        ) {
            Text(msg.text, style = MaterialTheme.typography.bodyMedium.copy(color = if (msg.isUser) Color.White else TextPrimary))
        }
    }
}
