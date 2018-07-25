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
import roxana.core.{Component, Resettable, Validable}
import rx._
import scalatags.JsDom
import scalatags.JsDom.Modifier


case class rxForm(cls: String = "",
                  onSubmit: rxForm => Unit = _ => (),
                  modifiers: Seq[Modifier] = Seq.empty)
                 (formContent: rxForm => Modifier)
                 (implicit rxCtx: Ctx.Owner)
  extends Component[dom.html.Form] with Resettable {

  // --- public API
  val inputs: Var[Seq[FormInput]] = Var(Seq.empty)
  val valid: Rx[Boolean] = inputs map (_ forall (_.valid()))

  override protected def buildTag: JsDom.TypedTag[dom.html.Form] = {
    import scalatags.JsDom.all._
    import scalatags.JsDom.attrs

    val _modifiers = buildParams(
      addIf(attrs.cls, this.cls)
    )

    form(formContent(this), modifiers ++ _modifiers)
  }

  override protected def buildElem(tag: JsDom.TypedTag[dom.html.Form]): dom.html.Form = {
    val elem = super.buildElem(tag)

    // register the 'onSubmit' handler to the form submit event
    elem.onsubmit = (ev: dom.Event) => {
      ev.preventDefault() // prevent the actual form submitting
      runOnSubmitIfValid()
      false
    }

    elem
  }

  override def reset(): Unit = inputs.now foreach (_.reset())

  def submit(): Unit = runOnSubmitIfValid()

  private[forms] def register(input: FormInput): Unit = inputs() = inputs.now :+ input

  private def runOnSubmitIfValid(): Unit = if (valid.now) onSubmit(this)
}

object rxForm {

  import scalatags.JsDom.all._

  def empty(implicit rxCtx: Ctx.Owner): rxForm = rxForm()(_ => div())
}

trait FormInput extends Validable with Resettable

