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

import java.util.concurrent.atomic.AtomicReference

import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.dom.ext._
import org.scalajs.dom.raw.{Comment, Node}
import rx._
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all.{Frag, Modifier}

import scala.collection.immutable
import scala.collection.immutable.Iterable

/**
  * Set of implicit conversions that provides support for using ''Scala.rx'' variables as regular
  * ''Scalatags'' nodes.
  */
trait RxNodeInstances {

  implicit class RxString2Frag(v: Rx[String])(implicit ctx: Ctx.Owner) extends Frag {
    def render: dom.Text = {
      val node = dom.document.createTextNode(v.now)
      v foreach { s => node.replaceData(0, node.length, s) }
      node
    }

    def applyTo(t: Element): Unit = t.appendChild(render)
  }

  implicit class RxTypedTagBinding[T <: dom.Element](rx: Rx[TypedTag[T]])
                                                    (implicit rxCtx: RoxanaContext)
    extends Modifier {

    import rxCtx._

    def applyTo(container: Element): Unit = {
      val atomicReference = new AtomicReference(rx.map(_.render).now)
      container.appendChild(atomicReference.get())
      rx.triggerLater {
        val current = rx.map(_.render).now
        val previous = atomicReference.getAndSet(current)
        container.replaceChild(current, previous)
        ()
      }
    }
  }

  implicit class RxElementBinding[T <: dom.Element](rx: Rx[T])
                                                   (implicit rxCtx: RoxanaContext) extends Modifier {

    import rxCtx._

    def applyTo(container: Element): Unit = {
      val atomicReference = new AtomicReference(rx.now)
      container.appendChild(atomicReference.get())
      rx.triggerLater {
        val current = rx.now
        val previous = atomicReference.getAndSet(current)
        container.replaceChild(current, previous)
        ()
      }
    }
  }

  implicit class RxElementsBinding(e: Rx[immutable.Iterable[Element]])
                                  (implicit ctx: Ctx.Owner) extends Modifier {
    def applyTo(t: Element): Unit = {
      val nonEmpty: Rx[Iterable[Node]] = e.map { t => if (t.isEmpty) List(new Comment) else t }
      val fragments = new AtomicReference(nonEmpty.now)
      nonEmpty.now foreach t.appendChild
      nonEmpty.triggerLater {
        val current: Iterable[Node] = nonEmpty.now
        val previous = fragments getAndSet current
        val i = t.childNodes.indexOf(previous.head)
        if (i < 0) throw new IllegalStateException("illegal state - children changed")
        0 until previous.size foreach (_ => t.removeChild(t.childNodes.item(i)))
        if (t.childNodes.length > i) {
          val next = t.childNodes.item(i)
          current foreach (t.insertBefore(_, next))
        } else {
          current foreach t.appendChild
        }
      }
    }
  }

}
