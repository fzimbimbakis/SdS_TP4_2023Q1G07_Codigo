import numpy as np
import matplotlib.pyplot as plt


def main():
    # Carga los datos de los archivos
    mean_times = np.loadtxt('../resources/pool_mean_times1.txt')
    std_times = np.loadtxt('../resources/pool_stdErr_times1.txt')

    # Genera los valores de x
    x = np.linspace(42.0, 56.0, 20)

    # Crea la figura y el gráfico
    fig, ax = plt.subplots()
    ax.errorbar(x, mean_times, yerr=std_times, fmt='o', color='black', ecolor='gray', capsize=2)

    # Personaliza el gráfico
    ax.set_xlabel('Componente y de la blanca')
    ax.set_ylabel('Tiempo medio (s)')

    # Muestra el gráfico
    plt.show()

if __name__ == '__main__':
    main()
