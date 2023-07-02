# JSON Comparator
Программа позволяет сравнивать 2 JSON файла, а точнее объекты внутри файлов на предмет полного совпадения, частичного совпадения или отсутствие совпадений по указанным ключам, на наличие дублей объектов внутри файлов, а так же формировать стильный и довольно подробный отчет в формате HTML.

### Программа умеет:
#### Сравнение
- в файле JSON находить массив с объектами для сравнения по указанному пути;
- сравнивать объекты по перечисленным ключам (включая многократные совпадения);
- сравнивать строки без учета регистра;
- сравнивать строки с обрезанием пробелов по краям;
- находить дубликаты объектов в файлах;
#### Формировать отчет 
- по полностью, частично или не совпавшим объектам;
- возможность показывать объекты со всеми ключами или только по сравниваемым ключам;
- добавлять порядковые номера объектам;
- добавлять запятые между объектами;
- формировать объекты в одну строку или с заданным количеством отступов;
- автоматический сохранять результат сравнения в папку в формате HTML и открывать его в браузере.
#### Кроме того:
- в программе имеется руководство пользователя;
- у программы современный темный дизайн;
- есть указание версии программы и проверка версии конфигурационного файла на совместимость с программой;
- есть ссылка на этот github проект;
- программу можно перемещать по экрану удерживая нажатой л.к.м. на темной части заголовка.
## Внешний вид
Скриншот основного окна программы:

![Основное окно программы](https://github.com/ShamilAbd/JSONComparator/blob/main/screenshots/main_window.PNG)

Скриншот верхней части формируемого отчета:

![Начало отчета](https://github.com/ShamilAbd/JSONComparator/blob/main/screenshots/result_page_1.PNG)

Другие скриншоты можно посмотреть в папке screenshots.

## Требования к сравниваемым файлам
Файлы должны иметь расширение *.json и внутри файла объекты должны быть расположенны непосредственно в массиве или в каком-то поле JSON объекта тоже в виде массива (см. пример файлов для сравнения).

## Как начать пользоваться программой
1) Для начала скачайте jar файл программы со страницы [релиза](https://github.com/ShamilAbd/JSONComparator/releases).
2) Убедитесь что на вашем компьютере установлена JRE (виртуальная машина Java) не ниже 11 версии, если ее нет, то загуглите, скачайте и установите.
3) Запустите скачанный jar файл и далее по инструкции из руководства пользователя.

