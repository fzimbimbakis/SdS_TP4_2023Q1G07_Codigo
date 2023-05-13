import matplotlib.pyplot as plt
import numpy as np

def main():
    # Leemos los valores de phi1.txt y phi2.txt
    with open('../resources/outputs/phi3.txt', 'r') as file:
        phi1 = [float(line) for line in file]

    with open('../resources/outputs/phi4.txt', 'r') as file:
        phi2 = [float(line) for line in file]

    with open('../resources/outputs/phi5.txt', 'r') as file:
         phi3 = [float(line) for line in file]

    with open('../resources/outputs/phi6.txt', 'r') as file:
        phi4 = [float(line) for line in file]

    # Creamos una figura y un subplot
    fig, ax = plt.subplots(1, 1)

    # Graficamos phi1 y phi2 en el mismo subplot
    ax.plot([0.01 * i for i in range(0, 10001)], phi1, label='k = 2')
    ax.plot([0.01 * i for i in range(0, 10001)], phi2, label='k = 3')
    ax.plot([0.01 * i for i in range(0, 10001)], phi3, label='k = 4')
    ax.plot([0.01 * i for i in range(0, 10001)], phi4, label='k = 5')
    ax.set_xlim(-2, 20)
    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel('Diferencia entre posiciones (cm)')
    ax.set_yscale('log')
    ax.legend()

    # Mostramos la figura
    plt.show()

    plt.clf()

    n = 5000
    n_sqrt = n**(1/2)

    mean1 = np.mean(np.array(phi1[:n]))
    mean2 = np.mean(np.array(phi2[:n]))
    mean3 = np.mean(np.array(phi3[:n]))
    mean4 = np.mean(np.array(phi4[:n]))
    std1 = np.std(np.array(phi1[:n]))/n_sqrt
    std2 = np.std(np.array(phi2[:n]))/n_sqrt
    std3 = np.std(np.array(phi3[:n]))/n_sqrt
    std4 = np.std(np.array(phi4[:n]))/n_sqrt

    values = [2, 3, 4, 5]
    mean = [mean1, mean2, mean3, mean4]
    std = [std1, std2, std3, std4]

    plt.errorbar(values, mean, yerr=std, fmt='o', markersize=5, capsize=2)
    plt.ylim(0, 1500)
    plt.ylabel('Diferencia promedio (cm)')
    plt.xlabel('k')
    plt.show()





if __name__ == '__main__':
    main()