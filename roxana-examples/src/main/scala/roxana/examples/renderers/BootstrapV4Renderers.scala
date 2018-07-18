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

import roxana.core.Renderer
import roxana.toolkit.forms.{rxButton, rxInputText}
import rx.{Ctx, Rx}

trait BootstrapV4Renderers {

  implicit def rxButtonRenderer(implicit rxCtx: Ctx.Owner): Renderer[rxButton] =
    (component: rxButton) => {
      val elem = component.elem

      elem.classList.add("btn")
      Rx(elem)
    }

  implicit def rxInputTextRenderer[M[_]](implicit rxCtx: Ctx.Owner): Renderer[rxInputText[M]] =
    (component: rxInputText[M]) => {
      val Valid = "is-valid"
      val Invalid = "is-invalid"
      val elem = component.elem
      val validCls = for (valid <- component.valid) yield if (valid) Valid else Invalid

      Rx {
        Seq(Valid, Invalid) foreach elem.classList.remove
        elem.classList.add("form-control")
        elem.classList.add(validCls())
        elem
      }
    }

}
