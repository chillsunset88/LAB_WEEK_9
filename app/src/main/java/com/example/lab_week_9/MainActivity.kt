package com.example.lab_week_9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_week_9.ui.theme.LAB_WEEK_9Theme
import com.example.lab_week_9.ui.theme.OnBackgroundItemText
import com.example.lab_week_9.ui.theme.OnBackgroundTitleText
import com.example.lab_week_9.ui.theme.PrimaryTextButton

data class Student(
    val name: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_9Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home(
                navigateFromHomeToResult = { listData ->
                    navController.navigate("resultContent/?listData=$listData")
                }
            )
        }
        composable(
            route = "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            ResultContent(
                listData = backStackEntry.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

@Composable
fun Home(navigateFromHomeToResult: (String) -> Unit) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    var inputValue by remember { mutableStateOf("") }

    HomeContent(
        listData = listData,
        inputValue = inputValue,
        onInputValueChange = { inputValue = it },
        onButtonClick = {
            if (inputValue.isNotBlank()) {
                listData.add(Student(inputValue))
                inputValue = ""
            }
        },
        navigateFromHomeToResult = {
            val dataString = listData.joinToString(", ") { it.name }
            navigateFromHomeToResult(dataString)
        }
    )
}

@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))
                TextField(
                    value = inputValue,
                    onValueChange = onInputValueChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_click),
                        onClick = onButtonClick
                    )
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_navigate),
                        onClick = navigateFromHomeToResult
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

@Composable
fun ResultContent(listData: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundTitleText(text = "Result")
        Spacer(modifier = Modifier.height(16.dp))
        OnBackgroundItemText(text = listData)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_9Theme {
        Home(navigateFromHomeToResult = {})
    }
}