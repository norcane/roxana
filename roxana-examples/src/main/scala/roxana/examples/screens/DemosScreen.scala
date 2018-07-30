/*
 * roxana :: Reactive UI component framework for Scala.js applications
 * Copyright (c) 2018 norcane
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

package roxana.examples.screens

import org.scalajs.dom
import org.scalajs.dom.html.Div
import roxana.core.Component
import roxana.examples.containers.todoList
import roxana.examples.{Router, Routes}
import roxana.routing.Screen
import roxana.toolkit.forms.rxLink
import rx._
import scalatags.JsDom

class DemosScreen(implicit rxCtx: Ctx.Owner) extends Screen {

  val demoName: Var[Option[String]] = Var(None)
  val demos: Map[String, () => Component[_ <: dom.Element]] = Map(
    "todoList" -> (() => todoList()),
  )
  private val demo: Rx[Option[Component[_ <: dom.Element]]] = Rx(demoName().map(demos(_).apply()))

  override protected def buildTag: JsDom.TypedTag[Div] = {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    div(cls := "container mt-5 mb-5",
      div(cls := "row",
        div(cls := "col-md-3", renderMenu),
        div(cls := "col-md-9 mt-5", renderDemo)
      )
    )

  }

  private def renderDemo = Rx {
    import scalatags.JsDom.all._

    demo() match {
      case Some(comp) => div(comp.elem)
      case None => div(
        """
          |Welcome to the Roxana example project, which demonstrates some of the key aspects of the
          |framework. Please select one of the examples from the left menu.
        """.stripMargin)
    }
  }

  private def renderMenu = Rx {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    def linkCls(name: String): String =
      "nav-item " + demoName().filter(_ == name).map(_ => "current").getOrElse("")

    ul(cls := "nav flex-column left-menu",
      h2("Available examples"),
      for ((dName, _) <- demos) yield li(cls := linkCls(dName),
        rxLink(dName, _ => Router.routeTo(Routes.example(dName)), cls = "nav-link"))
    )
  }
}
