

def main():
    filename1 = '../resources/output1.xyz'
    filename2 = '../resources/output5.xyz'
    data = []

    with open(filename1, 'r') as file1:
        with open(filename2, 'r') as file2:
            while True:
                # Leer la primera línea con el número N
                line1 = file1.readline()
                line2 = file2.readline()
                if not line1 and not line2:
                    break  # Fin del archivo

                # Leer N líneas y guardarlas en un array
                arr = []

                if line1:
                    n1 = int(line1.strip())
                    file1.readline()
                    for i in range(n1):
                        line1 = file1.readline()
                        arr.append(line1.strip())

                if line2:
                    n2 = int(line2.strip())
                    file2.readline()
                    for i in range(n2):
                        line2 = file2.readline()
                        arr.append(line2.strip())

                # Agregar el array a la lista de arrays
                data.append(arr)

    # Escribir los datos en un archivo
    with open('combined.xyz', 'w') as file:
        for arr in data:
            file.write(str(len(arr)) + '\n\n')
            for item in arr:
                file.write(item + '\n')


if __name__ == "__main__":
    main()
