import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val shoppingList = createShoppingList()

fun main() {

    embeddedServer(Netty, 9090) {
        install(ContentNegotiation) {
            json()
        }

        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }

        install(Compression) {
            gzip()
        }

        routing {
            route(ShoppingListItem.path) {
                get {
                    call.respond(shoppingList)
                }

                post {
                    val receivedItem = call.receive<ShoppingListItem>()
                    shoppingList.add(receivedItem)
                    call.respond(HttpStatusCode.OK, receivedItem)
                }

                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request.")
                    val itemToDelete = shoppingList.find { it.id == id } ?: error("Item with id {$id} not found.")
                    call.respond(itemToDelete)
                }
            }

            get("/hello") {
                call.respondText("Hello, API!")
            }
        }
    }.start(wait = true)
}

fun createShoppingList(): MutableList<ShoppingListItem> {
    return mutableListOf(
        ShoppingListItem("Cucumbers 🥒", 1),
        ShoppingListItem("Tomatoes 🍅", 2),
        ShoppingListItem("Orange Juice 🍊", 3)
    )
}