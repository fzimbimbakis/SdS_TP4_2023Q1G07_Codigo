from matplotlib import pyplot as plt
import mpl_toolkits.axes_grid1.inset_locator as mpl_il

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

    # Crear el objeto Axes principal
    fig, ax = plt.subplots(figsize=(8, 6))

    # Dibujar las curvas en el objeto Axes principal
    ax.plot(time, pos, label="Verlet")
    ax.plot(time1, pos1, label="Beeman")
    ax.plot(time2, pos2, label="Analytic solution")
    ax.plot(time3, pos3, label="Gear predictor corrector")

    # Definir los límites de la lupa
    x1, x2, y1, y2 = 0.518, 0.522, 0.687, 0.688

    # Crear el objeto Axes para la región ampliada
    axins = mpl_il.inset_axes(ax, width="30%", height="30%", loc=1)

    # Dibujar la curva ampliada en el objeto Axes de la región ampliada
    axins.plot(time, pos)
    axins.plot(time1, pos1)
    axins.plot(time2, pos2)
    axins.plot(time3, pos3)

    # Definir los límites y la posición de la región ampliada
    axins.set_xlim(x1, x2)
    axins.set_ylim(y1, y2)
    ip = mpl_il.InsetPosition(ax, [0.6, 0.6, 0.3, 0.3])
    axins.set_axes_locator(ip)

    # Añadir una línea que conecte la región ampliada con la curva principal
    ax.indicate_inset_zoom(axins)

    # Definir las etiquetas de los ejes
    ax.set_xlabel('Tiempo [s]')
    ax.set_ylabel('Posicion [m]')
    ax.legend()

    # Mostrar el gráfico completo
    plt.show()
