package com.verygoodsecurity.sdk.analytics.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.verygoodsecurity.sdk.analytics.VGSSharedAnalyticsManager
import com.verygoodsecurity.sdk.analytics.model.VGSAnalyticsEvent

class MainActivity : ComponentActivity() {

    private val analyticsManager =
        VGSSharedAnalyticsManager(
            source = "analyticsAndroidDemo",
            sourceVersion = "1.0.0",
            dependencyManager = "androidDependencyManagerDemo"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Content(
                    title = "",
                    onButtonClick = {
                        analyticsManager.capture(
                            vault = "<TENANT>",
                            environment = "sandbox",
                            formId = "androidDemoFormId",
                            VGSAnalyticsEvent.FieldAttach(
                                fieldType = "test",
                                contentPath = "test"
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun Content(
    title: String,
    onButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GreetingView(title)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onButtonClick) {
                Text(text = "Capture")
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Content(
            title = "Hello, Android!",
            onButtonClick = {}
        )
    }
}
