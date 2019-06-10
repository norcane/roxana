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

package roxana.core.validation

import cats.{FlatMap, Functor}
import roxana.toolkit.forms.Required
import simulacrum.typeclass


@typeclass trait InputType[M[_]] {
  def pure(input: String): M[String]

  def validated[Out](input: String,
                     converter: InputConverter[Out],
                     validator: Validator[Out]): M[Result[Out]]

  def errors[Out](input: String,
                  converter: InputConverter[Out],
                  validator: Validator[Out]): Seq[ValidationError]
}

object InputType {

  implicit def idInputType: InputType[Required] = new InputType[Required] {
    override def pure(input: String): Required[String] = input

    override def validated[Out](input: String,
                                converter: InputConverter[Out],
                                validator: Validator[Out]): Result[Out] =
      Functor[Required].map(pure(input))(in => converter.convert(in).andThen(validator))

    override def errors[Out](input: String,
                             converter: InputConverter[Out],
                             validator: Validator[Out]): Seq[ValidationError] =
      validated(input, converter, validator).swap.toList
  }

  implicit def optionInputType: InputType[Option] = new InputType[Option] {

    import cats.implicits._

    override def pure(input: String): Option[String] =
      if (input != null && input.nonEmpty) Some(input) else None

    override def validated[Out](input: String,
                                converter: InputConverter[Out],
                                validator: Validator[Out]): Option[Result[Out]] =
      Functor[Option].map(pure(input))(in => converter.convert(in).andThen(validator))


    override def errors[Out](input: String,
                             converter: InputConverter[Out],
                             validator: Validator[Out]): Seq[ValidationError] =
      FlatMap[Option].flatMap(pure(input))(in =>
        converter.convert(in).andThen(validator).swap.toOption).toSeq

  }
}
