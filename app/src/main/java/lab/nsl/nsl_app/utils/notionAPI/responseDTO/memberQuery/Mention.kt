package lab.nsl.nsl_app.utils.notionAPI.responseDTO.memberQuery

data class Mention(
    val id: String,
    val people: List<People>,
    val type: String
)