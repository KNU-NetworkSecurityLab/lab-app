package lab.nsl.nsl_app.utils.notionAPI.responseDTO.memberQuery

data class Github(
    val id: String,
    val rich_text: List<RichText>,
    val type: String
)