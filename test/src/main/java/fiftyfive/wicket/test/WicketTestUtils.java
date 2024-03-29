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
package fiftyfive.wicket.test;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPathExpressionException;

import fiftyfive.util.XPathHelper;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.tester.WicketTester;
import org.apache.wicket.util.tester.WicketTesterHelper;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * Helper functions and assertions for easier testing of Wicket pages
 * and components. Care has been taken to ensure that these helpers
 * automatically work with both XHTML and HTML5 documents. In some cases
 * there are separate methods to handle the two cases.
 * 
 * @see #startComponentWithHtml startComponentWithHtml()
 * @see #startComponentWithXHtml startComponentWithXHtml()
 */
public abstract class WicketTestUtils
{
    // Default to using the implementations shipped with the JRE
    
    private static String _transformerFactoryClassName =
        "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";
    
    private static String _documentBuilderFactoryClassName =
        "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
    
    /**
     * Change the {@link DocumentBuilderFactory} implementation used by the DOM and XPath
     * helper methods of this class. The default JRE-provided class should be sufficient:
     * {@code com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl}.
     */
    public static void setDocumentBuilderFactoryClassName(String className)
    {
        _documentBuilderFactoryClassName = className;
    }
    
    /**
     * Change the {@link TransformerFactory} implementation used by the DOM and XPath
     * helper methods of this class. The default JRE-provided class should be sufficient:
     * {@code com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl}.
     */
    public static void setTransformerFactoryClassName(String className)
    {
        _transformerFactoryClassName = className;
    }
    
    /**
     * Parses the most recently rendered Wicket page into an XML Document
     * object. Uses
     * <a href="http://mercury.ccil.org/~cowan/XML/tagsoup/">TagSoup</a>
     * under the hood to parse even the sloppiest HTML. You can then use
     * things like xpath expressions against the document.
     * 
     * @return The root Node of the resulting DOM
     */
    public static Node markupAsDOM(WicketTester tester)
        throws SAXException, ParserConfigurationException, TransformerException
    {
        // Obtain the raw text of the markup
        String markup = document(tester);
        
        // Configure the SAX2 TagSoup parser
        XMLReader reader = new Parser();
        reader.setFeature(Parser.namespacesFeature, false);
        reader.setFeature(Parser.namespacePrefixesFeature, false);

        // A transformer that will convert SAX2 events to a DOM node.
        TransformerFactory tf = TransformerFactory.newInstance(_transformerFactoryClassName, null);
        Transformer transformer = tf.newTransformer();

        // Construct an empty document into which the transformed elements will be placed
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(
            _documentBuilderFactoryClassName,
            null);
        DOMResult result = new DOMResult(dbf.newDocumentBuilder().newDocument());

        // Peform the transformation
        transformer.transform(
            new SAXSource(reader, new InputSource(new StringReader(markup))), 
            result
        );

        // This will be the root of the resulting DOM
        return result.getNode();
    }
    
    /**
     * Asserts that an XPath expression can be found in the most recently
     * rendered Wicket page. Uses
     * <a href="http://mercury.ccil.org/~cowan/XML/tagsoup/">TagSoup</a>
     * under the hood to parse even the sloppiest HTML into XML that can be
     * queryied by XPath. It is therefore possible that the XPath assertion
     * will pass, even though {@link #assertValidMarkup(WicketTester) assertValidMarkup()}
     * fails.
     */
    public static void assertXPath(WicketTester wt, String expr)
            throws IOException, SAXException, ParserConfigurationException, TransformerException,
                   XPathExpressionException
    {
        if(matchCount(wt, expr) == 0)
        {
            Assert.fail(String.format(
                "XPath expression [%s] could not be found in document:%n%s",
                expr,
                document(wt)
            ));
        }
    }

