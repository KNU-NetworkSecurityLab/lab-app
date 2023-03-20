package lab.nsl.nsl_app.models

data class BookItemListItem(
    val bookAuthor: String,
    val bookName: String,
    val id: Int,
    val bookTagList: ArrayList<String>
)