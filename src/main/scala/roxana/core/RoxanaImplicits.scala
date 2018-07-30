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

import java.util.Objects

import org.scalajs.dom
import org.scalajs.dom.Element
import roxana.core.renderers.{Renderer, Renderers}
import rx.{Ctx, Rx}
import scalatags.JsDom.Frag
import scalatags.JsDom.all.Modifier

trait RoxanaImplicits {

  implicit class bindComp[T <: Component[_ <: dom.Element]](c: T)
                                                           (implicit rxCtx: Ctx.Owner,
                                                            renderer: Renderer = Renderers.default)
    extends Modifier with RxNodeInstances {


    override def applyTo(t: Element): Unit = {
      RxElementBinding(renderer(c)).applyTo(t)
    }
  }

  implicit class bindRxComp[T <: Component[_ <: dom.Element]](rx: Rx[T])
                                                             (implicit rxCtx: Ctx.Owner,
                                                              renderer: Renderer = Renderers.default)
    extends Modifier with RxNodeInstances {

    private def rendered: Rx[dom.Element] =
      rx.flatMap(renderer)

    override def applyTo(t: Element): Unit = RxElementBinding(rendered).applyTo(t)
  }

  implicit class IterableFrag[A](xs: Iterable[A])(implicit ev: A => Frag) extends Frag {
    Objects.requireNonNull(xs)

    def applyTo(t: dom.Element): Unit = xs.foreach(_.applyTo(t))

    def render: dom.Node = {
      val frag = org.scalajs.dom.document.createDocumentFragment()
      xs.map(_.render).foreach(frag.appendChild)
      frag
    }
  }

  // FIXME rework this to use the renderer
  /*implicit*/ class IterableComponent[T <: dom.Element](cs: Iterable[Component[T]])
                                                        (implicit rxCtx: Ctx.Owner) extends Frag {
    override def render: dom.Node = {
      val frag = org.scalajs.dom.document.createDocumentFragment()
      cs.map(_.elem).foreach(frag.appendChild)
      frag
    }

    override def applyTo(t: Element): Unit = cs.foreach(_.applyTo(t))
  }

  implicit class BindableComponent[T <: Component[_]](component: T) {
    def bindTo(binding: CompBinding[T]): T = {
      if (binding != null) binding.component() = Some(component)
      component
    }
  }

}
