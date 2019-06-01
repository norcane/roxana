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

package roxana.core

import org.scalajs.dom.Element
import rx._
import scalatags.JsDom.all.{Attr, AttrValue}

/**
  * Set of implicit conversions that provides support for using ''Scala.rx'' variables for
  * ''Scalatags'' attributes.
  */
trait RxAttrInstances {

  implicit def rxAttrValue[T: AttrValue](implicit ctx: Ctx.Owner): AttrValue[Rx[T]] =
    new RxAttrValue[T, Rx[T]]

  implicit def rxAttrValueDyn[T: AttrValue](implicit ctx: Ctx.Owner): AttrValue[Rx.Dynamic[T]] =
    new RxAttrValue[T, Rx.Dynamic[T]]

  implicit def varAttrValue[T: AttrValue](implicit ctx: Ctx.Owner): AttrValue[Var[T]] =
    new RxAttrValue[T, Var[T]]

  class RxAttrValue[T, F <: Rx[T]](implicit av: AttrValue[T], ctx: Ctx.Owner) extends AttrValue[F] {
    override def apply(t: Element, a: Attr, rv: F): Unit = rv foreach { v => av.apply(t, a, v) }
  }

}
