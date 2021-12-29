import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.Props
import react.dom.h1
import react.dom.html.ReactHTML.input
import react.dom.li
import react.dom.ul
import react.functionComponent
import react.useEffectOnce
import react.useState

private val scope = MainScope()

val app = functionComponent<Props> {_ ->
    var shoppingList by useState(emptyList<ShoppingListItem>())

    useEffectOnce {
        scope.launch {
            shoppingList = getShoppingList()
        }
    }

    h1 {
        +"Full-Stack Shopping List"
    }

    ul {
        shoppingList.sortedByDescending(ShoppingListItem::priority).forEach { item ->
            li {
                key = item.toString()
                +"[${item.priority}] ${item.desc}"
            }
        }
    }

    child(inputComponent) {
        attrs.onSubmit = { input ->
            val cartItem = ShoppingListItem(input.replace("!", ""), input.count { it == '!' })
            scope.launch {
                addShoppingListItem(cartItem)
                shoppingList = getShoppingList()
            }
        }
    }}