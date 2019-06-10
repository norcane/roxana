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
package roxana.toolkit.forms

import org.scalajs.dom
import org.scalajs.dom.html.Input
import roxana.core.{Component, RoxanaContext}
import rx.Var
import scalatags.JsDom

case class rxCheck(label: String = "",
                   checked: Boolean = false,
                   onChange: dom.Event => Unit = _ => ())
                  (implicit rxCtx: RoxanaContext) extends Component[dom.html.Input] {

  val value: Var[Boolean] = Var(checked)

  override protected def buildTag: JsDom.TypedTag[Input] = {
    import scalatags.JsDom.all._
    import scalatags.JsDom.attrs

    val checkedAttr = if (this.checked) List(attrs.checked) else List.empty
    input(`type` := "checkbox")(checkedAttr: _*)
  }

  override protected def buildElem(tag: JsDom.TypedTag[Input]): Input = {
    val elem = super.buildElem(tag)

    elem.onchange = (ev: dom.Event) => {
      this.value() = !this.value.now
      onChange(ev)
    }

    elem
  }
}
