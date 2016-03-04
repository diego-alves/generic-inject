package foo;

import com.google.inject.Guice;
import com.google.inject.Injector;
import foo.pages.GooglePage;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Created by diegoalves on 27/02/16.
 */
public class Program {

    @Inject
    GooglePage googlePage;

    @Inject
    WebDriver webDriver;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ProgramModule());
        injector.getInstance(Program.class).start();

    }

    private void start() {
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        String resultado = googlePage
                .abrir()
                .preencher_Busca("5+5")
                .clicar_Botao()
                .obter_Resultado();

        System.out.print(resultado);

    }
}
