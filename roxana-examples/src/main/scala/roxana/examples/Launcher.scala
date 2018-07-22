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

package roxana.examples

import org.scalajs.dom
import roxana.core.Component
import roxana.core.renderers.Renderer
import roxana.examples.containers.todoList
import roxana.toolkit.forms.{rxButton, rxSelectMenu}
import rx._

object Launcher {

  import roxana.core.helpers._

  private implicit val rxCtx: Ctx.Owner = Ctx.Owner.safe()

  implicit val renderer: Renderer =  roxana.examples.renderers.bootstrap4

  def main(args: Array[String]): Unit = {
    println("roxana demo starting...")

    val components: Seq[rxSelectMenu.Item[() => Component[_ <: dom.Element]]] = Seq(
      rxSelectMenu.Item("todoList", () => todoList()),
      rxSelectMenu.Item("button", () => rxButton(label = "test"))
    )

    val selectedComponent = Var(components.head)

    val componentsSelect = rxSelectMenu[() => Component[_ <: dom.Element]](
      components, onChange = item => selectedComponent() = item)

    println("rendering component select menu")
    renderComponent(componentsSelect, 'selectContainer)

    selectedComponent.trigger {
      renderComponent(selectedComponent.now.value(), 'componentContainer)
    }
  }

}
