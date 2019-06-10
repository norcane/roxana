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
package roxana.examples.renderers

import org.scalajs.dom
import org.scalajs.dom.html.Select
import roxana.core.l10n.{LocaleSupport, Messages}
import roxana.core.renderers.Renderer
import roxana.toolkit.forms.{rxButton, rxCheck, rxInputText, rxSelectMenu}
import rx.{Ctx, Rx}

/**
  * ''Bootstrap 4'' renderers for the ''roxana'' toolkit components.
  */
object BootstrapV4Renderers extends LocaleSupport {

  def all(implicit rxCtx: Ctx.Owner, messages: Messages): Renderer = {
    case c: rxButton => rxButtonRenderer(c)
    case c: rxCheck => rxCheckRenderer(c)
    case c: rxInputText[_] => rxInputTextRenderer(c)
    case c: rxSelectMenu[_] => rxSelectMenuRenderer(c)
  }

  def rxButtonRenderer(component: rxButton)(implicit rxCtx: Ctx.Owner): Rx[dom.Element] = {
    val elem = component.elem

    elem.classList.add("btn")
    Rx(elem)
  }

  def rxCheckRenderer(component: rxCheck)(implicit rxCtx: Ctx.Owner): Rx.Dynamic[dom.Element] = {
    import scalatags.JsDom.all._

    def hash(): Long = System.nanoTime()

    val elem = component.elem

    Rx {
      val text = if (component.value()) del(component.label) else div(component.label)
      val elemId = if (elem.id == null || elem.id.isEmpty) s"check-${hash()}" else elem.id
      elem.id = elemId
      elem.classList.add("custom-control-input")

      div(cls := "custom-control custom-checkbox",
        elem,
        label(cls := "custom-control-label", `for` := elemId, text)
      ).render
    }
  }

  def rxInputTextRenderer[Out, M[_]](component: rxInputText[M])
                                    (implicit rxCtx: Ctx.Owner, messages: Messages): Rx[dom.Element] = {

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
        div(
          cls := "invalid-feedback",
          component.errors().map(err => loc(err.message, err.args: _*)).mkString(","))
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
