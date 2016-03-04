package foo.pages;

import com.google.inject.Provider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by diegoalves on 27/02/16.
 */
public class PageProvider<T> implements Provider<T> {

    private enum Action {
        ABRIR {
            @Override
            public String invoke(ActionArgs args) {
                args.driver.get(args.page.url());
                return null;
            }
        },
        PREENCHER {
            @Override
            public String invoke(ActionArgs args) {
                WebElement element = args.driver.findElement(By.cssSelector(args.field.selector()));
                element.sendKeys((String) args.args[0]);
                return null;
            }
        },
        CLICAR {
            @Override
            public String invoke(ActionArgs args) {
                WebElement element = args.driver.findElement(By.cssSelector(args.field.selector()));
                element.click();
                return null;
            }
        },
        OBTER {
            @Override
            public String invoke(ActionArgs args) {
                WebElement element = args.driver.findElement(By.cssSelector(args.field.selector()));
                return element.getText();
            }
        };

        public abstract String invoke(ActionArgs args);

    }

    private static class ActionArgs {
        WebDriver driver;
        Page page;
        Field field;
        Object[] args;
    }

    @Inject
    private WebDriver driver;

    private Class<T> type;

    public PageProvider(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        ActionArgs actionArgs = new ActionArgs();
                        actionArgs.driver = driver;
                        actionArgs.page = method.getDeclaringClass().getAnnotation(Page.class);
                        actionArgs.field = getField(actionArgs.page.fields(), method.getName());
                        actionArgs.args = args;

                        Action action = Action.valueOf(method.getName().split("_")[0].toUpperCase());

                        String invoke = action.invoke(actionArgs);

                        if(invoke == null)
                            return proxy;
                        else
                            return invoke;
                    }

                    private Field getField(Field[] fields, String name) {
                        String[] splited = name.split("_");
                        if(splited.length > 1){
                            for (Field field : fields) {
                                if(field.name().equals(splited[1]))
                                    return field;
                            }
                            throw new RuntimeException("No field with name "+splited[1]);
                        }
                        return null;
                    }
                }
        );

    }
}
