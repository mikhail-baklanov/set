В maven/conf/settings.xml прописать (для использования плагина jetty):
	<pluginGroups>
		<pluginGroup>org.mortbay.jetty</pluginGroup>
	</pluginGroups>

Сборка: 
	mvn clean package
Результат сборки см. в папке target.

Сборка и запуск сервера: 
	mvn clean package jetty:run
Приложение запускается на текущем хосте в корне домена на порту 80, т.е. для запуска приложения в браузере необходимо набрать:
	http://localhost
