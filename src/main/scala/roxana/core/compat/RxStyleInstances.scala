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
package roxana.core.compat

import org.scalajs.dom.Element
import rx.{Ctx, Rx, Var}
import scalatags.JsDom.all.{PixelStyleValue, StyleValue}
import scalatags.generic.Style

trait RxStyleInstances {


  implicit def varIsRxForStyleValue[T](implicit x: StyleValue[Rx[T]],
                                       ctx: Ctx.Owner): StyleValue[Var[T]] =
    (t: Element, s: Style, v: Var[T]) => x.apply(t, s, v)

  implicit def varIsRxForPixelStyleValue[T](implicit x: PixelStyleValue[Rx[T]],
                                            ctx: Ctx.Owner): PixelStyleValue[Var[T]] =
    (s: Style, v: Var[T]) => x.apply(s, v)

  implicit def rxDynIsRxForStyleValue[T](implicit x: StyleValue[Rx[T]],
                                         ctx: Ctx.Owner): StyleValue[Rx.Dynamic[T]] =
    (t: Element, s: Style, v: Rx.Dynamic[T]) => x.apply(t, s, v)

  implicit def rxDynIsRxForPixelStyleValue[T](implicit x: PixelStyleValue[Rx[T]],
                                              ctx: Ctx.Owner): PixelStyleValue[Rx.Dynamic[T]] =
    (s: Style, v: Rx.Dynamic[T]) => x.apply(s, v)

  implicit def rxStyleValue[T: StyleValue](implicit ctx: Ctx.Owner): StyleValue[Rx[T]] =
    new RxStyleValue[T]

  class RxStyleValue[T](implicit sv: StyleValue[T], ctx: Ctx.Owner) extends StyleValue[Rx[T]] {
    override def apply(t: Element, s: Style, rv: Rx[T]): Unit = {
      rv foreach { v => sv.apply(t, s, v) }
    }
  }

}
