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

package roxana.examples.l10n

import org.scalajs.dom
import org.scalajs.dom.ext
import roxana.core.l10n.Messages

import scala.concurrent.Future

/**
  * Naive implementation of mechanism retrieving localization messages from the server. It takes
  * the user's preferred language from the browser environment and tries to download the JSON
  * file containing the translations for the given language. In order to keep the code simple, many
  * checks that would be needed for the production code are omitted.
  */
object LocalizedMessages {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val contextPath = dom.window.location.pathname.split("/index.html")(0)
  private val DefaultLanguage = "en"

  /**
    * Load translated messages for the given language from the server. If the selected language is
    * not supported, default english translation will be used.
    *
    * @param language language (optional, defaults to english)
    * @return translated messages
    */
  def fetchFromServer(language: String = browserLanguage): Future[Messages] =
    fetch(language).recoverWith { case _: Throwable => fetch(DefaultLanguage) }

  private def browserLanguage: String = dom.window.navigator.language.split("-")(0)

  private def fetch(language: String): Future[Messages] = {
    ext.Ajax.get(contextPath + s"/l10n/messages_$language.json")
      .map(xhr => Messages(ujson.read(xhr.responseText).obj.mapValues(_.str).toMap))
  }

}