    /**
     * Asserts that exactly {@code count} number of instances of a given
     * XPath expression can be found in the most recently
     * rendered Wicket page. Uses
     * <a href="http://mercury.ccil.org/~cowan/XML/tagsoup/">TagSoup</a>
     * under the hood to parse even the sloppiest HTML into XML that can be
     * queryied by XPath. It is therefore possible that the XPath assertion
     * will pass, even though {@link #assertValidMarkup(WicketTester) assertValidMarkup()}
     * fails.
     */
    public static void assertXPath(int count, WicketTester wt, String expr)
            throws IOException, SAXException, ParserConfigurationException, TransformerException,
                   XPathExpressionException
    {
        // First make sure the expression exists at all
        if(count > 0)
        {
            assertXPath(wt, expr);
        }
        
        // Then do a more exact check
        final int matches = matchCount(wt, expr);
        if(matches != count)
        {
            String s = 1 == count ? "" : "s";
            Assert.fail(String.format(
                "Expected %d occurance%s of XPath expression [%s], but " +
                "found %d in document:%n%s",
                count,
                s,
                expr,
                matches,
                document(wt)
            ));
        }
    }
    
    /**
     * Assert that the last rendered page has a content-type of text/html
     * and is valid markup. Will autodetect whether the document is HTML5 or
     * XHTML and use the appropriate validator. An HTML5 document must start
     * with {@code <!DOCTYPE html>}, anything else is assumed to be XHTML.
     *
     * @param tester A WicketTester object that has just rendered a page.
     */
    public static void assertValidMarkup(WicketTester tester) 
        throws IOException
    {
        assertValidMarkup(tester, -1);
    }

    /**
     * Assert that the last rendered page has a content-type of text/html
     * and is valid markup. Will autodetect whether the document is HTML5 or
     * XHTML and use the appropriate validator. An HTML5 document must start
     * with {@code <!DOCTYPE html>}, anything else is assumed to be XHTML.
     *
     * @param tester A WicketTester object that has just rendered a page.
     * @param linesContext The number of lines of context to include around
     *                     each validation error.
     */
    public static void assertValidMarkup(WicketTester tester, int linesContext) 
        throws IOException
    {
        String type = tester.getServletResponse().getContentType();
        Assert.assertNotNull(
            "Content type of rendered Wicket page cannot be null", type
        );
        Assert.assertTrue(
            "Content type of rendered Wicket page must be text/html",
            type.equals("text/html") ||
            type.startsWith("text/html;")
        );
        
        String document = document(tester);
        AbstractDocumentValidator validator = validator(document);
        if(linesContext >= 0)
        {
            validator.setNumLinesContext(linesContext);
        }
        
        validator.parse(document);

        if(!validator.isValid())
        {
            Assert.fail(String.format(
                "Invalid HTML:%n%s",
                WicketTesterHelper.asLined(validator.getErrors())
            ));
        }
    }

    /**
     * Renders a component using a snippet of XHTML 1.0 Strict markup. Example:
     * <pre class="example">
     * startComponentWithXHtml(
     *     tester,
     *     new Label("label", "Hello, world!),
     *     "&lt;span wicket:id=\"label\"&gt;replaced by Wicket&lt;/span&gt;"
     * );</pre>
     * <p>
     * This method will place the component in a simple XHTML Page and render
     * it using the normal WicketTester request/response. In the above example,
     * the rendered output will be:
     * <pre class="example">
     * &lt;!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"&gt;
     * &lt;html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"&gt;
     * &lt;head&gt;
     *   &lt;title&gt;untitled&lt;/title&gt;
     * &lt;/head&gt;
     * &lt;body&gt;
     * &lt;span wicket:id=&quot;label&quot;&gt;Hello, world!&lt;/span&gt;
     * &lt;/body&gt;
     * &lt;/html&gt;</pre>
     * <p>
     * You can then use helper method like
     * {@link #assertXPath(WicketTester,String) assertXPath} or
     * {@link WicketTester#assertContains(String)}
     * to verify the component rendered as expected.
     */
    public static void startComponentWithXHtml(WicketTester tester,
                                               Component c,
                                               final String markup)
    {
        startComponentWithXHtml(tester, null, c, markup);
    }

