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
import rx._

import scala.reflect.ClassTag

trait RoxanaHelpers {

  def renderComponent[T <: dom.Element](component: Component[T], elementId: String)
                                       (implicit rxCtx: RoxanaContext): Unit = {
    import rxCtx.renderer

    val container = dom.document.getElementById(elementId)
    require(container != null, s"Cannot find element for ID '$elementId'")

    container.innerHTML = ""
    container.appendChild(renderer(component).now)
  }


  def binding[T <: Component[_]](implicit name: sourcecode.Name): CompBinding[T] =
    CompBinding(name.value, Var(None))

  def bound[T <: Component[_]](binding: CompBinding[T])
                              (implicit t: ClassTag[T]): T = {

    binding.component.now.getOrElse(throw new IllegalStateException(
      s"""
         |No component of type '$t' was bound to binding field '${binding.name}'. This is probably
         |caused by missing 'bindTo = ...' argument on the component you want to bind to this field.
      """.stripMargin.replaceAll("\n", " ")
    ))
  }
}

case class CompBinding[T <: Component[_]](name: String, component: Var[Option[T]])

