
O fantasma vermelho usa o algoritmo A* para perseguir o jogador. O A* é um algoritmo de busca de caminho que encontra a rota mais curta entre dois pontos levando em conta:

- O custo real percorrido (g)
    
- Uma estimativa de distância até o destino (h)

## 🔍 Como o Algoritmo A* Funciona

A função de avaliação principal é:

### `f(n) = g(n) + h(n)`

- **g(n):** Custo real do caminho do início até o nó atual
    
- **h(n):** Heurística estimada da distância do nó atual até o destino

	Usando a distância de Manhataam


- **f(n):** Soma total, usada para priorizar nós na fila



---

## 📦 Estruturas utilizadas

### `openSet` (Lista aberta)

Contém nós que ainda serão explorados. Implementada como uma **PriorityQueue**:

java

CopyEdit

`PriorityQueue<Node> openSet = new PriorityQueue<>();`

### `closedSet` (Lista fechada)

Conjunto de nós já visitados, para evitar repetições:

java

CopyEdit

`Set<String> closedSet = new HashSet<>();`

---

## 🧠 Heurística (h)

Utilizamos a **distância de Manhattan**, que é apropriada para grids onde os movimentos são feitos apenas em quatro direções (cima, baixo, esquerda, direita):

java

CopyEdit

`private double calculateHeuristic(int x1, int y1, int x2, int y2) {     return Math.abs(x1 - x2) + Math.abs(y1 - y2); }`

---

## 🔁 Lógica de busca

1. Adiciona o nó inicial no `openSet`
    
2. Enquanto houver nós na lista aberta:
    
    - Seleciona o nó com menor `f`
        
    - Verifica se chegou ao destino
        
    - Explora os vizinhos válidos (não fora do mapa ou em obstáculos)
        
    - Calcula novo g, h e f
        
    - Adiciona os vizinhos ao `openSet` se forem promissores
        

java

CopyEdit

`while (!openSet.isEmpty()) {     Node current = openSet.poll(); // menor f(n)      if (current.x == goalX && current.y == goalY) {         return reconstructPath(current); // chegou ao destino     }      closedSet.add(current.getKey());      for (int[] dir : directions) {         // Lógica para cada vizinho         ...     } }`

---

## 🧩 Reconstrução do Caminho

Depois de encontrar o destino, o caminho é reconstruído de trás pra frente, usando os ponteiros `parent` dos nós:

java

CopyEdit

`private List<Node> reconstructPath(Node goal) {     List<Node> path = new ArrayList<>();     Node current = goal;     while (current != null) {         path.add(0, current);         current = current.parent;     }     return path; }`

---

## 🧱 Tratamento de obstáculos

As paredes do mapa são convertidas para uma matriz de células ocupadas, usada durante a verificação de vizinhos:

java

CopyEdit

`boolean[][] occupiedCells = new boolean[columns][rows]; for (Block wall : obstacles) {     int wallX = wall.getX() / tileSize;     int wallY = wall.getY() / tileSize;     occupiedCells[wallX][wallY] = true; }`

---

## 👻 Tomada de Decisão do Fantasma

Depois que o caminho é calculado, o fantasma pega o segundo nó da lista (o primeiro é a sua própria posição) e calcula a direção para onde deve se mover:

java

CopyEdit

`Node nextNode = lastPath.size() > 1 ? lastPath.get(1) : lastPath.get(0);  if (nextNode.x < ghostX) return 'L'; if (nextNode.x > ghostX) return 'R'; if (nextNode.y < ghostY) return 'U'; if (nextNode.y > ghostY) return 'D';`

Se não houver caminho válido, o fantasma se move aleatoriamente:

java

CopyEdit

`private char getRandomDirection() {     char[] directions = {'U', 'D', 'L', 'R'};     return directions[new Random().nextInt(4)]; }`

---

## 🕒 Otimização com Tempo

Para evitar recalcular o caminho a todo momento, há um intervalo de 250ms entre cada novo cálculo:

java

CopyEdit

`private static final long PATH_RECALCULATION_DELAY = 250;  if (lastPath.isEmpty() || currentTime - lastPathCalculationTime > PATH_RECALCULATION_DELAY) {     lastPath = findPath(...);     lastPathCalculationTime = currentTime; }`