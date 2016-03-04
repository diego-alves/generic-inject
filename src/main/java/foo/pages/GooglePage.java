package foo.pages;

/**
 * Created by diegoalves on 27/02/16.
 */
@Page(
    title="Google",
    url="https://www.google.com.br",
    fields = {
        @Field(name="Busca", selector = "#lst-ib"),
        @Field(name="Botao", selector = "button[name=\"btnG\"]"),
        @Field(name="Resultado", selector = ".cwcot")

    }
)
public interface GooglePage {

    GooglePage abrir();

    GooglePage preencher_Busca(String text);

    GooglePage clicar_Botao();

    String obter_Resultado();

}
