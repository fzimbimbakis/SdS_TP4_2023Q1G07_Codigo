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

    time, pos = read_file("../resources/verlet2.txt")
    plt.plot(time, pos, label="Verlet")

    time1, pos1 = read_file("../resources/beeman2.txt")
    plt.plot(time1, pos1, label="Beeman")

    time2, pos2 = read_file("../resources/solution2.txt")
    plt.plot(time2, pos2, label="Analytic solution")

    time3, pos3 = read_file("../resources/gear2.txt")
    plt.plot(time3, pos3, label="Gear predictor corrector")

    plt.xlabel('Tiempo [s]')
    plt.ylabel('Posicion [m]')
    plt.legend()
    plt.savefig("../resources/graph.png")

    #se tiene que correr con el dt = 0.001
    plt.xlim(0.518, 0.528)
    plt.ylim(0.685,0.689)
    plt.savefig("../resources/zoom_graph.png")

