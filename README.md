# OpenDotaMobile
an android application which displays various player/match info via opendota api.

Документация разработчика для OpenDotaMobile

Введение

OpenDotaMobile - мобильное приложение для просмотра игровой статистики Dota 2. Данная документация предоставляет информацию о структуре приложения, используемых технологиях и API, а также руководство по разработке новых функций и поддержке существующего кода.

Содержание

Обзор архитектуры приложения
Интеграция с OpenDota API
Работа с базой данных
Разработка новых функций
Тестирование и отладка

1. Обзор архитектуры приложения

OpenDotaMobile разработан с использованием паттерна MVVM (Model-View-ViewModel) для обеспечения четкого разделения ответственности между компонентами приложения и возможности масштабирования проекта.

Model: Отвечает за доступ к данным (OpenDota API, база данных, кеширование) и бизнес-логику приложения.
View: Отвечает за представление данных и взаимодействие с пользователем (UI-элементы, анимации, навигация).
ViewModel: Отвечает за связь между Model и View, обеспечивая передачу данных и обработку пользовательских действий.

2. Интеграция с OpenDota API

OpenDotaMobile использует официальное API Dota 2 для доступа к игровой статистике, информации о матчах и турнирах. Для работы с API потребуется ключ доступа, который можно получить на официальном сайте Dota 2.

В проекте должен быть создан модуль для работы с API, который будет содержать функции для выполнения запросов к различным конечным точкам API и обработки полученных данных.

3. Работа с базой данных

Для сохранения локальной статистики, настроек приложения и кеширования данных используется локальная база данных. В качестве базы данных можно использовать встраиваемую SQLite.

Создайте модуль для работы с базой данных, который будет содержать функции для выполнения операций CRUD (создание, чтение, обновление, удаление) с данными, а также предоставлять интерфейс для сохранения настроек и кеширования данных.

4. Разработка новых функций

При добавлении новых функций в приложение следует придерживаться принципов модульности и переиспользования кода. Для каждой новой функции создайте отдельный компонент, который будет отвечать за свою часть функциональности.

Следуйте принципам MVVM при разработке компонентов и взаимодействии между ними. Используйте Redux для управления состоянием приложения и передачи данных между компонентами.
