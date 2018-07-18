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

package roxana.core.validation.textinput

import cats.Id
import roxana.core.validation
import roxana.core.validation.{ValResult, Validator}

trait TextInputImplicits {

  implicit def idTextInput: TextInput[Id] = new TextInput[Id] {
    override def extract(input: String): Id[String] = input

    override def validate[Out](input: String,
                               validator: Validator[String, Out]): Id[ValResult[Out]] =
      validateIn(extract(input), validator)

    override def errors[Out](input: String,
                             validator: Validator[String, Out]): Seq[validation.ValidationError] =
      super.errorsIn(extract(input), validator)
  }

  implicit def optionTextInput: TextInput[Option] = new TextInput[Option] {

    import cats.implicits._

    override def extract(input: String): Option[String] =
      if (input != null && input.nonEmpty) Some(input) else None

    override def validate[Out](input: String,
                               validator: Validator[String, Out]): Option[ValResult[Out]] =
      validateIn(extract(input), validator)

    override def errors[Out](input: String,
                             validator: Validator[String, Out]): Seq[validation.ValidationError] =
      errorsIn(extract(input), validator)
  }

}
