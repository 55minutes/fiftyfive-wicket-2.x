package ${package};

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;

/**
 * Base class for Wicket unit tests. Allows Wicket pages and components to be
 * easily tested in isolation. For pages or components that rely on Spring
 * dependency injection, consider overriding {@link #initSpringContext}.
 */
public abstract class BaseWicketUnitTest
{
    protected ${session_classname} _session;
    protected WicketTester _tester;
    
    @Before
    public void createTester()
    {
        _tester = new WicketTester(new ${app_classname}() {
            @Override
            public String getConfigurationType()
            {
                // Don't test in development mode, since debug utilities
                // can break XHTML compliance.
                return DEPLOYMENT;
            }
            @Override
            protected ApplicationContext getApplicationContext()
            {
                // Provide a static Spring context that can be configured
                // with mock beans for testing purposes.
                StaticWebApplicationContext context;
                context = new StaticWebApplicationContext();
                initSpringContext(context);
                return context;
            }
            @Override
            public ${session_classname} newSession(Request request, Response response)
            {
                // Enforce a singleton session object to be used for the entire
                // test. This allows us to modify the contents of the session
                // with prerequisite values before starting a test.
                if(null == _session)
                {
                    _session = super.newSession(request, response);
                }
                return _session;
            }
        });
    }
    
    @After
    public void destroyTester()
    {
        if(_tester != null) _tester.destroy();
        _tester = null;
        _session = null;
    }
    
    /**
     * Subclasses should override this method to register mock Spring beans
     * in the Spring context. This will allow Wicket components that contain
     * SpringBean annotations to use these mock beans during testing.
     * <p>
     * The default implementation of this method calls
     * {@link MockitoAnnotations#initMocks MockitoAnnotations.initMocks()} to
     * initialize any <code>&#064;Mock</code> variables that have been
     * declared in the test class.
     * <p>
     * Example usage:
     * <pre class="example">
     * &#064;Mock MyService myService;
     * 
     * protected void initSpringContext(StaticWebApplicationContext ctx)
     * {
     *     super.initSpringContext(ctx);
     *     ctx.getBeanFactory().registerSingleton("myService", myService);
     * }</pre>
     */
    protected void initSpringContext(StaticWebApplicationContext ctx)
    {
        MockitoAnnotations.initMocks(this);
    }
}
