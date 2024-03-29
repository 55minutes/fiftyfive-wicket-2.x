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

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebPage;
import static fiftyfive.wicket.css.MergedCssBuilderTest.*;

public class MergedCssBuilderTestPage extends WebPage
{
    public MergedCssBuilderTestPage()
    {
        super();
        add(CSSPackageResource.getHeaderContribution(CSS_1));
        add(CSSPackageResource.getHeaderContribution(CSS_2));
        add(CSSPackageResource.getHeaderContribution(CSS_PRINT_1, "print"));
        add(CSSPackageResource.getHeaderContribution(CSS_PRINT_2, "print"));
    }
}
