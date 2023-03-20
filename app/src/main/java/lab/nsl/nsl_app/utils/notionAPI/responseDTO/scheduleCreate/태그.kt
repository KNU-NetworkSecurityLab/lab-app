package lab.nsl.nsl_app.utils.notionAPI.responseDTO.scheduleCreate

data class 태그(
    val id: String,
    val multi_select: List<MultiSelect>,
    val type: String
)