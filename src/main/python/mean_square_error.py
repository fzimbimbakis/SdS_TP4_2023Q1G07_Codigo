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
    return list_x , np.array(list_y)




if __name__ == '__main__':

    dts = [0.01, 0.001, 0.0001, 0.00001]
    verlet_err = []
    gear_err = []
    beeman_err = []

    for i in range(1, 5):
        sol_time, sol_pos = read_file("../resources/solution"+str(i)+".txt")

        verlet_time, verlet_pos = read_file("../resources/verlet"+str(i)+".txt")
        verlet_err.append(np.mean(np.power(np.subtract(sol_pos,verlet_pos),2)))

        beeman_time, beeman_pos = read_file("../resources/beeman"+str(i)+".txt")
        beeman_err.append(np.mean(np.power(np.subtract(sol_pos,beeman_pos),2)))


        gear_time, gear_pos = read_file("../resources/gear"+str(i)+".txt")
        gear_err.append(np.mean(np.power(np.subtract(sol_pos,gear_pos),2)))


    plt.plot(dts, verlet_err)
    plt.scatter(dts, verlet_err, label="Verlet")
    plt.plot(dts, beeman_err)
    plt.scatter(dts, beeman_err, label="Beeman")
    plt.plot(dts, gear_err)
    plt.scatter(dts, gear_err, label="Gear predictor corrector")
    plt.xlabel('Dts')
    plt.ylabel('Error cuadrático medio')
    plt.semilogy()
    plt.semilogx()
    plt.legend()
    plt.savefig("../resources/MSE.png")