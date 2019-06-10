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
import org.scalajs.dom.html
import org.scalajs.dom.html.Button
import roxana.core.{Component, RoxanaContext}
import scalatags.JsDom
import scalatags.JsDom.{Modifier, TypedTag}

case class rxButton(label: String,
                    cls: String = "",
                    id: String = "",
                    tpe: String = "button",
                    disabled: Boolean = false,
                    onClick: dom.Event => Unit = _ => (),
                    modifiers: Seq[Modifier] = Seq.empty)
                   (implicit rxCtx: RoxanaContext)
  extends Component[dom.html.Button] {

  override protected def buildTag: JsDom.TypedTag[Button] = {
    import scalatags.JsDom.all._
    import scalatags.JsDom.attrs

    val _modifiers = buildParams(
      Some(this.label),
      addIf(attrs.cls, this.cls),
      addIf(attrs.disabled, this.disabled),
      addIf(attrs.id, this.id),
      addIf(attrs.tpe, this.tpe)
    )

    button(modifiers ++ _modifiers: _*)
  }

  override protected def buildElem(tag: TypedTag[html.Button]): html.Button = {
    val element = tag.render

    // register the custom 'onClick' listener
    element.onclick = onClick

    element
  }
}
