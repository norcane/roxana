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

package roxana.routing

import scala.annotation.tailrec

trait StaticRoutes {

  private val _routes = collection.mutable.ListBuffer.empty[Route[_]]

  private def splitPath(path: String): List[String] = path.split("/").dropWhile(_.isEmpty).toList

  private def addRoute[R <: Route[_]](route: R) = {
    _routes += route
    route
  }

  sealed trait PathElem

  case class Static(name: String) extends PathElem {
    override def toString: String = name
  }

  case object * extends PathElem

  case object ** extends PathElem

  sealed trait RouteDef[Self] {
    def self: Self

    def elems: List[PathElem]
  }

  implicit def stringToRouteDef0(name: String): RouteDef0 = RouteDef0(Static(name) :: Nil)

  implicit def asteriskToRoutePath1(ast: *.type): RouteDef1 = RouteDef1(ast :: Nil)

  implicit def stringToStatic(name: String): Static = Static(name)


  case class RouteDef0(elems: List[PathElem]) extends RouteDef[RouteDef0] {
    def /(static: Static) = RouteDef0(elems :+ static)

    def /(p: PathElem) = RouteDef1(elems :+ p)

    def self = RouteDef0(elems)

    def to(f0: ⇒ Unit): Route0 = addRoute(Route0(this, () => f0))
  }

  case class RouteDef1(elems: List[PathElem]) extends RouteDef[RouteDef1] {
    def /(static: Static) = RouteDef1(elems :+ static)

    def /(p: PathElem) = RouteDef2(elems :+ p)

    def self = RouteDef1(elems)

    def to[A: PathParam](f1: A ⇒ Unit): Route1[A] = addRoute(Route1(this, f1))
  }

  case class RouteDef2(elems: List[PathElem]) extends RouteDef[RouteDef2] {
    def /(static: Static) = RouteDef2(elems :+ static)

    def self = RouteDef2(elems)

    def to[A: PathParam, B: PathParam](f2: (A, B) ⇒ Unit): Route2[A, B] = addRoute(Route2(this, f2))
  }

  sealed trait Route[RD] {
    def routeDef: RouteDef[RD]

    def unapply(path: String): Option[() => Unit]
  }

  case class Route0(routeDef: RouteDef0, f0: () ⇒ Unit) extends Route[RouteDef0] {
    def apply(): String = PathMatcher0(routeDef.elems)()

    override def unapply(path: String): Option[() => Unit] =
      PathMatcher0.unapply(routeDef.elems, splitPath(path), f0)
  }

  case class Route1[A: PathParam](routeDef: RouteDef1, f1: A ⇒ Unit) extends Route[RouteDef1] {
    def apply(a: A): String = PathMatcher1(routeDef.elems)(a)

    override def unapply(path: String): Option[() => Unit] =
      PathMatcher1.unapply(routeDef.elems, splitPath(path), f1)
  }

  case class Route2[A: PathParam, B: PathParam](routeDef: RouteDef2, f2: (A, B) ⇒ Unit) extends Route[RouteDef2] {
    def apply(a: A, b: B): String = PathMatcher2(routeDef.elems)(a, b)

    override def unapply(path: String): Option[() => Unit] =
      PathMatcher2.unapply(routeDef.elems, splitPath(path), f2)
  }

  trait PathParam[T] {
    def apply(t: T): String

    def unapply(s: String): Option[T]
  }

  implicit val StringPathParam: PathParam[String] = new PathParam[String] {
    def apply(s: String): String = s

    def unapply(s: String): Option[String] = Some(s)
  }

  implicit val BooleanPathParam: PathParam[Boolean] = new PathParam[Boolean] {
    def apply(b: Boolean): String = b.toString

    def unapply(s: String): Option[Boolean] = s.toLowerCase match {
      case "1" | "true" | "yes" ⇒ Some(true)
      case "0" | "false" | "no" ⇒ Some(false)
      case _ ⇒ None
    }
  }

  object PathMatcher0 {
    def apply(elems: List[PathElem])(): String = elems.mkString("/", "/", "")

    def unapply(elems: List[PathElem], parts: List[String], handler: () => Unit): Option[() => Unit] =
      (elems, parts) match {
        case (Nil, Nil) => Some(handler)
        case (Static(x) :: xs, y :: ys) if x == y => unapply(xs, ys, handler)
        case _ => None
      }
  }

  object PathMatcher1 {
    def apply[A](elems: List[PathElem], prefix: List[PathElem] = Nil)(a: A)
                (implicit ppa: PathParam[A]): String = elems match {
      case Static(x) :: rest => apply(rest, prefix :+ Static(x))(a)
      case (* | **) :: rest => PathMatcher0(prefix ::: Static(ppa(a)) :: rest)()
      case _ => PathMatcher0(elems)()
    }

    @tailrec
    def unapply[A](elems: List[PathElem], parts: List[String], handler: A => Unit)
                  (implicit ppa: PathParam[A]): Option[() => Unit] = (elems, parts) match {
      case (Static(x) :: xs, y :: ys) if x == y => unapply(xs, ys, handler)
      case (* :: xs, ppa(a) :: ys) => PathMatcher0.unapply(xs, ys, () => handler(a))
      case (** :: xs, ys) => ppa.unapply(ys.mkString("/")).map { a => () => handler(a) }
      case _ => None
    }
  }

  object PathMatcher2 {
    def apply[A, B](elems: List[PathElem], prefix: List[PathElem] = Nil)(a: A, b: B)
                   (implicit ppa: PathParam[A], ppb: PathParam[B]): String = elems match {
      case Static(x) :: rest => apply(rest, prefix :+ Static(x))(a, b)
      case (* | **) :: rest => PathMatcher1(prefix ::: Static(ppa(a)) :: rest)(b)
      case _ => PathMatcher0(elems)()
    }

    @tailrec
    def unapply[A, B](elems: List[PathElem], parts: List[String], handler: (A, B) => Unit)
                     (implicit ppa: PathParam[A], ppb: PathParam[B]): Option[() => Unit] =
      (elems, parts) match {
        case (Static(x) :: xs, y :: ys) if x == y => unapply(xs, ys, handler)
        case (* :: xs, ppa(a) :: ys) => PathMatcher1.unapply(xs, ys, (b: B) => handler(a, b))
        case _ => None
      }
  }

  // PROTECTED API

  protected val root = RouteDef0(Nil)

  protected def route[R](routeDef: RouteDef[R]): R = routeDef.self

  // PUBLIC API

  def routes: Seq[Route[_]] = _routes

  def actionForPath(path: String): Option[() => Unit] = routes.flatMap(_.unapply(path)).headOption
}

