package com.example.rna.exam.ia;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ReaderText {
    int[][] matriz = new int[21][63]; // Crear una matriz de 21 filas y 63 columnas
    double factorA;
    double bias;
    double weights[];
    int epocaa;
    int[][] etiquetas = {
            {1,0,0,0,0,0,0},
            {0,1,0,0,0,0,0},
            {0,0,1,0,0,0,0},
            {0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0},
            {0,0,0,0,0,1,0},
            {0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0},
            {0,1,0,0,0,0,0},
            {0,0,1,0,0,0,0},
            {0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0},
            {0,0,0,0,0,1,0},
            {0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0},
            {0,1,0,0,0,0,0},
            {0,0,1,0,0,0,0},
            {0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0},
            {0,0,0,0,0,1,0},
            {0,0,0,0,0,0,1}
    }; // Las salidas esperadas para cada entrada

    public int[][] leerArchivo(String contenido) {
        System.out.println("Contenido del archivo:");
        System.out.println(contenido);

        int fila = 0;
        int columna = 0;

        for (int i = 0; i < contenido.length() && fila < 21; i++) {
            char caracter = contenido.charAt(i);
            if (caracter == '#' || caracter == '@') {
                matriz[fila][columna] = 1;
            } else if (caracter == '.' || caracter == 'o') {
                matriz[fila][columna] = -1;
            }

            if (caracter != '\n' && caracter != '\r') { // Ignorar los saltos de línea
                columna++;
            }

            if (columna == 63) { // Si se alcanza la columna 63, pasar a la siguiente fila
                fila++;
                columna = 0;
            }
        }

        System.out.println("Matriz resultante:");
        imprimirMatriz(matriz);

        return matriz;
    }

    private void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
    public double fNet(int fila[], double[] weights, double bias) {
        // Inicializar la suma
        double suma = 0;

        // Multiplicar cada elemento de la fila por su peso correspondiente y sumarlo a la suma
        for (int i = 0; i < fila.length; i++) {
            suma += fila[i] * weights[i]; // Multiplicar el valor de la fila con el peso correspondiente y sumarlo
        }

        // Sumar el sesgo multiplicado por el peso correspondiente
        suma += bias;

        // Retornar el resultado
        return suma;
    }

    public String entrenar(ParametrosEntrenamiento parametrosEntrenamiento) {
        StringBuilder resultadoEntrenamiento = new StringBuilder();
        resultadoEntrenamiento.append("| Epoca | Porcentaje de Aprendizaje | Pesos \n");
        resultadoEntrenamiento.append("|-------|---------------------------|-------|\n");

        // Iterar a través de las épocas de entrenamiento
        for (int epoca = 0; epoca < parametrosEntrenamiento.getEpocas(); epoca++) {
            int prediccionesCorrectas = 0; // Inicializar contador de predicciones correctas

            // Iterar a través de cada entrada y su salida esperada
            for (int i = 0; i < matriz.length; i++) {
                // Calcular la salida utilizando los pesos actuales
                double resultado = fNet(matriz[i], parametrosEntrenamiento.getWeights(), parametrosEntrenamiento.getBias());

                // Aplicar la función de activación
                int prediccion = resultado > 0 ? 1 : 0;

                // Comprobar si la predicción es correcta
                if (prediccion == etiquetas[i][Math.min(epoca, etiquetas[i].length - 1)]) {
                    prediccionesCorrectas++; // Incrementar el contador si la predicción es correcta
                }

                // Actualizar los pesos si la predicción es incorrecta
                if (epoca < etiquetas[i].length && prediccion != etiquetas[i][epoca]) {
                    for (int j = 0; j < parametrosEntrenamiento.getWeights().length; j++) {
                        parametrosEntrenamiento.getWeights()[j] += parametrosEntrenamiento.getAlpha() * (etiquetas[i][epoca] - prediccion) * matriz[i][j];
                    }
                    parametrosEntrenamiento.setBias(parametrosEntrenamiento.getBias() + parametrosEntrenamiento.getAlpha() * (etiquetas[i][epoca] - prediccion));
                }
            }

            // Calcular el porcentaje de predicciones correctas
            double porcentajeAprendizaje = (double) prediccionesCorrectas / matriz.length * 100;

            // Construir la cadena con los resultados de la época actual
            resultadoEntrenamiento.append(String.format("|   %-3d   |          %-7.1f%%         | ", epoca + 1, porcentajeAprendizaje));
            weights= parametrosEntrenamiento.getWeights();
            bias= parametrosEntrenamiento.getBias();
            factorA= parametrosEntrenamiento.getAlpha();
            epocaa= parametrosEntrenamiento.getEpocas();
            resultadoEntrenamiento.append(Arrays.toString(parametrosEntrenamiento.getWeights()) + " " + parametrosEntrenamiento.getBias() + "\n");
        }

        // Retornar la cadena con todos los resultados
        return resultadoEntrenamiento.toString();
    }

    public int predecir(int fila[], double[] weights, double bias) {
        // Calcular la salida utilizando fNet
        double resultado = fNet(fila, weights, bias);

        // Aplicar la función de activación
        return resultado > 0 ? 1 : 0;
    }
    public String test() {
        StringBuilder resultadoEntrenamiento = new StringBuilder();
        resultadoEntrenamiento.append("| Epoca | Porcentaje de Aprendizaje | Pesos \n");
        resultadoEntrenamiento.append("|-------|---------------------------|-------|\n");

        // Iterar a través de las épocas de entrenamiento
        for (int epoca = 0; epoca < epocaa; epoca++) {
            int prediccionesCorrectas = 0; // Inicializar contador de predicciones correctas

            // Iterar a través de cada entrada y su salida esperada
            for (int i = 0; i < matriz.length; i++) {
                // Calcular la salida utilizando los pesos actuales
                double resultado = fNet(matriz[i], weights, bias);

                // Aplicar la función de activación
                int prediccion = resultado > 0 ? 1 : 0;

                // Comprobar si la predicción es correcta
                if (prediccion == etiquetas[i][Math.min(epoca, etiquetas[i].length - 1)]) {
                    prediccionesCorrectas++; // Incrementar el contador si la predicción es correcta
                }

                // Actualizar los pesos si la predicción es incorrecta
                if (epoca < etiquetas[i].length && prediccion != etiquetas[i][epoca]) {
                    for (int j = 0; j < weights.length; j++) {
                        weights[j] += factorA * (etiquetas[i][epoca] - prediccion) * matriz[i][j];
                    }
                }
            }

            // Calcular el porcentaje de predicciones correctas
            double porcentajeAprendizaje = (double) prediccionesCorrectas / matriz.length * 100;

            // Construir la cadena con los resultados de la época actual
            resultadoEntrenamiento.append(String.format("|   %-3d   |          %-7.1f%%         | ", epoca + 1, porcentajeAprendizaje));
            resultadoEntrenamiento.append(Arrays.toString(weights) + " " + bias + "\n");
        }

        // Retornar la cadena con todos los resultados
        return resultadoEntrenamiento.toString();

    }
}