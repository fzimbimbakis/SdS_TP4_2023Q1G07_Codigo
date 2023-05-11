import matplotlib.pyplot as plt

def main():
    # Leemos los valores de phi1.txt y phi2.txt
    with open('../resources/phi1.txt', 'r') as file:
        phi1 = [float(line) for line in file]

    with open('../resources/phi2.txt', 'r') as file:
        phi2 = [float(line) for line in file]

    with open('../resources/phi3.txt', 'r') as file:
         phi3 = [float(line) for line in file]

    # Creamos una figura y un subplot
    fig, ax = plt.subplots(1, 1)

    # Graficamos phi1 y phi2 en el mismo subplot
    ax.plot([0.01 * i for i in range(0, 10001)], phi1, label='k = 2')
    ax.plot([0.01 * i for i in range(0, 10001)], phi2, label='k = 3')
    ax.plot([0.01 * i for i in range(0, 10001)], phi3, label='k = 4')
    ax.set_xlim(-1, 20)
    ax.set_yscale('log')
    ax.legend()

    # Mostramos la figura
    plt.show()

if __name__ == '__main__':
    main()