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

package roxana.examples.renderers

import org.scalajs.dom
import org.scalajs.dom.html.Select
import roxana.core.renderers.Renderer
import roxana.toolkit.forms.{rxButton, rxInputText, rxSelectMenu}
import rx.{Ctx, Rx}

/**
  * ''Bootstrap 4'' renderers for the ''roxana'' toolkit components.
  */
object BootstrapV4Renderers {

  def all(implicit rxCtx: Ctx.Owner): Renderer = {
    case c: rxButton => rxButtonRenderer(c)
    case c: rxInputText[_] => rxInputTextRenderer(c)
    case c: rxSelectMenu[_] => rxSelectMenuRenderer(c)
  }

  def rxButtonRenderer(component: rxButton)(implicit rxCtx: Ctx.Owner): Rx[dom.Element] = {
    val elem = component.elem

    elem.classList.add("btn")
    Rx(elem)
  }

  def rxInputTextRenderer[Out, M[_]](component: rxInputText[M])
                               (implicit rxCtx: Ctx.Owner): Rx[dom.Element] = {

    import scalatags.JsDom.all._

    val Valid = "is-valid"
    val Invalid = "is-invalid"
    val elem = component.elem
    val validCls = for (valid <- component.valid) yield if (valid) Valid else Invalid

    Rx {
      Seq(Valid, Invalid) foreach elem.classList.remove
      elem.classList.add("form-control")
      elem.classList.add(validCls())

      div(
        elem,
        // TODO do proper error message localization
        div(cls := "invalid-feedback", component.errors().map(_.message).mkString(","))
      ).render
    }
  }

  def rxSelectMenuRenderer[T](component: rxSelectMenu[T])
                             (implicit rxCtx: Ctx.Owner): Rx.Dynamic[Select] = {
    val elem = component.elem

    elem.classList.add("custom-select")
    Rx(elem)
  }

}
