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
import roxana.core.validation.{InputConverter, InputType, ValidationError, validators}
import roxana.core.{Component, RoxanaContext, validation}
import rx.{Rx, Var}
import scalatags.JsDom.{Modifier, TypedTag}

abstract class rxInputBase[Out: InputConverter, +M[_] : InputType]
(cls: String = "",
 id: String = "",
 placeholder: String = "",
 tpe: String = "text",
 validator: validation.Validator[Out],
 modifiers: Seq[Modifier] = Seq.empty)
(implicit rxCtx: RoxanaContext, form: rxForm)
  extends Component[dom.html.Input] with FormInput {

  import rxCtx._

  // -- public API
  val rawValue: Var[String] = Var("")
  val errors: Rx[Seq[ValidationError]] = Rx(InputType[M].errors(rawValue(), InputConverter[Out], validator))
  override val valid: Rx[Boolean] = Rx(errors().isEmpty)

  // register the input component to the parent rxForm
  form.register(this)

  override protected def buildTag: TypedTag[dom.html.Input] = {
    import scalatags.JsDom.all._
    import scalatags.JsDom.attrs

    val _modifiers = buildParams(
      addIf(attrs.cls, this.cls),
      addIf(attrs.id, this.id),
      addIf(attrs.placeholder, this.placeholder)
    )

    input(modifiers ++ _modifiers)
  }

  override protected def buildElem(tag: TypedTag[dom.html.Input]): dom.html.Input = {
    val element = super.buildElem(tag)

    element.onkeyup = (_: dom.KeyboardEvent) => {
      // register listeners for capturing value changes
      this.rawValue() = element.value
      element.focus()
    }

    element
  }

  override def reset(): Unit = {
    rawValue() = ""
    this.elem.value = ""
  }

  override def focus(): Unit = {
    dom.window.setTimeout(() => this.elem.focus(), 100)
  }
}

case class rxInputText[+M[_] : InputType](cls: String = "",
                                          id: String = "",
                                          placeholder: String = "",
                                          tpe: String = "text",
                                          validator: validation.Validator[String] = validators.nonEmpty,
                                          modifiers: Seq[Modifier] = Seq.empty)
                                         (implicit rxCtx: RoxanaContext, form: rxForm)
  extends rxInputBase[String, M](cls, id, placeholder, tpe, validator, modifiers)
