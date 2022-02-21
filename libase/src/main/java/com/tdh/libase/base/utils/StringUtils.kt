fun String?.isNothing(): Boolean {
    return this == null || this.trim().isEmpty()
}