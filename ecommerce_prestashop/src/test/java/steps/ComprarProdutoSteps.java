package steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import pages.HomePage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.ProdutoPage;

public class ComprarProdutoSteps {

	private static WebDriver driver;
	private HomePage homePage = new HomePage(driver);

	@Before
	public static void inicializar() {
		System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\ChromeDrive\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Dado("que estou na pagina inicial")
	public void que_estou_na_pagina_inicial() {
		homePage.carregarPaginaInicial();
		assertThat(homePage.obterTituloPagina(), is("Loja de Teste"));

	}

	@Quando("nao estou logado")
	public void nao_estou_logado() {
		assertThat(homePage.estaLogado(), is(false));

	}

	@Entao("vizualizo {int} produtos disponiveis")
	public void vizualizo_produtos_disponiveis(Integer int1) {
		assertThat(homePage.contarProdutos(), is(int1));
	}

	@Entao("carrinho esta zerado")
	public void carrinho_esta_zerado() {
		assertThat(homePage.obterQuantidadeProdutosNoCarrinho(), is(0));
	}

	LoginPage loginPage;

	@Quando("estou logado")
	public void estou_logado() {
		// Clicar no botao Sign in na home page
		loginPage = homePage.clicarBotaoSignIn();

		// Preencher usuario e senha
		loginPage.preencherEmail("marcelo@teste.com");
		loginPage.preencherPassword("marcelo");

		// Clicar no bot�o Sign In para logar
		loginPage.clicarBotaoSignIn();

		// Validar se o usu�rio est� logado de fato
		assertThat(homePage.estaLogado("Marcelo Bittencourt"), is(true));

		homePage.carregarPaginaInicial();
	}

	ProdutoPage produtoPage;
	String nomeProduto_HomePage;
	String precoProduto_HomePage;
	String nomeProduto_ProdutoPage;
	String precoProduto_ProdutoPage;

	@Quando("seleciono o produto na posicao {int}")
	public void seleciono_o_produto_na_posicao(Integer indice) {
		nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		precoProduto_HomePage = homePage.obterPrecoProduto(indice);

		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);

		produtoPage = homePage.clicarProduto(indice);

		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
	}

	@Quando("o nome do produto na pagina inicial e na pagina produto eh {string}")
	public void o_nome_do_produto_na_pagina_inicial_eh(String nomeProduto) {
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto.toUpperCase()));
		assertThat(nomeProduto_ProdutoPage.toUpperCase(), is(nomeProduto.toUpperCase()));

	}

	@Quando("o preco do produto na pagina inicial e na pagina produto eh {string}")
	public void o_preco_do_produto_na_pagina_inicial_eh(String precoProduto) {
		assertThat(precoProduto_HomePage, is(precoProduto.toUpperCase()));
		assertThat(precoProduto_ProdutoPage, is(precoProduto.toUpperCase()));
	}
    ModalProdutoPage modalProdutoPage;
	@Quando("adiciono o produto no carrinho com tamanho {string} cor {string} e quantidade {int}")
	public void adiciono_o_produto_no_carrinho_com_tamanho_cor_e_quantidade(String tamanhoProduto, String corProduto,
			Integer quantidadeProduto) {
		// Selecionar tamanho
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();

		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());

		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);

		listaOpcoes = produtoPage.obterOpcoesSelecionadas();

		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());

		// Selecionar cor
		produtoPage.selecionarCorPreta();

		// Selecionar quantidade
		produtoPage.alterarQuantidade(quantidadeProduto);

		// Adicionar ao carrinho
		modalProdutoPage = produtoPage.clicarBotaoAddToCart();

		// Valida��es
		assertThat(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"), is(true));
	}

	@Entao("o produto aparece na confirmação com o nome {string} preco {string} tamanho {string} cor {string} e quantidade {int}")
	public void o_produto_aparece_na_confirmação_com_o_nome_preco_tamanho_cor_e_quantidade(String nomeProduto,
			String precoProduto, String tamanhoProduto, String corProduto, Integer quantidadeProduto) {
		
		Double precoProdutoDoubleEncontrado = Double.parseDouble(modalProdutoPage.obterPrecoProduto().replace("$", ""));
		Double precoProdutoDoubleEsperado = Double.parseDouble(precoProduto.replace("$", ""));

		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));
		
		String subtotalString = modalProdutoPage.obterSubTotal();
		subtotalString = subtotalString.replace("$", "");
		Double subtotalEncontrado = Double.parseDouble(subtotalString);
		
		Double subtotalCalculadoEsperado = quantidadeProduto * precoProdutoDoubleEsperado;
		
		assertThat(subtotalEncontrado, is(subtotalCalculadoEsperado));
	}

	@After
	public static void finalizar() {
		driver.quit();
	}

}
