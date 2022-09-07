package com.example.fakeshopping

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.fakeshopping.ui.theme.FakeShoppingTheme
import com.example.fakeshopping.utils.LoginScreenRoutes
import com.example.fakeshopping.utils.LoginStateDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val Context.dataStore by preferencesDataStore(
        LoginStateDataStore.dataStoreName
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Blue.toArgb()

        setContent {

            FakeShoppingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().statusBarsPadding(),
                    color = MaterialTheme.colors.background
                ) {

                    Navigation(
                        window,
                        onLoggedStateChanged = { id ->
                            if(!id.isNullOrEmpty()){
                                createOrSetLoginState(id)
                            }else{
                                createOrSetLoginState(null)
                            }
                        },
                        getCurrentLoggedUser = {
                            readCurrentLoggedUser()
                        }
                    )

                }
            }

        }

    }

    private fun readCurrentLoggedUser():String?{

        val dataStoreKey = stringPreferencesKey(LoginStateDataStore.DATASTORE_USER_ID_KEY)
        val preferences: Preferences
        runBlocking {
            preferences = dataStore.data.first()
        }
        return preferences[dataStoreKey]

    }

    private fun createOrSetLoginState(userId:String?){

        val dataStoreUserIdKey = stringPreferencesKey(LoginStateDataStore.DATASTORE_USER_ID_KEY)
        val preferences:Preferences
            runBlocking {

            preferences = dataStore.edit { preferences ->
                preferences[dataStoreUserIdKey] = userId ?: ""
            }

        }

    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FakeShoppingTheme {

    }
}
