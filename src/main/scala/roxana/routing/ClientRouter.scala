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

import org.scalajs.dom
import org.scalajs.dom.window

/**
  * The ''client router'' represents a robust, type-safe solution for client-side application
  * routing. The ''router'' is responsible for proper handling of URL changes and routing to the
  * given path. The ''routes'' describes the possible paths actions for them.
  *
  * The implementation of ''client router'' must ensure that all routes must be bookmarkable, i.e.
  * when the user routes to the selected path, this step  must be persisted in the browser's history
  * and reflected in browser URL.
  */
trait ClientRouter {

  /**
    * Routes recognized by this router.
    */
  val routes: StaticRoutes

  /**
    * Performs the router action for the currently valid path.
    */
  def doCurrentAction(): Unit

  /**
    * Registers the router for listening proper events. This method is responsible to handle the
    * change of the path, based on which the proper router action should be performed.
    */
  def bindToEvents(): Unit

  /**
    * Routes to the given path. The registered router event for this path will be performed.
    *
    * @param path path to route to
    */
  def routeTo(path: String): Unit

  /**
    * Initializes the router. This is the method that should be called from the client code, during
    * the application start.
    */
  def init(): Unit = {
    bindToEvents()
    doCurrentAction()
  }
}

/**
  * This implementation of [[ClientRouter]] uses the ''hashbang'' part of the URL for the routing
  * purposes. This router should only be used if you cannot use the [[HistoryRouter]], that uses the
  * modern browser's ''History API''. This can happen if you need to support legacy browsers, or
  * your web application doesn't run on server.
  *
  * == Examples: ==
  *
  * {{{
  *   path: /           ->  index.html#/          (works also without the '#/' part)
  *   path: /hello      ->  index.html#/hello
  *   path: /user/323   ->  index.html#/user/323
  * }}}
  */
trait HashBangRouter extends ClientRouter {

  private val prefix = "#!"

  override def doCurrentAction(): Unit =
    routes.actionForPath(window.location.hash.substring(prefix.length)) foreach (_.apply())

  override def bindToEvents(): Unit = {
    window.addEventListener("hashchange", (_: dom.Event) => {
      doCurrentAction()
    })
  }

  override def routeTo(path: String): Unit = {
    routes.actionForPath(path) foreach { _ =>
      window.location.hash = "!" + path
    }
  }
}

/**
  * This implementation of [[ClientRouter]] uses the browser's ''History API'' to manipulate the
  * URL changes.
  *
  * == Examples: ==
  * {{{
  *   path: /         ->  http://your.site/
  *   path: /hello    ->  http://your.site/hello
  *   path: /user/323 ->  http://your.site/user/323
  * }}}
  */
// FIXME add support for the context path
trait HistoryRouter extends ClientRouter {

  override def doCurrentAction(): Unit =
    routes.actionForPath(window.location.pathname) foreach (_.apply())

  override def bindToEvents(): Unit = {
    window.addEventListener("popstate", (_: dom.Event) => {
      doCurrentAction()
    })
  }

  override def routeTo(path: String): Unit = {
    routes.actionForPath(path) foreach { action =>
      window.history.pushState(null, null, path)
      action()
    }
  }
}