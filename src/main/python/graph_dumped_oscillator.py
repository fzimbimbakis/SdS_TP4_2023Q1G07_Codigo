from matplotlib import pyplot as plt
import numpy as np


def read_file(name):
    list_x = []
    list_y = []
    with open(name) as archivo:
        for linea in archivo:
                x_aux, y_aux= linea.split()
                list_x.append(float(x_aux))
                list_y.append(float(y_aux))
    return list_x , list_y




if __name__ == '__main__':
    plt.xlabel('Tiempo')
    plt.ylabel('Posicion')
    time, pos = read_file("../resources/verlet1.txt")
    plt.plot(time, pos)
    #plt.legend()
    plt.savefig("../resources/verlet.png")
    plt.clf()

    plt.xlabel('Tiempo')
    plt.ylabel('Posicion')
    time1, pos1 = read_file("../resources/beeman1.txt")
    plt.plot(time1, pos1)
    #plt.legend()
    plt.savefig("../resources/beeman.png")
    plt.clf()

    plt.xlabel('Tiempo')
    plt.ylabel('Posicion')
    time2, pos2 = read_file("../resources/solution1.txt")
    plt.plot(time2, pos2)
    #plt.legend()
    plt.savefig("../resources/solution.png")
    plt.clf()

    plt.xlabel('Tiempo')
    plt.ylabel('Posicion')
    time3, pos3 = read_file("../resources/gear1.txt")
    plt.plot(time3, pos3)
    #plt.legend()
    plt.savefig("../resources/gear.png")


