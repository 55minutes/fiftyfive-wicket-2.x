/**
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

package fiftyfive.wicket.css;

import fiftyfive.wicket.BaseWicketTest;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.parser.XmlTag;
import org.junit.Test;


public class IterationCssBehaviorTest extends BaseWicketTest
{
    @Test
    public void testRender() throws Exception
    {
        _tester.startPage(IterationCssBehaviorTestPage.class);
        _tester.assertRenderedPage(IterationCssBehaviorTestPage.class);
        _tester.assertResultPage(
            IterationCssBehaviorTestPage.class,
            "IterationCssBehaviorTestPage-expected.html"
        );
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void test_badComponent() throws Exception
    {
        Label label = new Label("hello", "world");
        IterationCssBehavior behavior = new IterationCssBehavior("first");
        behavior.onComponentTag(label, new ComponentTag("span", XmlTag.OPEN));
    }
}
