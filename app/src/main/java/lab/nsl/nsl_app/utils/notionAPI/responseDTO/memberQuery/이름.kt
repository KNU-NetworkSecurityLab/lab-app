package lab.nsl.nsl_app.utils.notionAPI.responseDTO.memberQuery

data class 이름(
    val id: String,
    val title: List<Title>,
    val type: String
)