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

package roxana.core

import org.scalajs.dom
import org.scalajs.dom.Element
import rx.{Ctx, Rx}
import scalatags.JsDom.all.Modifier

trait RoxanaImplicits {

  implicit class bindComp[T <: Component[_ <: dom.Element]](c: T)
                                                           (implicit rxCtx: Ctx.Owner,
                                                            renderer: Renderer[T] = null)
    extends Modifier with RxNodeInstances {


    override def applyTo(t: Element): Unit = {
      if (renderer != null) RxElementBinding(renderer.render(c)).applyTo(t) else t.appendChild(c.elem)
    }
  }

  implicit class bindRxComp[T <: Component[_ <: dom.Element]](rx: Rx[T])
                                                             (implicit rxCtx: Ctx.Owner,
                                                              renderer: Renderer[T] = null)
    extends Modifier with RxNodeInstances {

    private def rendered: Rx[dom.Element] =
      if (renderer != null) rx.flatMap(renderer.render) else rx.map(_.elem)

    override def applyTo(t: Element): Unit = RxElementBinding(rendered).applyTo(t)
  }

  implicit class BindableComponent[T <: Component[_]](component: T) {
    def bindTo(binding: CompBinding[T]): T = {
      if (binding != null) binding.component() = Some(component)
      component
    }
  }

}
