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

package roxana.toolkit.forms

import org.scalajs.dom
import roxana.core.Component
import rx.Ctx
import scalatags.JsDom
import scalatags.JsDom.{Modifier, TypedTag}

case class rxLink(content: Modifier,
                  onClick: dom.Event => Unit,
                  cls: String = "",
                  modifiers: Seq[Modifier] = Seq.empty)
                 (implicit rxCtx: Ctx.Owner)
  extends Component[dom.html.Anchor] {

  override protected def buildTag: JsDom.TypedTag[dom.html.Anchor] = {
    import scalatags.JsDom.all._
    import scalatags.JsDom.attrs


    val _modifiers = modifiers ++ buildParams(
      addIf(attrs.cls, this.cls)
    )

    a(href := "#", this.content)(_modifiers: _*)
  }

  override protected def buildElem(tag: TypedTag[dom.html.Anchor]): dom.html.Anchor = {
    val elem = super.buildElem(tag)

    elem.onclick = (ev: dom.Event) => {
      ev.preventDefault()
      onClick(ev)
    }

    elem
  }
}
