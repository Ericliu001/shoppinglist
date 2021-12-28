import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class ShoppingListItem(val desc: String, val priority: Int) {
    val id: Int = Random.nextInt()

    companion object {
        const val path = "/shoppingList"
    }
}

