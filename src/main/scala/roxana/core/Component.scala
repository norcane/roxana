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

import org.scalajs.dom
import rx.Rx
import scalatags.JsDom.{Modifier, TypedTag}
import scalatags.generic.{Attr, AttrPair, AttrValue}

trait Component[T <: dom.Element] {

  protected def buildTag: TypedTag[T]

  protected def buildElem(tag: TypedTag[T]): T = tag.render

  final lazy val elem: T = buildElem(buildTag)

  protected def buildParams(params: Option[Modifier]*): Seq[Modifier] = params.flatten

  protected def addIf(attr: Attr, value: => String)
                     (implicit ev: AttrValue[dom.Element, String]): Option[Modifier] = {

    if (value != null && !value.isEmpty) Some(attr.:=[dom.Element, String](value)) else None
  }

  protected def addIf(attr: AttrPair[dom.Element, String], value: => Boolean)
                     (implicit ev: AttrValue[dom.Element, Boolean]): Option[Modifier] = {

    if (value) Some(attr.:=[dom.Element, Boolean](value)) else None
  }

}

trait Validable {
  val valid: Rx[Boolean]
}

trait Resettable {
  def reset(): Unit
}
