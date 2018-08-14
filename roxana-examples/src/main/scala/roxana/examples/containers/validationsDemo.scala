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

package roxana.examples.containers

import cats.Id
import org.scalajs.dom
import roxana.core.{Component, RoxanaContext}
import roxana.toolkit.forms.{rxForm, rxInputText}
import scalatags.JsDom

case class validationsDemo()(implicit rxCtx: RoxanaContext) extends Component[dom.html.Div] {

  override protected def buildTag: JsDom.TypedTag[dom.html.Div] = {
    import roxana.core.implicits._
    import scalatags.JsDom.all._

    div(cls := "row justify-content-md-center",
      div(cls := "col-md-12",
        rxForm() { implicit form =>
          div(cls := "form-group row justify-content-md-center",
            label(cls := "col-md-4 col-form-label text-right", `for` := "text-input-1",
              "Text input with required value:"),
            div(cls := "col-md-4",
              rxInputText[Id](id = "text-input-1",
                modifiers = Seq(autocomplete := "off"))
            )
          )
        }
      ),
      div(cls := "col-md-12",
        rxForm() { implicit form =>
          div(cls := "form-group row justify-content-md-center",
            label(cls := "col-md-4 col-form-label text-right", `for` := "text-input-1",
              "Text input with optional value:"),
            div(cls := "col-md-4",
              rxInputText[Option](id = "text-input-1",
                modifiers = Seq(autocomplete := "off"))
            )
          )
        }
      )
    )
  }
}
