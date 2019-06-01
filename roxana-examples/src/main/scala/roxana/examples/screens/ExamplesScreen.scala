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

package roxana.examples.screens

import org.scalajs.dom
import org.scalajs.dom.html.Div
import roxana.core.{Component, RoxanaContext}
import roxana.examples.containers.{todoList, validationsDemo}
import roxana.examples.{Router, Routes}
import roxana.routing.Screen
import roxana.toolkit.forms.rxLink
import rx._
import scalatags.JsDom

class ExamplesScreen(implicit rxCtx: RoxanaContext) extends Screen {

  import rxCtx._

  val exampleName: Var[Option[String]] = Var(None)
  val examples: Map[String, (String, () => Component[_ <: dom.Element])] = Map(
    "todoList" -> ("To-Do List", () => todoList()),
    "validationsDemo" -> ("Form Validations", () => validationsDemo())
  )
  private val example: Rx[Option[Component[_ <: dom.Element]]] =
    Rx(exampleName().map(examples(_)._2.apply()))

  override protected def buildTag: JsDom.TypedTag[Div] = {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    div(cls := "container mt-5 mb-5",
      div(cls := "row",
        div(cls := "col-md-3", menuContainer),
        div(cls := "col-md-9 mt-5", exampleContainer)
      )
    )

  }

  private val exampleContainer = Rx {
    import scalatags.JsDom.all._

    example() match {
      case Some(comp) => div(comp.elem)
      case None => div(
        """
          |Welcome to the Roxana example project, which demonstrates some of the key aspects of the
          |framework. Please select one of the examples from the left menu.
        """.stripMargin)
    }
  }

  private val menuContainer = Rx {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    def linkCls(name: String): String =
      "nav-item " + exampleName().filter(_ == name).map(_ => "current").getOrElse("")

    ul(cls := "nav flex-column left-menu",
      h2("Available examples"),
      for ((eKey, (eName, _)) <- examples) yield li(cls := linkCls(eKey),
        rxLink(eName, _ => Router.routeTo(Routes.example(eKey)), cls = "nav-link"))
    )
  }
}
