package roxana.core.l10n

case class Messages(messages: Map[String, String]) {
  def ++(other: Messages): Messages = this.copy(messages = messages ++ other.messages)
}

object Messages {
  val Empty = Messages(Map.empty)
}
