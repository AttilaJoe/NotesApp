package com.example.notesapp.ui
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.NoteViewModel
import com.example.notesapp.data.model.Note
import okhttp3.internal.format
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    NotesScreen(noteViewModel)
                }
            }
        }
        noteViewModel.fetchNotes()
    }
}
@Composable
fun NotesScreen(noteViewModel: NoteViewModel) {
    val notes by noteViewModel.notes.observeAsState(emptyList())
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Tambah Catatan Baru", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (title.isNotBlank() && content.isNotBlank()) {
                noteViewModel.addNote(Note(title = title, content = content))
                title = ""
                content = ""
            }
        }) {
            Text("Simpan Catatan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Daftar Catatan", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        notes.forEach { note ->
            Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = note.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = note.content, style = MaterialTheme.typography.titleMedium)
                    Text(text = formatTimestamp(note.timestamp), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: com.google.firebase.Timestamp?): String {
    return if (timestamp != null) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        sdf.format(timestamp.toDate())
    } else {
        "-"
    }
}
