/*
 * roxana :: Reactive UI component framework for Scala.js applications
 * Copyright (c) 2018-2019 norcane
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package roxana.toolkit.forms

import org.scalajs.dom
import org.scalajs.dom.html.Select
import roxana.core.{Component, RoxanaContext}
import rx._
import scalatags.JsDom

case class rxSelect[T](initialItems: Seq[rxSelect.Item[T]] = Seq.empty,
                       onChange: rxSelect.Item[T] => Unit = (_: rxSelect.Item[T]) => ()
                          )(implicit rxCtx: RoxanaContext) extends Component[dom.html.Select] {

  import rxCtx._

  // -- public API
  val items: Var[Seq[rxSelect.Item[T]]] = Var(initialItems)

  private val mapping: Rx[Map[String, rxSelect.Item[T]]] =
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

case object rxSelect {

  case class Item[T](label: String, value: T)

}
