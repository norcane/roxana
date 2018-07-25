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

import cats.data.Validated
import cats.{Applicative, Functor}
import simulacrum.typeclass

package object validation extends ValidationImplicits {

  object stringValidators extends StringValidators

  case class ValidationError(message: String, args: String*)

  type ValResult[Out] = Validated[ValidationError, Out]

  type Validator[In, Out] = In => ValResult[Out]

  @typeclass trait Sequence[M[_]] {
    def toSeq[A](value: M[A]): Seq[A]
  }

  trait ValidableInput {

    protected def validateIn[In, Out, M[_] : Applicative : Functor](input: M[In],
                                                                    validator: Validator[In, Out]): M[ValResult[Out]] =
      Functor[M].map(input)(validator)

    protected def errorsIn[In, Out, M[_] : Applicative : Functor : Sequence](input: M[In],
                                                                             validator: Validator[In, Out]): Seq[ValidationError] =
      Sequence[M].toSeq(validateIn(input, validator)) flatMap (_.swap.toList)
  }

  trait Validators {
    protected def valid[Out](result: Out): ValResult[Out] = Validated.valid(result)

    protected def invalid[Out](message: String, args: String*): ValResult[Out] =
      Validated.invalid(ValidationError(message, args: _*))
  }

  trait DefaultValidators extends Validators {
    def default[T](input: T): ValResult[T] = valid(input)
  }

}
