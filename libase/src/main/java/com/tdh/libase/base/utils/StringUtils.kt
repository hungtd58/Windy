fun String?.isNothing(): Boolean {
    return this == null || this.trim().isEmpty()
}

fun String?.stripAccent(): String {
    if (this == null) {
        return ""
    }
    var stripAccent: String = org.apache.commons.lang3.StringUtils.stripAccents(this)
    stripAccent = replaceSpecialAccent(stripAccent)
    return stripAccent
}

private fun replaceSpecialAccent(s: String): String {
    var result = s.replace("đ".toRegex(), "d")
    result = result.replace("Đ".toRegex(), "D")
    return result
}