import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.todolistssy.R

object AppIcons {
        val History: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_history)
}