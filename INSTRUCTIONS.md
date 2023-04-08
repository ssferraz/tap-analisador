# Instruções de instalação e execução do programa

##### [OPCIONAL] – Certifique-se de ter o Java instalado:
Verifique se você tem o Java instalado em seu sistema, digitando o seguinte comando no terminal:

	java --version
Se você não tiver o Java instalado, faça o download e instale-o em seu sistema.

##### [1] – Clone o repositório:
Abra o terminal e navegue até o diretório onde você deseja salvar o projeto. Em seguida, execute o seguinte comando:

	git clone https://github.com/ssferraz/tap-analisador.git

##### [2] – Mude o diretório atual para a pasta do projeto, executando o seguinte comando:

	cd tap-analisador

##### [3] – Digite esse comando para rodar o projeto no terminal.
	
	mkdir bin
	javac -d bin src/com/github/ssferraz/tap_analisador/Main.java
	java -cp bin com.github.ssferraz.tap_analisador.Main
  
##### [4] – Digite o nome do diretório.
###### Exemplo:

	vikings-first-season
##### [RESULTADO] - Será criado na raiz do projeto uma pasta chamada resultados com os arquivos .json
