package come.hasan.foraty.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import come.hasan.foraty.note.screens.Destinations
import come.hasan.foraty.note.screens.Navigation
import come.hasan.foraty.note.screens.localNavController
import come.hasan.foraty.note.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    CompositionLocalProvider(localNavController provides navController) {
                        Navigation(startDestination = Destinations.Home.route)
                    }
                }
            }
        }
    }
}
