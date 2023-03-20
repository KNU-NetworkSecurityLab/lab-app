package lab.nsl.nsl_app.utils.notionAPI.responseDTO.retrieveDatabase

data class WinePairing(
    val id: String,
    val name: String,
    val rich_text: RichText,
    val type: String
)