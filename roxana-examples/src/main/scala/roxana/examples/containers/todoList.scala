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

package roxana.examples.containers

import cats.Id
import org.scalajs.dom
import org.scalajs.dom.html.LI
import roxana.core.helpers._
import roxana.core.{Component, RoxanaContext}
import roxana.toolkit.forms.{rxButton, rxForm, rxInputText}
import rx._
import scalatags.JsDom

case class todoList()(implicit rxCtx: RoxanaContext)
  extends Component[dom.html.Div] {

  // import implicit stuff from Roxana context
  import rxCtx._

  // --- public API
  val items: Var[List[todoList.Item]] = Var(List.empty)

  // component bindings
  // FIXME maybe there is better solution for this?
  private val inputNewItem = binding[rxInputText[Id]]
  private val formNewItem = binding[rxForm]

  override protected def buildTag: JsDom.TypedTag[dom.html.Div] = {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    val todoItems: Rx.Dynamic[List[LI]] = Rx(items().map(renderItem(_).render))

    div(cls := "row justify-content-md-center",
      div(cls := "card card-body bg-light col-md-10",
        h1(cls := "mb-5", "Things to do:"),
        renderEmptyWarning(),
        ul(cls := "list-group",
          todoItems
        ),
        div(
          hr(),
          div(cls := "clearfix",
            rxForm(cls = "form-horizontal", onSubmit = handleSubmit, autoFocus = true) { implicit form =>
              div(
                div(cls := "form-group row justify-content-md-center",
                  label(cls := "col-md-2 col-form-label", `for` := "newItem", "Task"),
                  div(cls := "col-md-6",
                    rxInputText[Id](id = "newItem", placeholder = "What do you need to do?",
                      modifiers = Seq(autocomplete := "off")).bindTo(inputNewItem)
                  )
                ),
                div(cls := "row justify-content-md-center",
                  div(cls := "col-md-8 text-right",
                    // FIXME try to figure out how to get rid of the enclosing Rx()
                    Rx(rxButton("Add task", onClick = _ => form.submit(), cls = "btn-primary",
                      disabled = !form.valid()))
                  )
                )
              )
            }.bindTo(formNewItem)
          )
        )
      )
    )
  }

  private def renderItem(item: todoList.Item): JsDom.TypedTag[dom.html.LI] = {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    li(cls := "list-group-item clearfix",
      span(cls := "align-middle", item.text),
      div(cls := "float-right",
        rxButton("Ｘ", onClick = removeItem(item), cls = "btn-sm btn-danger img-circle")
      )
    )
  }

  private def renderEmptyWarning() = {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    val cssDisplay = items map (itemsSet => if (itemsSet.isEmpty) "block" else "none")

    div(display := cssDisplay, cls := "alert alert-info", "No items left to be done. Yay!!!")
  }

  private def removeItem(item: todoList.Item)(ev: dom.Event): Unit = {
    items() = items.now filterNot (_.hash == item.hash)
    bound(formNewItem).focus()
  }

  private def handleSubmit(form: rxForm): Unit = {
    items() = items.now :+ todoList.Item(bound(inputNewItem).rawValue.now)
    bound(formNewItem).reset()

  }
}

object todoList {
  case class Item(text: String, hash: Long = System.nanoTime())
}
