import java.util.Random;

public class TabelaHashRehashing {

    public static void main(String[] args) {

        Random random = new Random(1090);   // seed = 1090
        int[] tabela = new int[100];        // matriz de 100 posicoes

        // -1 significa posicao livre
        for (int i = 0; i < 100; i++) {
            tabela[i] = -1;
        }

        int ocupadas = 0;          // quantas posicoes ja estao preenchidas
        int totalColisoes = 0;     // total de colisoes na insercao
        int bloqueados = 0;        // numeros que nao puderam ser inseridos

        // guarda cada elemento inserido e quantas colisoes ele teve
        int[] valores = new int[100];
        int[] colisoes = new int[100];
        int inseridos = 0;

        // insercao de 100 num
        for (int n = 0; n < 100; n++) {
            int valor = random.nextInt(100001);   // numero de 0 a 100000

            if (ocupadas >= 90) {
                // 90% de ocupacao o rehash impede novas insercoes
                bloqueados++;
            } else {
                int posicao = valor % 100;        // funcao hash
                int colisoesDoElemento = 0;

                // rehashing enquanto a posicao estiver ocupada, vai para a proxima
                while (tabela[posicao] != -1) {
                    colisoesDoElemento++;
                    posicao = (posicao + 1) % 100;   // r(i) = (i + 1) % tamanho
                }

                tabela[posicao] = valor;
                ocupadas++;
                totalColisoes = totalColisoes + colisoesDoElemento;

                valores[inseridos] = valor;
                colisoes[inseridos] = colisoesDoElemento;
                inseridos++;
            }
        }

        // resultado da insercao
        System.out.println("Elementos inseridos: " + inseridos);
        System.out.println("Elementos bloqueados (ocupacao de 90%): " + bloqueados);
        System.out.println("Total de colisoes: " + totalColisoes);

        // 3 elementos q mais colidiram
        boolean[] usado = new boolean[100];
        int[] top = new int[3];   // guarda o indice em valores[] de cada um dos 3

        for (int k = 0; k < 3; k++) {
            int melhor = -1;
            for (int i = 0; i < inseridos; i++) {
                if (usado[i] == false) {
                    if (melhor == -1 || colisoes[i] > colisoes[melhor]) {
                        melhor = i;
                    }
                }
            }
            usado[melhor] = true;
            top[k] = melhor;
        }

        System.out.println();
        System.out.println("Tres elementos que mais colidiram:");
        for (int k = 0; k < 3; k++) {
            System.out.println((k + 1) + ") valor " + valores[top[k]]
                    + " -> " + colisoes[top[k]] + " colisoes");
        }

        // posicoes livres
        System.out.println();
        System.out.print("Posicoes livres: ");
        for (int i = 0; i < 100; i++) {
            if (tabela[i] == -1) {
                System.out.print(i + " ");
            }
        }
        System.out.println();

        // teste de busca dos 3 elementos
        System.out.println();
        System.out.println("Teste de busca:");
        for (int k = 0; k < 3; k++) {
            int chave = valores[top[k]];
            int i = chave % 100;       // posicao inicial (hash)
            int passos = 1;            // quantas posicoes foram olhadas

            // para quando achar a chave ou uma posicao livre
            while (tabela[i] != chave && tabela[i] != -1) {
                i = (i + 1) % 100;
                passos++;
            }

            if (tabela[i] == chave) {
                System.out.println("Valor " + chave + " encontrado na posicao " + i
                        + " (hash dava " + (chave % 100) + ", " + passos + " posicoes olhadas)");
            } else {
                System.out.println("Valor " + chave + " NAO encontrado");
            }
        }
    }
}