# language: pt
Funcionalidade: Comprar produto
  Como um usuario logado
  Eu quero escolher um produto
  e visualizar esse produto no carrinho
  Para concluir o pedido

  @validacaoinicial
  Cenario: Deve mostrar uma lista de oito produtos na pagina inicial
    Dado que estou na pagina inicial
    Quando nao estou logado
    Entao vizualizo 8 produtos disponiveis
    E carrinho esta zerado

  @fluxopadrao
  Cenario: Deve mostrar produto escolhido confirmado
    Dado que estou na pagina inicial
    Quando estou logado
    E seleciono o produto na posicao <posicao>
    E o nome do produto na pagina inicial e na pagina produto eh <nomeProduto>
    E o preco do produto na pagina inicial e na pagina produto eh <precoProduto>
    E adiciono o produto no carrinho com tamanho <tamanhoProduto> cor <corProduto> e <quantidadeProduto>
    Entao o produto aparece na confirmação com o nome "Hummingbird printed t-shirt" preco "$19.12 SAVE 20%" tamanho "M" cor "Black" e quantidade 2
    
    Exemplos: 
      | posicao | nomeProduto                   | precoProduto      | tamanhoProduto | corProduto | quantidadeProduto |
      |       0 | "Hummingbird printed t-shirt" | "$19.12 SAVE 20%" | "M"            | "Black"    |                 2 |
