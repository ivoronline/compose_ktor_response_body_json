package com.ivoronline.compose_ktor_response_body_json

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

//==================================================================
// MAIN ACTIVITY
//==================================================================
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {

      var person by remember { mutableStateOf(Person(0, "No name", 0)) }
      val coroutineScope = rememberCoroutineScope()

      Button(onClick = { coroutineScope.launch { person = callURL() } }) {
        Text("RESPONSE: $person")
      }

    }
  }
}

//==================================================================
// CALL URL
//==================================================================
suspend fun callURL() : Person {

  //CONFIGURE CLIENT
  val client = HttpClient(CIO) {
    install(ContentNegotiation){ json() }
  }

  //CAL URL
  val person: Person = client.get("http://192.168.0.102:8080/ReceiveBodyJSON").body()

  //CLOSE CLIENT
  client.close()

  //RETURN PERSON
  println(person)  //Person(id=1, name=John, age=20)
  return person;

}

//==================================================================
// PERSON
//==================================================================
@Serializable
data class Person(val id: Int, val name: String, val age: Int)
