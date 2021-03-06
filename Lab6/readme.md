﻿В приложении визуализируется трехмерная каркасная модель буквы "П".

Для построения использовалась правосторонняя система координат.

Чтобы подвигать камерой, надо кликнуть в области системы координат, 
отпустить мышку и просто двигать ей - таким образом можно 
посмотреть на систему и фигуру в ней с разных ракурсов (вращение системы вокруг осей Ox, Oy). 
Если зажать при этом клавишу Shift, вращение происходит вокруг оси Oz.
Чтобы выйти с этого режима, нужно снова кликнуть мышкой в области системы координат. 
Сцену можно приближать и отдалять с помощью скролла мышкой.

Главное окно содержит 4 кнопки. 

При нажатии на кнопки "Oxy projection", "Oxz projection", "Oyz projection" 
в системе координат отображаются проекции фигуры на соответсвующие плоскости. 
При еще одном нажатии на кнопки эти проекции исчезают.

При нажатии на кнопку "Transformate" открывается дочернее окно "Transformate letter" для ввода данных 
(вещественных чисел) для трехмерной трансформации буквы: переноса, вращения, масштабирования по осям Ox, Oy, Oz 
соответственно. При вводе некорректных данных (пустой строки, букв или значения градусов больше 360 или меньше -360) 
введенные значения изменяются на значения по умолчанию (0 для переноса и градусов вращения и 1 для масштабирования).

После ввода данных при нажатии на кнопку "Transformate" в окне "Transformate letter", 
открывается новое дочернее окно "Transformation matrix" с итоговой матрицей
преобразования, окно "Transformate letter" при этом закрывается. После нажатия кнопки, 
все ранее произведенные трансформации сбрасываются - т.е. введенные для трансформации данные
применяются к букве в исходном состоянии (матрица вершин исходной буквы умножается на полученную матрицу преобразования).

Матрицы преобразования умножаются в порядке T * R * S (T - translate, R - rotate, S - scale),
таким образом исходная буква сначала масштабируется, затем вращается, затем переносится вдоль осей.
