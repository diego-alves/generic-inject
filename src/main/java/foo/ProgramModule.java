package foo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.google.inject.AbstractModule;
import foo.pages.Page;
import foo.pages.PageProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.reflections.Reflections;

/**
 * Created by diegoalves on 27/02/16.
 */
public class ProgramModule extends AbstractModule {

    @Override
    protected void configure() {
        //bind(WebDriver.class).to(HtmlUnitDriver.class).in(Singleton.class);
        bind(WebDriver.class).toInstance(new HtmlUnitDriver(BrowserVersion.FIREFOX_38));

        for (Class pageClass : new Reflections("foo.pages").getTypesAnnotatedWith(Page.class)) {
            bind(pageClass).toProvider(new PageProvider(pageClass));
        }

    }


}
