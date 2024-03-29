/*
 * Copyright 2011 55 Minutes (http://www.55minutes.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//= require <modernizr>

/*------------------------------------------------------------------------------
| modernizr-extras.js
| 55 Minutes JS utilities v2.5-pre
| Author(s): Richa Avasthi
| Created: 2010-10-13
|
| Extra tests for Modernizr.
------------------------------------------------------------------------------*/

/*
** Test whether the browser supports the "placeholder" attribute on textarea
** elements.
*/
Modernizr.addTest("textareaplaceholder", function() {
    var t = document.createElement("textarea");
    return "placeholder" in t;
});