    /**
     * A variation of
     * {@link #startComponentWithXHtml(WicketTester,Component,String) startComponentWithXHtml()}
     * that accepts {@link PageParameters}. These page parameters are passed to
     * the page that wraps the component under test.
     * 
     * @since 2.0.4
     */
    public static void startComponentWithXHtml(WicketTester tester,
                                               PageParameters parameters,
                                               Component c,
                                               final String markup)
    {
        WebPage page = new PageWithInlineMarkup(String.format(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
            "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">%n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\" " +
            "xml:lang=\"en\" lang=\"en\">%n" +
            "<head>%n  <title>untitled</title>%n</head>%n" +
            "<body>%n%s%n</body>%n</html>",
            markup),
            parameters
        );
        page.add(c);
        tester.startPage(page);
    }

    /**
     * Renders a component using a snippet of HTML5 markup. Example:
     * <pre class="example">
     * startComponentWithHtml(
     *     tester,
     *     new Label("label", "Hello, world!),
     *     "&lt;span wicket:id=\"label\"&gt;replaced by Wicket&lt;/span&gt;"
     * );</pre>
     * <p>
     * This method will place the component in a simple HTML5 Page and render
     * it using the normal WicketTester request/response. In the above example,
     * the rendered output will be:
     * <pre class="example">
     * &lt;!DOCTYPE html&gt;
     * &lt;html&gt;
     * &lt;head&gt;
     *   &lt;title&gt;untitled&lt;/title&gt;
     * &lt;/head&gt;
     * &lt;body&gt;
     * &lt;span wicket:id=&quot;label&quot;&gt;Hello, world!&lt;/span&gt;
     * &lt;/body&gt;
     * &lt;/html&gt;</pre>
     * <p>
     * You can then use helper method like
     * {@link #assertXPath(WicketTester,String) assertXPath} or
     * {@link WicketTester#assertContains(String)}
     * to verify the component rendered as expected.
     */
    public static void startComponentWithHtml(WicketTester tester,
                                              Component c,
                                              final String markup)
    {
        startComponentWithHtml(tester, null, c, markup);
    }

    /**
     * A variation of
     * {@link #startComponentWithHtml(WicketTester,Component,String) startComponentWithHtml()}
     * that accepts {@link PageParameters}. These page parameters are passed to
     * the page that wraps the component under test.
     * 
     * @since 2.0.4
     */
    public static void startComponentWithHtml(WicketTester tester,
                                              PageParameters parameters,
                                              Component c,
                                              final String markup)
    {
        WebPage page = new PageWithInlineMarkup(String.format(
            "<!DOCTYPE html>%n" +
            "<html>%n<head>%n  <title>untitled</title>%n</head>%n" +
            "<body>%n%s%n</body>%n</html>",
            markup),
            parameters
        );
        page.add(c);
        tester.startPage(page);
    }
    
    /**
     * Returns the most recently rendered page as a String, as provided by
     * the WicketTester.
     */
    private static String document(WicketTester tester)
    {
        String doc = tester.getServletResponse().getDocument();
        Assert.assertNotNull(
            "HTTP body of rendered Wicket page cannot be null", doc
        );
        return doc;
    }
    
    /**
     * Creates either an Html5Validator or XHtmlValidator based on whether the
     * specific document appears to be HTML5 or not. It does this by looking
     * for the exact string {@code <!DOCTYPE html>} before the opening
     * {@code <html>} element. Documents with this doctype are assumed to be
     * HTML5. Otherwise the document is assumed to be some flavor of XHTML.
     */
    private static AbstractDocumentValidator validator(String document)
    {
        AbstractDocumentValidator validator;
        
        // Create a validator based on whether or not the document appears to
        // be HTML5. If not HTML5, assume some flavor of XHTML.
        int doctype = document.indexOf("<!DOCTYPE html>");
        if(doctype >= 0 && doctype < document.indexOf("<html"))
        {
            validator = new Html5Validator();
        }
        else
        {
            validator = new XHtmlValidator();
        }
        
        return validator;
    }
    
    /**
     * Counts the number of nodes that are matched by the given xpath
     * expression. The expression is evaluated against the most recently
     * rendered page in the WicketTester.
     */
    private static int matchCount(WicketTester tester, String xPathExpr)
            throws IOException, SAXException, ParserConfigurationException, TransformerException,
                   XPathExpressionException
    {
        NodeList nl = new XPathHelper(markupAsDOM(tester)).findNodes(xPathExpr);
        return nl != null ? nl.getLength() : 0;
    }
}
