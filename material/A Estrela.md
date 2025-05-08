
O fantasma vermelho está usando o A*, ele tem 3 **funções de cálculo**:

- g(): a função que calcula a **distância do fantasma até dado nó**.
- h(): a função heurística que diz a **distância do pacman (objetivo)** até esse nó.
- f(): **a soma entre g() e h()**.

 E duas listas:

- `openSet`: caminhos à serem visitados dado o nó atual que acabou de sofrer a expansão
- `closedSet`: caminhos já visitados e portanto devem ser desconsiderando quando encontrando o caminho.

## Teoria

O A* é essencialmente Dijkstra para encontrar o menor caminho, mas Dijkstra leva em conta todas as posições possíveis, enquanto o A* tem o suporte de uma **heurística - h()** para saber se **vale à pena ou não expandir**.

Ele consegue isso fazendo o cálculo da distância de Manhattam entre o nó atual e o nó do pacman, ou seja, **ele direciona à expansão do algoritmo de Dijkstra**.

Se pensássemos que o fantasma é um ratinho e o pacman um queijo, o uso de Dijskstra seria como o ratinho validando todos os caminhos até o queijo (sem senso) e então escolhendo o de menor custo (mais próximo).

Já com o A*, que contém uma função h() de cálculo de heurística seria como **a capacidade do ratinho de sentir o cheiro queijo**, ignorando aqueles caminhos onde o cheiro é mais fraco pois eles certamente não estão te aproximando do queijo, e portanto, buscando o queijo ou pacman mais eficientemente.
