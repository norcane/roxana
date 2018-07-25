package roxana.toolkit.forms

import org.scalajs.dom
import org.scalajs.dom.html.Select
import roxana.core.Component
import rx._
import scalatags.JsDom

case class rxSelectMenu[T](
                            initialItems: Seq[rxSelectMenu.Item[T]] = Seq.empty,
                            onChange: rxSelectMenu.Item[T] => Unit = (_: rxSelectMenu.Item[T]) => ()
                          )
                          (implicit rxCtx: Ctx.Owner) extends Component[dom.html.Select] {

  // -- public API
  val items: Var[Seq[rxSelectMenu.Item[T]]] = Var(initialItems)

  private val mapping: Rx[Map[String, rxSelectMenu.Item[T]]] =
    Rx(items().map(item => item.value.hashCode().toString -> item).toMap)

  override protected def buildTag: JsDom.TypedTag[Select] = {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    val options = Rx(items()
      .map(item => option(item.label, value := item.value.hashCode()).render).toList)

    select(options)
  }

  override protected def buildElem(tag: JsDom.TypedTag[Select]): Select = {
    val elem = super.buildElem(tag)

    elem.onchange = (_: dom.Event) => {
      mapping.now.get(elem.value).foreach(onChange)
    }

    elem
  }
}

case object rxSelectMenu {

  case class Item[T](label: String, value: T)

}
