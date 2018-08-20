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

package roxana.core.l10n

import org.scalajs.dom

trait LocaleSupport {

  /**
    * Returns the localized message for the given message key and (optional) list of arguments.
    *
    * @param key      localized message key
    * @param args     arguments
    * @param messages collection of localized messages
    * @return localized message
    */
  protected def loc(key: String, args: String*)(implicit messages: Messages): String = {
    messages.messages.get(key) match {
      case Some(message) =>
        formatMessage(key, message, args)
      case None =>
        dom.console.warn(s"Missing localized message for key '$key'")
        key
    }
  }

  private def formatMessage(key: String, message: String, args: Seq[String])
                           (implicit messages: Messages): String = {
    val formatted = args.zipWithIndex
      .foldLeft(message) { case (last, (arg, idx)) => last.replace(s"{$idx}", arg) }

    if (message.count(_ == '{') > args.length) {
      dom.console.warn(s"Possible forgotten formatting placeholder for message $key=$formatted")
    }

    formatted
  }

}
