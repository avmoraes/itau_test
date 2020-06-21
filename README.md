# itau_test

Desafio Itaú

Objetivo: Criar uma aplicação mobile de notas pessoais (exemplo: google keep). A aplicação deverá ter pelo menos três activities, sendo estas:

1.       Tela de login: esta tela deverá ter integração com o firebase para a validação de e-mail e senha.

 

E-mail: notaspessoais@desafioitau.com

Senha: admin123

 

Caso tenha um e-mail diferente como entrada, deverá haver um tratamento de erro.

 

BONUS: perguntar se deseja salvar e-mail e senha no primeiro login

 

2.       Tela de exibição das notas: as notas deverão estar formato de lista – utilizar um recyclerview de preferência. Cada item da lista deverá exibir, um título, uma descrição breve e um número de 1 à 5 para indicar a prioridade do assunto. Ao clicar em algum item, deverá abrir uma activity que permite a visualização e edição das informações da nota. Os items deverão ser salvos de forma persistente (SQLite/Room). Além do mais, ao dar um swipe em algum item, este deverá ser excluído. A tela deverá ter um botão para a adição de novas notas e dois items de menu na toolbar: um para a exclusão de todos os items de uma vez o outro para logout.

 

3.       Tela para inserir e salvar dados: Os dados deverão ser: título, descrição breve, descrição completa e um spinner para definir a prioridade. Além deverá haver um botão/menu na toolbar para salver as informações. Como dito anteriormente, os dados deverão ser salvos de forma persistente, utilizando SQLite ou Room.
